package com.coderbdk.asmaulhusna.ui.favorite

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coderbdk.asmaulhusna.R
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaEntity
import com.coderbdk.asmaulhusna.ui.components.BackNavigationButton
import com.coderbdk.asmaulhusna.ui.theme.AsmaulHusnaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    uiState: FavoriteUiState,
    onEvent: (FavoriteUiEvent) -> Unit
) {
    val searchBarState = rememberSearchBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val searchBarScrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(text = stringResource(R.string.favorite_title))
                    },
                    navigationIcon = {
                        BackNavigationButton {
                            onEvent(FavoriteUiEvent.NavigateBack)
                        }
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
                                onEvent(FavoriteUiEvent.QueryChange(it))
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
                                            onEvent(FavoriteUiEvent.QueryChange(""))
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
                        AsmaulHusnaFavoriteItem(
                            data = item,
                            onFavoriteToggled = { isFav ->
                                onEvent(FavoriteUiEvent.SetFavoriteStatus(item.number, isFav))
                            },
                            onItemClick = {
                                onEvent(FavoriteUiEvent.NavigateToDetails(it))
                            }
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun AsmaulHusnaFavoriteItem(
    data: AsmaulHusnaEntity,
    onFavoriteToggled: (Boolean) -> Unit,
    onItemClick: (Int) -> Unit,
) {
    val favoriteTint by animateColorAsState(
        targetValue = if (data.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
        label = "Favorite Color Animation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onItemClick(data.number) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        ListItem(
            leadingContent = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = data.number.toString(),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            },
            headlineContent = {
                Text(
                    text = data.arabicName
                )

            },
            trailingContent = {
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
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritePreview() {
    AsmaulHusnaTheme {
        FavoriteScreen(
            uiState = FavoriteUiState(
                asmaulHusnaList = listOf(
                    AsmaulHusnaEntity(
                        number = 1,
                        arabicName = "ٱلرَّحْمَـٰنُ",
                        audioFile = "ar_rahman.mp3",
                        isFavorite = false,
                    ),
                    AsmaulHusnaEntity(
                        number = 2,
                        arabicName = "ٱلرَّحِيمُ",
                        audioFile = "ar_rahim.mp3",
                        isFavorite = false,
                    ),
                    AsmaulHusnaEntity(
                        number = 3,
                        arabicName = "ٱلْمَلِكُ",
                        audioFile = "al_malik.mp3",
                        isFavorite = false,
                    )
                )

            )
        ) { }
    }
}