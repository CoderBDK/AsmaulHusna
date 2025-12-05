package com.coderbdk.asmaulhusna.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.asmaulhusna.data.local.db.entity.LanguageEntity
import com.coderbdk.asmaulhusna.domain.usecase.GetLanguageListUseCase
import com.coderbdk.asmaulhusna.domain.usecase.ObserveSettingsUseCase
import com.coderbdk.asmaulhusna.domain.usecase.SetAppLanguageUseCase
import com.coderbdk.asmaulhusna.domain.usecase.ToggleDarkModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val isDarkTheme: Boolean = false,
    val isLoading: Boolean = false,
    val selectedLanguageId: Int = 1,
    val languageList: List<LanguageEntity> = emptyList(),
)

sealed interface SettingsUiEvent {
    object NavigateBack : SettingsUiEvent
    data class ToggleTheme(val isDark: Boolean) : SettingsUiEvent
    data class SetLanguage(val languageId: Int) : SettingsUiEvent
}

sealed class SettingsSideEffect {
    object NavigateUp : SettingsSideEffect()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    observeSettingsUseCase: ObserveSettingsUseCase,
    private val toggleDarkModeUseCase: ToggleDarkModeUseCase,
    private val setAppLanguageUseCase: SetAppLanguageUseCase,
    private val getLanguageListUseCase: GetLanguageListUseCase,
) : ViewModel() {

    private val languageListFlow: StateFlow<List<LanguageEntity>> = flow {
        try {
            emit(getLanguageListUseCase())
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val uiState: StateFlow<SettingsUiState> = combine(
        observeSettingsUseCase(),
        languageListFlow
    )
    { settingsModel, languageList ->
        SettingsUiState(
            isDarkTheme = settingsModel.isDarkMode,
            selectedLanguageId = settingsModel.selectedLanguageId,
            isLoading = false,
            languageList = languageList
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState(isLoading = true)
        )

    private val _sideEffect = Channel<SettingsSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun handleEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.ToggleTheme -> handleToggleTheme(event.isDark)
            is SettingsUiEvent.SetLanguage -> handleSetLanguage(event.languageId)
            is SettingsUiEvent.NavigateBack -> sendSideEffect(SettingsSideEffect.NavigateUp)
        }
    }

    private fun handleToggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            toggleDarkModeUseCase(isDark)
        }
    }

    private fun handleSetLanguage(languageId: Int) {
        viewModelScope.launch {
            setAppLanguageUseCase(languageId)
        }
    }

    private fun sendSideEffect(effect: SettingsSideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}