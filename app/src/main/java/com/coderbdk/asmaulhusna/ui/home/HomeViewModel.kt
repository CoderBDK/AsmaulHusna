package com.coderbdk.asmaulhusna.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaFull
import com.coderbdk.asmaulhusna.domain.usecase.GetAsmaulHusnaListUseCase
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

data class HomeUiState(
    val isLoading: Boolean = false,
    val asmaulHusnaList: List<AsmaulHusnaFull> = emptyList(),
    val error: String? = null,
    val query: String = ""
)

sealed interface HomeUiEvent {
    data class SetFavoriteStatus(val number: Int, val isFavorite: Boolean) : HomeUiEvent
    object RefreshData : HomeUiEvent
    data class NavigateToDetails(val number: Int) : HomeUiEvent
    data object NavigateToSettings : HomeUiEvent
    data class QueryChange(val query: String) : HomeUiEvent
}

sealed interface HomeSideEffect {
    data class ShowToast(val message: String) : HomeSideEffect
    data class NavigateToDetails(val number: Int) : HomeSideEffect
    data object NavigateToSettings : HomeSideEffect
}


@HiltViewModel
class HomeViewModel @Inject constructor(
    getAsmaulHusnaListUseCase: GetAsmaulHusnaListUseCase,
    observeSettingsUseCase: ObserveSettingsUseCase,
    private val setFavoriteStatusUseCase: SetFavoriteStatusUseCase
) : ViewModel() {

    private val _queryState = MutableStateFlow("")


    val uiState: StateFlow<HomeUiState> = observeSettingsUseCase()
        .combine(getAsmaulHusnaListUseCase()) { settings, allAsmas ->
            Pair(settings, allAsmas)
        }
        .combine(_queryState) { (settings, allAsmas), currentQuery ->
            val filteredList = if (currentQuery.isBlank()) {
                allAsmas
            } else {
                allAsmas.filter { asma ->
                    asma.arabicName.contains(currentQuery, ignoreCase = true) ||
                            asma.number.toString().contains(currentQuery) ||
                            asma.meaning.contains(currentQuery, ignoreCase = true)
                }
            }
            HomeUiState(
                isLoading = false,
                asmaulHusnaList = filteredList,
                query = currentQuery
            )
        }
        .onStart {
            emit(HomeUiState(isLoading = true))
        }
        .catch { e ->
            emit(
                HomeUiState(
                    isLoading = false,
                    error = e.localizedMessage ?: "Unknown error occurred."
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(isLoading = true)
        )

    private val _sideEffect = Channel<HomeSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun handleEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.SetFavoriteStatus -> setFavoriteStatus(event.number, event.isFavorite)
            HomeUiEvent.RefreshData -> {
            }

            is HomeUiEvent.NavigateToDetails -> {
                sendSideEffect(HomeSideEffect.NavigateToDetails(event.number))
            }

            is HomeUiEvent.NavigateToSettings -> {
                sendSideEffect(HomeSideEffect.NavigateToSettings)
            }

            is HomeUiEvent.QueryChange -> {
                _queryState.value = event.query
            }
        }
    }

    private fun setFavoriteStatus(number: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                setFavoriteStatusUseCase(number, isFavorite)
                val message = if (isFavorite) "Favorite added." else "Favorite removed."
                sendSideEffect(HomeSideEffect.ShowToast(message))
            } catch (e: Exception) {
                sendSideEffect(HomeSideEffect.ShowToast("Failed to update favorite status."))
            }
        }
    }

    private fun sendSideEffect(effect: HomeSideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}