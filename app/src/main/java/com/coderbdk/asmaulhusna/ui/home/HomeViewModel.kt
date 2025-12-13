package com.coderbdk.asmaulhusna.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaFull
import com.coderbdk.asmaulhusna.domain.usecase.GetAsmaulHusnaListUseCase
import com.coderbdk.asmaulhusna.domain.usecase.ObserveSettingsUseCase
import com.coderbdk.asmaulhusna.domain.usecase.SetFavoriteStatusUseCase
import com.coderbdk.asmaulhusna.playback.AudioPlayerManager
import com.coderbdk.asmaulhusna.ui.audio.AudioPlaybackEvent
import com.coderbdk.asmaulhusna.ui.audio.AudioPlaybackState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val asmaulHusnaList: List<AsmaulHusnaFull> = emptyList(),
    val error: String? = null,
    val query: String = "",
    val playerState: AudioPlaybackState? = null
)

sealed interface HomeUiEvent {
    data class SetFavoriteStatus(val number: Int, val isFavorite: Boolean) : HomeUiEvent
    object RefreshData : HomeUiEvent
    data class NavigateToDetails(val number: Int) : HomeUiEvent
    data object NavigateToSettings : HomeUiEvent
    data object NavigateToFavorite : HomeUiEvent
    data class QueryChange(val query: String) : HomeUiEvent
    data object PlayPause : HomeUiEvent
    data class ToggleAudio(val asmaulHusna: AsmaulHusnaFull): HomeUiEvent
}

sealed interface HomeSideEffect {
    data class ShowToast(val message: String) : HomeSideEffect
    data class NavigateToDetails(val number: Int) : HomeSideEffect
    data object NavigateToSettings : HomeSideEffect
    data object NavigateToFavorite : HomeSideEffect
}


@HiltViewModel
class HomeViewModel @Inject constructor(
    getAsmaulHusnaListUseCase: GetAsmaulHusnaListUseCase,
    observeSettingsUseCase: ObserveSettingsUseCase,
    private val setFavoriteStatusUseCase: SetFavoriteStatusUseCase,
    private val audioPlayerManager: AudioPlayerManager
) : ViewModel() {

    private val _queryState = MutableStateFlow("")

    private val baseDataFlow = observeSettingsUseCase()
        .combine(getAsmaulHusnaListUseCase()) { settings, allAsmas ->
            Pair(settings, allAsmas)
        }

    val uiState: StateFlow<HomeUiState> = combine(
        baseDataFlow,
        _queryState,
        audioPlayerManager.playbackState
    ) { (settings, allAsmas), currentQuery, audioState ->
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
            query = currentQuery,
            playerState = audioState
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

    init {
        viewModelScope.launch {
            if (audioPlayerManager.needsPlaylistLoading()){
                audioPlayerManager.loadPlaylist(
                    getAsmaulHusnaListUseCase().first(),
                    0
                )
            }
        }
    }
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

            is HomeUiEvent.NavigateToFavorite -> {
                sendSideEffect(HomeSideEffect.NavigateToFavorite)
            }

            is HomeUiEvent.QueryChange -> {
                _queryState.value = event.query
            }

            is HomeUiEvent.PlayPause -> audioPlayerManager.togglePlayPause()

            is HomeUiEvent.ToggleAudio -> toggleAudio(event.asmaulHusna)
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

    fun toggleAudio(item: AsmaulHusnaFull) {

        val currentState = audioPlayerManager.playbackState.value

        val isSameItem = currentState.asmaulHusna.number == item.number

        if (isSameItem) {
            audioPlayerManager.togglePlayPause()
        } else {
            audioPlayerManager.loadMedia(item.number)
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (audioPlayerManager.playbackState.value.isPlaying) {
            audioPlayerManager.togglePlayPause()
        }
    }
}