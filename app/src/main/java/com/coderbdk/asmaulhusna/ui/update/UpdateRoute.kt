package com.coderbdk.asmaulhusna.ui.update

import android.app.Activity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coderbdk.asmaulhusna.R
import com.coderbdk.asmaulhusna.ui.navigation.AsmaulHusnaNavRoute

@Composable
fun UpdateRoute(
    onNavigateToHome: (routeToPopUpFrom: AsmaulHusnaNavRoute) -> Unit,
) {
    val viewModel = hiltViewModel<UpdateViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var appUpdateDialogState by remember { mutableStateOf<Int?>(null) }
    var errorDialogMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                UpdateSideEffect.NavigateToHome -> {
                    onNavigateToHome(AsmaulHusnaNavRoute.Update())
                }

                is UpdateSideEffect.ShowAppUpdateDialog -> {
                    appUpdateDialogState = effect.remoteVersion
                }

                is UpdateSideEffect.ShowError -> {
                    errorDialogMessage = effect.message
                }
            }
        }
    }
    appUpdateDialogState?.let { remoteVersion ->
        AppUpdateConfirmationDialog(
            remoteVersion = remoteVersion,
            onConfirmUpdate = {  },
            onDismiss = {
                (context as Activity).finish()
            }
        )
    }

    errorDialogMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { errorDialogMessage = null },
            title = { Text(stringResource(R.string.dialog_error_title)) },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = { errorDialogMessage = null }) {
                    Text(stringResource(R.string.dialog_error_ok_button))
                }
            }
        )
    }
    UpdateScreen(
        state = state,
        onUpdateClicked = viewModel::handleDataUpdateEvent,
        onSkipClicked = {
            onNavigateToHome(AsmaulHusnaNavRoute.Update())
        }
    )
}

@Composable
fun AppUpdateConfirmationDialog(remoteVersion: Int, onConfirmUpdate: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_update_title, remoteVersion)) },
        text = { Text(stringResource(R.string.dialog_update_message)) },
        confirmButton = {
            Button(onClick = onConfirmUpdate) {
                Text(stringResource(R.string.dialog_update_confirm_button))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.dialog_update_dismiss_button))
            }
        }
    )
}