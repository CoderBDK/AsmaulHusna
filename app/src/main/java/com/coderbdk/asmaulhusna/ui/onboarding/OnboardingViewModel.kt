package com.coderbdk.asmaulhusna.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.asmaulhusna.data.local.db.entity.LanguageEntity
import com.coderbdk.asmaulhusna.data.repository.SettingsRepository
import com.coderbdk.asmaulhusna.domain.usecase.GetLanguageListUseCase
import com.coderbdk.asmaulhusna.domain.usecase.InitializeAsmaulHusnaDataUseCase
import com.coderbdk.asmaulhusna.domain.usecase.SetAppLanguageUseCase
import com.coderbdk.asmaulhusna.domain.usecase.SetFirstTimeUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class OnboardingStep {
    object Welcome : OnboardingStep()
    object LanguageSelection : OnboardingStep()
    object FeatureHighlights : OnboardingStep()
    object FinalGetStarted : OnboardingStep()
}
data class OnboardingUiState(
    val currentStep: OnboardingStep = OnboardingStep.Welcome,
    val languageList: List<LanguageEntity> = emptyList(),
    val isDataLoading: Boolean = false,
    val error: String? = null
)
sealed interface OnboardingUiEvent {
    data class LanguageSelected(val langId: Int) : OnboardingUiEvent
    object NextButtonClicked : OnboardingUiEvent
    object GetStartedClicked : OnboardingUiEvent
}

sealed interface OnboardingSideEffect {
    object NavigateToHome : OnboardingSideEffect
    data class ShowToast(val message: String) : OnboardingSideEffect
}

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val setAppLanguageUseCase: SetAppLanguageUseCase,
    private val initializeAsmaulHusnaDataUseCase: InitializeAsmaulHusnaDataUseCase,
    private val getLanguageListUseCase: GetLanguageListUseCase,
    private val setFirstTimeUserUseCase: SetFirstTimeUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<OnboardingSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        loadLanguages()
    }

    fun handleEvent(event: OnboardingUiEvent) {
        when (event) {
            is OnboardingUiEvent.NextButtonClicked -> advanceStep()
            is OnboardingUiEvent.LanguageSelected -> completeLanguageSetup(event.langId)
            is OnboardingUiEvent.GetStartedClicked -> completeOnboardingFlow()
        }
    }

    private fun advanceStep() {
        _uiState.update {
            it.copy(currentStep = when (it.currentStep) {
                OnboardingStep.Welcome -> OnboardingStep.LanguageSelection
                OnboardingStep.LanguageSelection -> OnboardingStep.FeatureHighlights
                OnboardingStep.FeatureHighlights -> OnboardingStep.FinalGetStarted
                OnboardingStep.FinalGetStarted -> it.currentStep
            })
        }
    }

    private fun loadLanguages() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(languageList = getLanguageListUseCase()) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Failed to load languages") }
            }
        }
    }

    private fun completeLanguageSetup(selectedLangId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isDataLoading = true) }

            setAppLanguageUseCase(selectedLangId)

            initializeAsmaulHusnaDataUseCase()

            _uiState.update {
                it.copy(isDataLoading = false, currentStep = OnboardingStep.FeatureHighlights)
            }
        }
    }

    private fun completeOnboardingFlow() {
        viewModelScope.launch {
            if (_uiState.value.isDataLoading) {
                _sideEffect.send(OnboardingSideEffect.ShowToast("Please wait for data setup to finish."))
                return@launch
            }

            setFirstTimeUserUseCase(false)

            _sideEffect.send(OnboardingSideEffect.NavigateToHome)
        }
    }
}