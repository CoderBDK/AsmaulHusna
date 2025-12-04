package com.coderbdk.asmaulhusna.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.asmaulhusna.data.remote.model.Resource
import com.coderbdk.asmaulhusna.domain.model.UpdateCheckResult
import com.coderbdk.asmaulhusna.domain.usecase.CheckAppUpdateUseCase
import com.coderbdk.asmaulhusna.domain.usecase.InitializeLanguageDataUseCase
import com.coderbdk.asmaulhusna.domain.usecase.ObserveSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SplashUiState(val isLoading: Boolean)

sealed interface SplashSideEffect {
    object NavigateToHome : SplashSideEffect
    object NavigateToOnboarding : SplashSideEffect
    data class NavigateToUpdate(val updateResult: UpdateCheckResult) : SplashSideEffect
    data class ShowError(val message: String) : SplashSideEffect
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val observeSettingsUseCase: ObserveSettingsUseCase,
    private val initializeLanguageDataUseCase: InitializeLanguageDataUseCase,
    private val checkAppUpdateUseCase: CheckAppUpdateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState(isLoading = true))
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<SplashSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        initializeAndNavigate()
    }

    private fun initializeAndNavigate() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = initializeLanguageDataUseCase()

            if (result is Resource.Error) {
                _sideEffect.send(
                    SplashSideEffect.ShowError(
                        result.message
                    )
                )
                return@launch
            }

            val settings = observeSettingsUseCase().first()

            if (settings.isFirstTimeUser) {
                _sideEffect.send(SplashSideEffect.NavigateToOnboarding)
            } else {
                handleUpdateCheck()
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun handleUpdateCheck() {
        _uiState.update { it.copy(isLoading = true) }
        val updateResult = checkAppUpdateUseCase()
        _uiState.update { it.copy(isLoading = false) }

        when (updateResult) {
            is UpdateCheckResult.ForceAppUpdate -> {
                _sideEffect.send(SplashSideEffect.NavigateToUpdate(updateResult))
            }

            is UpdateCheckResult.DataUpdateNeeded -> {
                _sideEffect.send(SplashSideEffect.NavigateToUpdate(updateResult))
            }

            UpdateCheckResult.NoUpdateNeeded -> {
                _sideEffect.send(SplashSideEffect.NavigateToHome)
            }
        }
    }
}
