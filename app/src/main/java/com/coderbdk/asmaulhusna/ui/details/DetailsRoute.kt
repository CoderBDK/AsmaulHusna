package com.coderbdk.asmaulhusna.ui.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DetailsRoute(onNavigateUp: () -> Unit) {
    val viewModel = hiltViewModel<DetailsViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                DetailsSideEffect.NavigateUp -> onNavigateUp()
                is DetailsSideEffect.PlayAudio -> {}
                is DetailsSideEffect.ShowToast -> {}
            }
        }
    }
    DetailsScreen(
        uiState = uiState,
        onEvent = viewModel::handleEvent
    )
}