package com.coderbdk.asmaulhusna.ui.audio

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coderbdk.asmaulhusna.R
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaFull
import com.coderbdk.asmaulhusna.playback.LoopMode
import com.coderbdk.asmaulhusna.ui.components.BackNavigationButton
import com.coderbdk.asmaulhusna.ui.theme.AsmaulHusnaTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlaybackScreen(
    uiState: AudioPlaybackState,
    onEvent: (AudioPlaybackEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                },
                navigationIcon = {
                    BackNavigationButton(
                        onClick = onNavigateBack
                    )
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1f),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = uiState.asmaulHusna.arabicName,
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = uiState.asmaulHusna.transliteration,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = uiState.asmaulHusna.meaning,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.weight(1f))
            PlaybackSlider(
                uiState = uiState,
                onSeek = { pos -> onEvent(AudioPlaybackEvent.Seek(pos)) },
            )

            PlaybackControls(uiState = uiState, onEvent = onEvent)

        }
    }
}

@Composable
fun PlaybackSlider(
    uiState: AudioPlaybackState,
    onSeek: (Long) -> Unit,
) {
    val duration = uiState.playbackDurationMs

    val playerSliderValue = if (duration > 0) uiState.playbackPositionMs.toFloat() / duration.toFloat() else 0f

    var isSeeking by remember { mutableStateOf(false) }

    var localSeekValue by remember { mutableFloatStateOf(playerSliderValue) }

    if (!isSeeking) {
        localSeekValue = playerSliderValue
    }

    Column(Modifier.fillMaxWidth()) {
        Slider(
            value = localSeekValue,
            onValueChange = { newValue ->
                isSeeking = true
                localSeekValue = newValue
            },

            onValueChangeFinished = {
                val seekPosition = (localSeekValue * duration).toLong()

                onSeek(seekPosition)

                isSeeking = false
            },

            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val timeToDisplay = if (isSeeking) (localSeekValue * duration).toLong() else uiState.playbackPositionMs

            Text(formatTime(timeToDisplay), style = MaterialTheme.typography.labelSmall)
            Text(formatTime(duration), style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun PlaybackControls(uiState: AudioPlaybackState, onEvent: (AudioPlaybackEvent) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LoopModeButton(
            uiState = uiState,
            onToggle = { onEvent(AudioPlaybackEvent.ToggleLoop) }
        )

        IconButton(onClick = { onEvent(AudioPlaybackEvent.SkipPrevious) }) {
            Icon(
                painterResource(R.drawable.baseline_skip_previous_24),
                contentDescription = "Previous"
            )
        }

        FilledIconButton(
            onClick = { onEvent(AudioPlaybackEvent.PlayPause) },
            modifier = Modifier.size(72.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            val icon =
                if (uiState.isPlaying) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24
            Icon(painterResource(icon), contentDescription = "Play/Pause", Modifier.size(40.dp))
        }

        IconButton(onClick = { onEvent(AudioPlaybackEvent.SkipNext) }) {
            Icon(painterResource(R.drawable.baseline_skip_next_24), contentDescription = "Next")
        }
    }
}

@Composable
fun LoopModeButton(uiState: AudioPlaybackState, onToggle: () -> Unit) {
    val icon = when (uiState.loopMode) {
        LoopMode.OFF -> R.drawable.outline_repeat_24
        LoopMode.ONE -> R.drawable.outline_repeat_one_24
        LoopMode.ALL -> R.drawable.baseline_repeat_24
    }

    val tint =
        if (uiState.loopMode != LoopMode.OFF) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    IconButton(onClick = onToggle) {
        Icon(
            painterResource(icon),
            contentDescription = "Loop Mode",
            tint = tint
        )
    }
}

fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val seconds = totalSeconds % 60
    val minutes = totalSeconds / 60
    return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
}


@Preview(showBackground = true)
@Composable
fun AudioPlaybackPreview() {
    AsmaulHusnaTheme {
        AudioPlaybackScreen(
            uiState = AudioPlaybackState(
                asmaulHusna = AsmaulHusnaFull(
                    number = 1,
                    arabicName = "ٱلرَّحْمَـٰنُ",
                    audioFile = "ar_rahman.mp3",
                    isFavorite = false,
                    transliteration = "Ar-Rahmān",
                    meaning = "The Most Merciful",
                    description = "Allah is the One whose mercy is abundant, encompassing all creation.",
                    languageName = "English"
                )
            ),
            onEvent = {}
        ) { }
    }
}