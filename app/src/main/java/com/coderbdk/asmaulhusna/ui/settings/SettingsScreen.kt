package com.coderbdk.asmaulhusna.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coderbdk.asmaulhusna.R
import com.coderbdk.asmaulhusna.ui.components.BackNavigationButton
import com.coderbdk.asmaulhusna.ui.theme.AsmaulHusnaTheme


@Composable
fun SettingsListItem(
    headline: String,
    summary: String,
    leadingIcon: ImageVector,
    onClick: () -> Unit,
    trailingContent: @Composable () -> Unit
) {
    ListItem(
        headlineContent = { Text(text = headline) },
        supportingContent = { Text(text = summary, style = MaterialTheme.typography.bodyMedium) },
        leadingContent = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null
            )
        },
        trailingContent = trailingContent,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit
) {
    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.settings_title))
                },
                navigationIcon = {
                    BackNavigationButton {
                        onEvent(SettingsUiEvent.NavigateBack)
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp)
        ) {

            val darkModeSummary = if (uiState.isDarkTheme) {
                stringResource(R.string.settings_dark_mode_summary_on)
            } else {
                stringResource(R.string.settings_dark_mode_summary_off)
            }

            SettingsListItem(
                headline = stringResource(R.string.settings_dark_mode_title),
                summary = darkModeSummary,
                leadingIcon = if (uiState.isDarkTheme) ImageVector.vectorResource(R.drawable.outline_dark_mode_24) else ImageVector.vectorResource(
                    R.drawable.outline_light_mode_24
                ),
                onClick = { onEvent(SettingsUiEvent.ToggleTheme(!uiState.isDarkTheme)) },
                trailingContent = {
                    Switch(
                        checked = uiState.isDarkTheme,
                        onCheckedChange = { isChecked ->
                            onEvent(SettingsUiEvent.ToggleTheme(isChecked))
                        }
                    )
                }
            )

            HorizontalDivider()

            LanguageSelector(
                uiState = uiState,
                onEvent = onEvent
            )

            HorizontalDivider()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelector(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedLanguageName = uiState.languageList.find {
        it.languageId == uiState.selectedLanguageId
    }?.languageName ?: stringResource(R.string.settings_language_loading)

    ListItem(
        headlineContent = { Text(text = stringResource(R.string.settings_language_title)) },
        supportingContent = {
            Text(
                text = selectedLanguageName,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingContent = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.outline_language_24),
                contentDescription = null
            )
        },
        trailingContent = {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                Row(
                    modifier = Modifier.menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedLanguageName,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    uiState.languageList.forEach { language ->
                        DropdownMenuItem(
                            text = { Text(language.languageName) },
                            onClick = {
                                onEvent(SettingsUiEvent.SetLanguage(language.languageId))
                                expanded = false
                            },
                            trailingIcon = {
                                if (language.languageId == uiState.selectedLanguageId) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.outline_check_24),
                                        contentDescription = stringResource(R.string.settings_selected)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    AsmaulHusnaTheme {
        SettingsScreen(uiState = SettingsUiState()) { }
    }
}