package com.coderbdk.asmaulhusna.ui.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coderbdk.asmaulhusna.ui.navigation.AsmaulHusnaNavRoute

@Composable
fun OnboardingRoute(
    onNavigateToHome: (routeToPopUpFrom: AsmaulHusnaNavRoute) -> Unit,

    ) {
    val viewModel = hiltViewModel<OnboardingViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                OnboardingSideEffect.NavigateToHome -> onNavigateToHome(AsmaulHusnaNavRoute.Onboarding)
                is OnboardingSideEffect.ShowToast -> {

                }
            }
        }
    }

    OnboardingScreen(uiState = uiState, onEvent = viewModel::handleEvent)
}