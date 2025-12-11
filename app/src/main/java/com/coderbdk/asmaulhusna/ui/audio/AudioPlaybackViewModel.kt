package com.coderbdk.asmaulhusna.ui.audio

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaFull
import com.coderbdk.asmaulhusna.domain.usecase.GetAsmaulHusnaListUseCase
import com.coderbdk.asmaulhusna.playback.AudioPlayerManager
import com.coderbdk.asmaulhusna.playback.LoopMode
import com.coderbdk.asmaulhusna.ui.navigation.AsmaulHusnaNavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AudioPlaybackState(
    val asmaulHusna: AsmaulHusnaFull,
    val isPlaying: Boolean = false,
    val playbackPositionMs: Long = 0L,
    val playbackDurationMs: Long = 0L,
    val loopMode: LoopMode = LoopMode.OFF,
    val error: String? = null
)

sealed class AudioPlaybackEvent {
    object PlayPause : AudioPlaybackEvent()
    object SkipNext : AudioPlaybackEvent()
    object SkipPrevious : AudioPlaybackEvent()
    data class Seek(val positionMs: Long) : AudioPlaybackEvent()
    data class LoadMedia(val number: Int) : AudioPlaybackEvent()
    object ToggleLoop : AudioPlaybackEvent()
}

@HiltViewModel
class AudioPlaybackViewModel @Inject constructor(
    private val playerManager: AudioPlayerManager,
    private val getAsmaulHusnaListUseCase: GetAsmaulHusnaListUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    val uiState: StateFlow<AudioPlaybackState> = playerManager.playbackState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            AudioPlaybackState(
                asmaulHusna = AsmaulHusnaFull(
                    number = 1,
                    arabicName = "ٱلرَّحْمَـٰنُ",
                    audioFile = "ar_rahman.mp3",
                    isFavorite = false,
                    transliteration = "Ar-Rahmān",
                    meaning = "The Most Merciful",
                    description = "Allah is the One whose mercy is abundant, encompassing all creation.",
                    languageName = "English"
                ),
            )
        )

    init {
        val initialNumber = savedStateHandle.toRoute<AsmaulHusnaNavRoute.AudioPlayback>().number
        viewModelScope.launch {
            if (playerManager.needsPlaylistLoading()){
                playerManager.loadPlaylist(
                    getAsmaulHusnaListUseCase().first(),
                    initialNumber
                )
            }
            handleEvent(AudioPlaybackEvent.LoadMedia(initialNumber))
        }

    }

    fun handleEvent(event: AudioPlaybackEvent) {
        when (event) {
            AudioPlaybackEvent.PlayPause -> playerManager.togglePlayPause()
            AudioPlaybackEvent.SkipNext -> playerManager.skipToNext()
            AudioPlaybackEvent.SkipPrevious -> playerManager.skipToPrevious()
            is AudioPlaybackEvent.Seek -> playerManager.seekTo(event.positionMs)
            is AudioPlaybackEvent.LoadMedia -> playerManager.loadMedia(event.number)
            is AudioPlaybackEvent.ToggleLoop -> playerManager.toggleLoopMode()
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (playerManager.playbackState.value.isPlaying) {
            playerManager.togglePlayPause()
        }
    }
}