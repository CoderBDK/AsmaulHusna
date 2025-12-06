package com.coderbdk.asmaulhusna.ui.update

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coderbdk.asmaulhusna.R
import com.coderbdk.asmaulhusna.ui.theme.AsmaulHusnaTheme

@Composable
fun UpdateScreen(
    state: UpdateUiState,
    onUpdateClicked: () -> Unit,
    onSkipClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.isUpdateInProgress) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.update_status_downloading), style = MaterialTheme.typography.titleMedium)
            }
        } else if (state.isUpdateAvailable || state.isDataUpdateMandatory) {
            UpdateOptionsContent(
                isMandatory = state.isDataUpdateMandatory,
                onUpdateClicked = onUpdateClicked,
                onSkipClicked = onSkipClicked
            )
        } else {
            Text(stringResource(R.string.update_status_checking), style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun UpdateOptionsContent(
    isMandatory: Boolean,
    onUpdateClicked: () -> Unit,
    onSkipClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.update_available_message),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onUpdateClicked,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(stringResource(R.string.update_button_text))
        }

        if (!isMandatory) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onSkipClicked,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(stringResource(R.string.update_skip_button_text))
            }
        } else {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(R.string.update_mandatory_required),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpdatePreview() {
    AsmaulHusnaTheme {
        UpdateScreen(state = UpdateUiState(), onUpdateClicked = {}) { }
    }
}