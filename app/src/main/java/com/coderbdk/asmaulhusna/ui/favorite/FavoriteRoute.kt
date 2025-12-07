package com.coderbdk.asmaulhusna.ui.favorite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun FavoriteRoute(onNavigateUp: () -> Unit, onNavigateToDetails: (Int) -> Unit) {
    val viewModel = hiltViewModel<FavoriteViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is FavoriteSideEffect.ShowToast -> {

                }

                is FavoriteSideEffect.NavigateUp -> onNavigateUp()
                is FavoriteSideEffect.NavigateToDetails -> onNavigateToDetails(effect.number)
            }
        }
    }

    FavoriteScreen(
        uiState = state,
        onEvent = viewModel::handleEvent,
    )
}