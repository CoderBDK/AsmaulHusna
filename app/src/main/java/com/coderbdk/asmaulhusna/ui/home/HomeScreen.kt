package com.coderbdk.asmaulhusna.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coderbdk.asmaulhusna.R
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaFull
import com.coderbdk.asmaulhusna.ui.theme.AsmaulHusnaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
) {
    val searchBarState = rememberSearchBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val searchBarScrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = stringResource(R.string.app_name))
                    },
                    scrollBehavior = scrollBehavior
                )
                TopSearchBar(
                    state = searchBarState,
                    scrollBehavior = searchBarScrollBehavior,
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = uiState.query,
                            onQueryChange = {
                                onEvent(HomeUiEvent.QueryChange(it))
                            },
                            onSearch = {
                                keyboardController?.hide()
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.outline_search_24),
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                if (uiState.query.isNotBlank()) {
                                    IconButton(
                                        onClick = {
                                            onEvent(HomeUiEvent.QueryChange(""))
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.baseline_clear_24),
                                            contentDescription = null
                                        )
                                    }
                                }
                            },
                            placeholder = { Text(stringResource(R.string.home_search_placeholder)) },
                            expanded = false,
                            onExpandedChange = {},
                        )
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        onEvent(HomeUiEvent.NavigateToFavorite)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.outline_favorite_24),
                            contentDescription = null
                        )
                    },
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {

                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_home_24),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        onEvent(HomeUiEvent.NavigateToSettings)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.outline_settings_24),
                            contentDescription = null
                        )
                    }
                )
            }
        },
        modifier = Modifier
            .nestedScroll(
                connection = scrollBehavior.nestedScrollConnection
            )
            .nestedScroll(
                connection = searchBarScrollBehavior.nestedScrollConnection
            )
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                LinearProgressIndicator()
            } else if (uiState.error != null) {
                Text("E:${uiState.error}")
            } else {
                LazyColumn {
                    items(uiState.asmaulHusnaList) { item ->
                        AsmaulHusnaItem(
                            uiState = uiState,
                            data = item,
                            onFavoriteToggled = { isFav ->
                                onEvent(HomeUiEvent.SetFavoriteStatus(item.number, isFav))
                            },
                            onItemClick = {
                                onEvent(HomeUiEvent.NavigateToDetails(it))
                            },
                            onEvent = onEvent
                        )
                    }
                }
            }
        }
    }

}


@Composable
fun AsmaulHusnaItem(
    uiState: HomeUiState,
    data: AsmaulHusnaFull,
    onFavoriteToggled: (Boolean) -> Unit,
    onItemClick: (Int) -> Unit,
    onEvent: (HomeUiEvent) -> Unit
) {
    val favoriteTint by animateColorAsState(
        targetValue = if (data.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
        label = "Favorite Color Animation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                if (uiState.playerState?.isPlaying == true) {
                    onEvent(HomeUiEvent.PlayPause)
                }
                onItemClick(data.number)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = data.number.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = data.arabicName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = data.transliteration,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = data.meaning,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column {
                val isCurrentItem = uiState.playerState?.asmaulHusna?.number == data.number

                IconButton(
                    onClick = {
                        onEvent(HomeUiEvent.ToggleAudio(data))
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(if (isCurrentItem && uiState.playerState.isPlaying) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24),
                        contentDescription = "Play Audio",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(
                    onClick = {
                        onFavoriteToggled(!data.isFavorite)
                    },
                    modifier = Modifier
                        .size(48.dp)

                ) {
                    Icon(
                        imageVector = if (data.isFavorite) ImageVector.vectorResource(R.drawable.baseline_favorite_24)
                        else ImageVector.vectorResource(R.drawable.outline_favorite_24),
                        contentDescription = "Favorite Toggle",
                        tint = favoriteTint
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    AsmaulHusnaTheme {
        HomeScreen(
            uiState = HomeUiState(
                asmaulHusnaList = listOf(
                    AsmaulHusnaFull(
                        number = 1,
                        arabicName = "ٱلرَّحْمَـٰنُ",
                        audioFile = "ar_rahman.mp3",
                        isFavorite = false,
                        transliteration = "Ar-Rahmān",
                        meaning = "The Most Merciful",
                        description = "Allah is the One whose mercy is abundant, encompassing all creation.",
                        languageName = "English"
                    ),
                    AsmaulHusnaFull(
                        number = 2,
                        arabicName = "ٱلرَّحِيمُ",
                        audioFile = "ar_rahim.mp3",
                        isFavorite = false,
                        transliteration = "Ar-Rahīm",
                        meaning = "The Most Compassionate",
                        description = "Allah is the One who shows continuous mercy and compassion to the believers.",
                        languageName = "English"
                    ),
                    AsmaulHusnaFull(
                        number = 3,
                        arabicName = "ٱلْمَلِكُ",
                        audioFile = "al_malik.mp3",
                        isFavorite = false,
                        transliteration = "Al-Malik",
                        meaning = "The Sovereign",
                        description = "Allah is the absolute ruler and owner of all creation, free from all needs.",
                        languageName = "English"
                    )
                )

            )
        ) { }
    }
}