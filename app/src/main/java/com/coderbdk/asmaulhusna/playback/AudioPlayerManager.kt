package com.coderbdk.asmaulhusna.playback

import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaFull
import com.coderbdk.asmaulhusna.ui.audio.AudioPlaybackState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerManager {

    val playbackState: StateFlow<AudioPlaybackState>

    fun loadPlaylist(names: List<AsmaulHusnaFull>, startIndex: Int)
    fun loadMedia(number: Int)
    fun needsPlaylistLoading(): Boolean
    fun togglePlayPause()

    fun skipToNext()
    fun skipToPrevious()
    fun seekTo(positionMs: Long)

    fun toggleLoopMode()

    fun releasePlayer()
}