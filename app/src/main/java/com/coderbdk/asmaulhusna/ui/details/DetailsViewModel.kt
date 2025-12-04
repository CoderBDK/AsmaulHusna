package com.coderbdk.asmaulhusna.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaFull
import com.coderbdk.asmaulhusna.domain.usecase.GetAsmaulHusnaListUseCase
import com.coderbdk.asmaulhusna.ui.navigation.AsmaulHusnaNavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class DetailsUiState(
    val asmaulHusna: AsmaulHusnaFull,
    val asmaulHusnaList: List<AsmaulHusnaFull> = emptyList(),
    val currentNumber: Int = 0
)
sealed class DetailsUiEvent {
    object NavigateBack : DetailsUiEvent()
    object OnPreviousClicked : DetailsUiEvent()
    object OnNextClicked : DetailsUiEvent()
    object PlayFullAudio : DetailsUiEvent()
}

sealed class DetailsSideEffect {
    object NavigateUp : DetailsSideEffect()
    data class ShowToast(val message: String) : DetailsSideEffect()
    data class PlayAudio(val number: Int) : DetailsSideEffect()
}

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getAsmaulHusnaListUseCase: GetAsmaulHusnaListUseCase,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            DetailsUiState(
                asmaulHusna = AsmaulHusnaFull(
                    1,
                    "",
                    "",
                    false,
                    "",
                    "",
                    "",
                    ""
                )
            )
        )
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<DetailsSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        viewModelScope.launch {
            val asmaulHusnaList = getAsmaulHusnaListUseCase().first()
            _uiState.update {
                val number = savedStateHandle.toRoute<AsmaulHusnaNavRoute.Details>().number
                val targetIndex = (number - 1).coerceIn(0, asmaulHusnaList.lastIndex)
                it.copy(
                    asmaulHusna = asmaulHusnaList[targetIndex],
                    asmaulHusnaList = asmaulHusnaList,
                )
            }
        }

    }

    fun handleEvent(event: DetailsUiEvent) {
        when (event) {
            DetailsUiEvent.NavigateBack -> sendSideEffect(DetailsSideEffect.NavigateUp)

            DetailsUiEvent.OnNextClicked -> updateCurrentNumber(1)
            DetailsUiEvent.OnPreviousClicked -> updateCurrentNumber(-1)

            DetailsUiEvent.PlayFullAudio -> playAudio()
        }
    }

    private fun updateCurrentNumber(direction: Int) {
        viewModelScope.launch {
            val list = uiState.value.asmaulHusnaList
            if (list.isEmpty()) return@launch

            val maxNumber = list.lastIndex
            val current = uiState.value.currentNumber
            var next = current + direction

            if (next > maxNumber) {
                next = 0
            } else if (next < 1) {
                next = maxNumber
            }
            _uiState.update {
                it.copy(
                    currentNumber = next,
                    asmaulHusna = list[next]
                )
            }
        }
    }

    private fun playAudio() {
        sendSideEffect(DetailsSideEffect.PlayAudio(uiState.value.asmaulHusna.number))
    }

    private fun sendSideEffect(effect: DetailsSideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}