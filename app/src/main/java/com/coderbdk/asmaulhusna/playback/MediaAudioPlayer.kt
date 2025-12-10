package com.coderbdk.asmaulhusna.playback

import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.coderbdk.asmaulhusna.BuildConfig
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaFull
import com.coderbdk.asmaulhusna.ui.audio.AudioPlaybackState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MediaAudioPlayer @Inject constructor(
    @ApplicationContext context: Context,
) : AudioPlayerManager, Player.Listener {

    private val player: ExoPlayer = ExoPlayer.Builder(context).build()
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var positionUpdateJob: Job? = null
    private var playlist = emptyList<AsmaulHusnaFull>()
    private var isPlaylistLoaded = false

    private val _playbackState = MutableStateFlow(
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
            loopMode = LoopMode.OFF
        )
    )
    override val playbackState: StateFlow<AudioPlaybackState> = _playbackState.asStateFlow()

    private companion object {
        const val TAG = "MediaAudioPlayer"
    }

    init {
        player.addListener(this)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        _playbackState.update {
            it.copy(isPlaying = isPlaying)
        }

        if (isPlaying) {
            startPositionUpdateJob()
        } else {
            positionUpdateJob?.cancel()
            positionUpdateJob = null
        }
    }

    private fun startPositionUpdateJob() {

        positionUpdateJob?.cancel()

        positionUpdateJob = scope.launch {
            while (isActive) {
                updatePlaybackPosition()
                delay(100)
            }
        }
    }

    private fun updatePlaybackPosition() {
        if (player.isPlaying) {
            _playbackState.update {
                it.copy(
                    playbackPositionMs = player.currentPosition,
                    playbackDurationMs = player.duration.coerceAtLeast(0),
                )
            }
        }
    }

    override fun loadPlaylist(names: List<AsmaulHusnaFull>, startIndex: Int) {
        if (names.isEmpty()) return

        playlist = names
        player.clearMediaItems()

        val mediaItems = names.map { name ->
            name.audioFile.let { fileName ->
                val url = "http://${BuildConfig.LOCAL_SERVER_IP}/audio/$fileName"
                Log.i(TAG, url)
                MediaItem.Builder()
                    .setUri(url)
                    .setMediaId(name.number.toString())
                    .build()
            }
        }

        player.setMediaItems(mediaItems, startIndex.coerceIn(0, mediaItems.lastIndex), 0)
        player.prepare()
        isPlaylistLoaded = true
        updateCurrentAsmaulHusna()
    }

    override fun loadMedia(number: Int) {

        val targetIndex = playlist.indexOfFirst { it.number == number }

        if (targetIndex != -1) {
            player.seekToDefaultPosition(targetIndex)

            if (!player.isPlaying) {
                player.play()
            }

            updateCurrentAsmaulHusna()
        } else {
            _playbackState.update {
                it.copy(
                    error = "Error: Asmaul Husna number $number not found in playlist. Cannot load media."
                )
            }
        }
    }

    override fun needsPlaylistLoading(): Boolean {
        return !isPlaylistLoaded
    }

    override fun togglePlayPause() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        updateCurrentAsmaulHusna()
    }

    private fun updateCurrentAsmaulHusna() {
        val currentMediaId = player.currentMediaItem?.mediaId?.toIntOrNull() ?: return
        val currentAsma = playlist.find { it.number == currentMediaId } ?: return

        _playbackState.update {
            it.copy(
                asmaulHusna = currentAsma,
                playbackDurationMs = player.duration.coerceAtLeast(0),
                playbackPositionMs = player.currentPosition,
                error = null
            )
        }
    }


    override fun skipToNext() {
        player.seekToNextMediaItem()
    }

    override fun skipToPrevious() {
        player.seekToPreviousMediaItem()
    }

    override fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
    }

    override fun toggleLoopMode() {
        val nextMode = when (_playbackState.value.loopMode) {
            LoopMode.OFF -> LoopMode.ONE
            LoopMode.ONE -> LoopMode.ALL
            LoopMode.ALL -> LoopMode.OFF
        }

        player.repeatMode = when (nextMode) {
            LoopMode.OFF -> Player.REPEAT_MODE_OFF
            LoopMode.ONE -> Player.REPEAT_MODE_ONE
            LoopMode.ALL -> Player.REPEAT_MODE_ALL
        }

        _playbackState.update { it.copy(loopMode = nextMode) }
    }

    override fun releasePlayer() {
        positionUpdateJob?.cancel()
        player.removeListener(this)
        player.release()
        scope.cancel()
    }
}