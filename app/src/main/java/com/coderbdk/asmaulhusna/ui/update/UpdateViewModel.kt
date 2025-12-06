package com.coderbdk.asmaulhusna.ui.update

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.coderbdk.asmaulhusna.data.remote.model.AppConfigResponse
import com.coderbdk.asmaulhusna.data.remote.model.Resource
import com.coderbdk.asmaulhusna.domain.model.DataModule
import com.coderbdk.asmaulhusna.domain.model.UpdateCheckResult
import com.coderbdk.asmaulhusna.domain.model.decodeToUpdateCheckResult
import com.coderbdk.asmaulhusna.domain.usecase.CheckAppUpdateUseCase
import com.coderbdk.asmaulhusna.domain.usecase.UpdateAppDataUseCase
import com.coderbdk.asmaulhusna.ui.navigation.AsmaulHusnaNavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UpdateUiState(
    val isLoading: Boolean = true,
    val isUpdateInProgress: Boolean = false,
    val isDataUpdateMandatory: Boolean = false,
    val isUpdateAvailable: Boolean = false
)

sealed interface UpdateSideEffect {
    object NavigateToHome : UpdateSideEffect
    data class ShowAppUpdateDialog(val remoteVersion: Int) : UpdateSideEffect
    data class ShowError(val message: String) : UpdateSideEffect
}

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val checkAppUpdateUseCase: CheckAppUpdateUseCase,
    private val updateAppDataUseCase: UpdateAppDataUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UpdateUiState())
    val uiState: StateFlow<UpdateUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<UpdateSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private var modulesToUpdate: List<DataModule> = emptyList()
    private var remoteConfig: AppConfigResponse? = null

    init {
        // checkAndNavigate()
        viewModelScope.launch {
            handleReceivedUpdateResult()
        }
    }

    private suspend fun handleReceivedUpdateResult() {
        _uiState.update { it.copy(isLoading = true) }
        val data = savedStateHandle.toRoute<AsmaulHusnaNavRoute.Update>().data
            ?: run {
                _sideEffect.send(UpdateSideEffect.ShowError("Update check failed during Splash."))
                _uiState.update { it.copy(isLoading = false) }
                return
            }

        val updateResult = data.decodeToUpdateCheckResult()
        _uiState.update { it.copy(isLoading = false) }
        when (updateResult) {
            is UpdateCheckResult.ForceAppUpdate -> {
                _sideEffect.send(UpdateSideEffect.ShowAppUpdateDialog(updateResult.remoteVersion))
            }

            is UpdateCheckResult.DataUpdateNeeded -> {
                modulesToUpdate = updateResult.modules
                remoteConfig = updateResult.remoteConfig

                _uiState.update {
                    it.copy(
                        isDataUpdateMandatory = remoteConfig?.isDataUpdateMandatory ?: false,
                        isUpdateAvailable = true
                    )
                }
            }

            UpdateCheckResult.NoUpdateNeeded -> {
                _sideEffect.send(UpdateSideEffect.NavigateToHome)
            }
        }
    }

    private fun checkAndNavigate() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val updateResult = checkAppUpdateUseCase()
            _uiState.update { it.copy(isLoading = false) }

            when (updateResult) {
                is UpdateCheckResult.ForceAppUpdate -> {
                    _sideEffect.send(UpdateSideEffect.ShowAppUpdateDialog(updateResult.remoteVersion))
                }

                is UpdateCheckResult.DataUpdateNeeded -> {
                    modulesToUpdate = updateResult.modules
                    remoteConfig = updateResult.remoteConfig

                    _uiState.update {
                        it.copy(
                            isDataUpdateMandatory = remoteConfig?.isDataUpdateMandatory ?: false,
                            isUpdateAvailable = true
                        )
                    }
                }

                UpdateCheckResult.NoUpdateNeeded -> {
                    _sideEffect.send(UpdateSideEffect.NavigateToHome)
                }
            }
        }
    }

    fun handleDataUpdateEvent() {
        viewModelScope.launch {
            val modules = modulesToUpdate
            val config = remoteConfig

            if (modules.isEmpty() || config == null) {
                _sideEffect.send(UpdateSideEffect.ShowError("Update data not available."))
                return@launch
            }

            _uiState.update { it.copy(isUpdateInProgress = true) }

            val result = updateAppDataUseCase(
                modulesToUpdate = modules,
                remoteConfig = config
            )

            _uiState.update { it.copy(isUpdateInProgress = false) }

            if (result is Resource.Success) {
                _sideEffect.send(UpdateSideEffect.NavigateToHome)
            } else if (result is Resource.Error) {
                _sideEffect.send(UpdateSideEffect.ShowError(result.message))
            }
        }
    }
}