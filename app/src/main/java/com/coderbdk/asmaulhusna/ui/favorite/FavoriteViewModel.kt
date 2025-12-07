package com.coderbdk.asmaulhusna.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaEntity
import com.coderbdk.asmaulhusna.domain.usecase.GetFavoriteAsmaulHusnaUseCase
import com.coderbdk.asmaulhusna.domain.usecase.ObserveSettingsUseCase
import com.coderbdk.asmaulhusna.domain.usecase.SetFavoriteStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoriteUiState(
    val isLoading: Boolean = false,
    val asmaulHusnaList: List<AsmaulHusnaEntity> = emptyList(),
    val error: String? = null,
    val query: String = ""
)

sealed interface FavoriteUiEvent {
    data class SetFavoriteStatus(val number: Int, val isFavorite: Boolean) : FavoriteUiEvent
    object RefreshData : FavoriteUiEvent
    data class NavigateToDetails(val number: Int) : FavoriteUiEvent
    data object NavigateBack: FavoriteUiEvent
    data class QueryChange(val query: String) : FavoriteUiEvent
}

sealed interface FavoriteSideEffect {
    data class ShowToast(val message: String) : FavoriteSideEffect
    data class NavigateToDetails(val number: Int) : FavoriteSideEffect
    data object NavigateUp: FavoriteSideEffect
}

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    getAsmaulHusnaListUseCase: GetFavoriteAsmaulHusnaUseCase,
    observeSettingsUseCase: ObserveSettingsUseCase,
    private val setFavoriteStatusUseCase: SetFavoriteStatusUseCase
) : ViewModel() {
    private val _queryState = MutableStateFlow("")


    val uiState: StateFlow<FavoriteUiState> = observeSettingsUseCase()
        .combine(getAsmaulHusnaListUseCase()) { settings, allAsmas ->
            Pair(settings, allAsmas)
        }
        .combine(_queryState) { (settings, allAsmas), currentQuery ->
            val filteredList = if (currentQuery.isBlank()) {
                allAsmas
            } else {
                allAsmas.filter { asma ->
                    asma.arabicName.contains(currentQuery, ignoreCase = true) ||
                            asma.number.toString().contains(currentQuery)

                }
            }
            FavoriteUiState(
                isLoading = false,
                asmaulHusnaList = filteredList,
                query = currentQuery
            )
        }
        .onStart {
            emit(FavoriteUiState(isLoading = true))
        }
        .catch { e ->
            emit(
                FavoriteUiState(
                    isLoading = false,
                    error = e.localizedMessage ?: "Unknown error occurred."
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FavoriteUiState(isLoading = true)
        )

    private val _sideEffect = Channel<FavoriteSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun handleEvent(event: FavoriteUiEvent) {
        when (event) {
            is FavoriteUiEvent.SetFavoriteStatus -> setFavoriteStatus(
                event.number,
                event.isFavorite
            )

            FavoriteUiEvent.RefreshData -> {
            }

            FavoriteUiEvent.NavigateBack -> sendSideEffect(FavoriteSideEffect.NavigateUp)
            is FavoriteUiEvent.NavigateToDetails -> {
                sendSideEffect(FavoriteSideEffect.NavigateToDetails(event.number))
            }
            is FavoriteUiEvent.QueryChange -> {
                _queryState.value = event.query
            }
        }
    }

    private fun setFavoriteStatus(number: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                setFavoriteStatusUseCase(number, isFavorite)
                val message = if (isFavorite) "Favorite added." else "Favorite removed."
                sendSideEffect(FavoriteSideEffect.ShowToast(message))
            } catch (e: Exception) {
                sendSideEffect(FavoriteSideEffect.ShowToast("Failed to update favorite status."))
            }
        }
    }

    private fun sendSideEffect(effect: FavoriteSideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}