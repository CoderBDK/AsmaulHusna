package com.coderbdk.asmaulhusna.ui.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coderbdk.asmaulhusna.R
import com.coderbdk.asmaulhusna.data.local.db.entity.LanguageEntity
import com.coderbdk.asmaulhusna.ui.theme.AsmaulHusnaTheme

@Composable
fun OnboardingScreen(uiState: OnboardingUiState, onEvent: (OnboardingUiEvent) -> Unit) {

    when (uiState.currentStep) {
        OnboardingStep.Welcome -> WelcomeContent(
            onNext = { onEvent(OnboardingUiEvent.NextButtonClicked) }
        )

        OnboardingStep.LanguageSelection ->
            LanguageSelectionStep(
                languages = uiState.languageList,
                onLanguageSelected = { langId ->
                    onEvent(OnboardingUiEvent.LanguageSelected(langId))
                }
            )

        OnboardingStep.FeatureHighlights ->
            FeatureHighlightsContent(
                isDataLoading = uiState.isDataLoading,
                onNext = { onEvent(OnboardingUiEvent.NextButtonClicked) }
            )

        OnboardingStep.FinalGetStarted ->
            FinalGetStartedStep(
                isDataLoading = uiState.isDataLoading,
                onGetStarted = { onEvent(OnboardingUiEvent.GetStartedClicked) }
            )
    }
}

@Composable
fun WelcomeContent(onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.allah),
            fontSize = 148.sp,
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = colorScheme.primary,
                shadow = Shadow(
                    color = colorScheme.outlineVariant,
                    blurRadius = 10f,
                    offset = Offset(2f, 1f)
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(50.dp)
                )
        )

        Spacer(Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.assalamu_aleykum),
            contentDescription = null,
            colorFilter = ColorFilter.tint(colorScheme.onSurface),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        )
        Spacer(Modifier.weight(1f))
        FilledTonalIconButton(
            onClick = onNext, Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_keyboard_double_arrow_right_24),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun LanguageSelectionStep(languages: List<LanguageEntity>, onLanguageSelected: (Int) -> Unit) {
    var selectedLanguageId by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.outline_language_24),
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(languages) { lang ->
                val isSelected = lang.languageId == selectedLanguageId
                ElevatedCard(
                    onClick = {
                        selectedLanguageId = lang.languageId
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                ) {
                    ListItem(
                        headlineContent = {
                            Text(lang.languageName)
                        },
                        trailingContent = {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    selectedLanguageId = lang.languageId
                                },
                            )
                        }
                    )
                }

            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        FilledTonalIconButton(
            onClick = {
                selectedLanguageId?.let(onLanguageSelected)
            },
            enabled = selectedLanguageId != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_keyboard_double_arrow_right_24),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun FeatureHighlightsContent(isDataLoading: Boolean, onNext: () -> Unit) {
    data class Feature(
        @field:StringRes
        val title: Int,
        @field:StringRes
        val summary: Int,
        @field:DrawableRes
        val icon: Int
    )

    val featureHighlights = listOf(
        Feature(
            title = R.string.onboarding_app_fh1_title,
            summary = R.string.onboarding_app_fh1_summary,
            icon = R.drawable.outline_blur_on_24
        ),
        Feature(
            title = R.string.onboarding_app_fh2_title,
            summary = R.string.onboarding_app_fh2_summary,
            icon = R.drawable.outline_volume_up_24
        ),
        Feature(
            title = R.string.onboarding_app_fh3_title,
            summary = R.string.onboarding_app_fh3_summary,
            icon = R.drawable.outline_menu_book_24
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.onboarding_app_highlights),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(24.dp))

        Column(Modifier
            .fillMaxWidth()
            .weight(1f), verticalArrangement = Arrangement.Center) {
            repeat(featureHighlights.size) {
                val item = featureHighlights[it]
                ListItem(
                    leadingContent = {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = null,
                            tint = colorScheme.onPrimary,
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = colorScheme.primary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp)
                        )
                    },
                    headlineContent = {
                        Text(text = stringResource(item.title))
                    },
                    supportingContent = {
                        Text(text = stringResource(item.summary))
                    }
                )
                Spacer(Modifier.height(24.dp))
            }

        }


        Spacer(modifier = Modifier.height(24.dp))

        if (isDataLoading) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        } else {
            FilledTonalIconButton(
                onClick = onNext, Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_keyboard_double_arrow_right_24),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Composable
fun FinalGetStartedStep(isDataLoading: Boolean, onGetStarted: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.onboarding_app_final_ready_title),
            style = MaterialTheme.typography.headlineLarge,
            color = colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.onboarding_app_final_ready_subtitle),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))

        FilledTonalButton(
            onClick = onGetStarted,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            enabled = !isDataLoading
        ) {
            Text(
                text = if (isDataLoading)
                    stringResource(R.string.onboarding_app_final_button_loading)
                else
                    stringResource(R.string.onboarding_app_final_button_start)
            )
        }

        if (isDataLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    AsmaulHusnaTheme {
        OnboardingScreen(uiState = OnboardingUiState(currentStep = OnboardingStep.FinalGetStarted)) { }
    }
}
