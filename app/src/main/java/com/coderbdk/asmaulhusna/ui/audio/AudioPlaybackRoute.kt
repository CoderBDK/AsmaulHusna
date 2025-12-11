package com.coderbdk.asmaulhusna.ui.audio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun AudioPlaybackRoute(onNavigateUp: () -> Unit) {
    val viewModel = hiltViewModel<AudioPlaybackViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AudioPlaybackScreen(
        uiState = uiState,
        onEvent = viewModel::handleEvent,
        onNavigateBack = onNavigateUp
    )
}