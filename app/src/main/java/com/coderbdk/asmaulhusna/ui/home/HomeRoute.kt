package com.coderbdk.asmaulhusna.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun HomeRoute(
    onNavigateToDetails: (Int) -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is HomeSideEffect.ShowToast -> {

                }

                is HomeSideEffect.NavigateToDetails -> onNavigateToDetails(effect.number)
                HomeSideEffect.NavigateToSettings -> onNavigateToSettings()
            }
        }
    }

    HomeScreen(
        uiState = state,
        onEvent = viewModel::handleEvent,
    )
}