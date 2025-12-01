package com.coderbdk.asmaulhusna.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coderbdk.asmaulhusna.domain.model.UpdateCheckResult
import com.coderbdk.asmaulhusna.ui.navigation.AsmaulHusnaNavRoute

@Composable
fun SplashRoute(
    onNavigateToHome: (routeToPopUpFrom: AsmaulHusnaNavRoute) -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToUpdate: (UpdateCheckResult) -> Unit,
) {
    val viewModel = hiltViewModel<SplashViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                SplashSideEffect.NavigateToHome -> onNavigateToHome(AsmaulHusnaNavRoute.Splash)
                SplashSideEffect.NavigateToOnboarding -> onNavigateToOnboarding()
                is SplashSideEffect.NavigateToUpdate -> onNavigateToUpdate(effect.updateResult)
                is SplashSideEffect.ShowError -> {

                }
            }
        }
    }

    SplashScreen(uiState)
}
