package com.coderbdk.asmaulhusna.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coderbdk.asmaulhusna.R
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaFull
import com.coderbdk.asmaulhusna.ui.components.BackNavigationButton
import com.coderbdk.asmaulhusna.ui.theme.AsmaulHusnaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(uiState: DetailsUiState, onEvent: (DetailsUiEvent) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                },
                navigationIcon = {
                    BackNavigationButton {
                        onEvent(DetailsUiEvent.NavigateBack)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primaryContainer,
                    navigationIconContentColor = colorScheme.primary
                )
            )
        },
    ) {
        Column(Modifier.padding(it)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .background(
                        color = colorScheme.primaryContainer,
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    ).padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.asmaulHusna.arabicName,
                    color = colorScheme.primary,
                    fontSize = 80.sp,
                    textAlign = TextAlign.Center,
                )

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    FilledIconButton(
                        onClick = {
                            onEvent(DetailsUiEvent.OnPreviousClicked)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.rounded_arrow_circle_left_24),
                            contentDescription = null
                        )
                    }


                    FilledIconButton(
                        onClick = {
                            onEvent(DetailsUiEvent.OnNextClicked)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_arrow_circle_right_24),
                            contentDescription = null
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .background(
                        color = colorScheme.surface
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = uiState.asmaulHusna.transliteration,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = uiState.asmaulHusna.meaning,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    stringResource(R.string.details_description_heading),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = uiState.asmaulHusna.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(24.dp).weight(1f))
                FilledTonalButton(
                    onClick = {
                        onEvent(DetailsUiEvent.PlayFullAudio)
                    }
                ) {
                    Text("Play Full Audio")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsPreview() {
    AsmaulHusnaTheme {
        DetailsScreen(
            uiState = DetailsUiState(
                asmaulHusna = AsmaulHusnaFull(
                    number = 1,
                    arabicName = "ٱلرَّحْمَـٰنُ",
                    audioFile = "ar_rahman.mp3",
                    isFavorite = false,
                    transliteration = "Ar-Rahmān",
                    meaning = "The Most Merciful",
                    description = "Allah is the One whose mercy is abundant, encompassing all creation.",
                    languageName = "English"
                ),
            ),
            onEvent = {}
        )
    }
}