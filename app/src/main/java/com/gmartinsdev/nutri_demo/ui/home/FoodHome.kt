package com.gmartinsdev.nutri_demo.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmartinsdev.nutri_demo.data.model.Food
import com.gmartinsdev.nutri_demo.data.model.Photo
import com.gmartinsdev.nutri_demo.ui.components.ErrorMessageScreen
import com.gmartinsdev.nutri_demo.ui.components.SearchViewComponent
import com.gmartinsdev.nutri_demo.ui.theme.MainTheme

/**
 *  composable class responsible for displaying a list of foods
 */
@Composable
fun FoodHomeScreen(
    vm: FoodViewModel = hiltViewModel(),
    navToInfoScreen: (Int) -> Unit
) {
    val state by vm.state.collectAsState()
    val savedSearchQuery = rememberSaveable {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        vm.fetchData(savedSearchQuery.value.ifEmpty { "" })
    }

    Scaffold(
        topBar = { }
    ) { innerPadding ->
        FoodHome(
            modifier = Modifier.padding(innerPadding),
            state = state,
            onClick = {
                navToInfoScreen.invoke(it.id)
            },
            onSearch = {
                vm.fetchData(it)
                savedSearchQuery.value = it
            },
            onRefresh = {
                vm.fetchData("")
            }
        )
    }
}

@Composable
fun FoodHome(
    modifier: Modifier = Modifier,
    state: UiState,
    onClick: (Food) -> Unit,
    onSearch: (String) -> Unit,
    onRefresh: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchViewComponent(
            modifier = modifier,
            onSearch = { onSearch.invoke(it) }
        )
        when (state) {
            is UiState.Loading -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Loaded -> {
                if (state.data.isNotEmpty()) {
                    LazyColumn(
                        modifier = modifier
                            .widthIn(0.dp, 800.dp)
                            .padding(
                                top = 10.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 10.dp
                            ),
                        verticalArrangement = Arrangement.spacedBy(16.dp),

                        ) {
                        items(state.data) { food ->
                            FoodItemScreen(
                                foodItem = food,
                                onClick = { onClick.invoke(food) }
                            )
                        }
                    }
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorMessageScreen(
                        message = state.errorMessage,
                        buttonEnabled = true
                    ) {
                        onRefresh.invoke()
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun FoodHomePreview() {
    MainTheme {
        FoodHome(
            state = UiState.Loaded(
                listOf(
                    Food(
                        111,
                        "aaa",
                        1.0,
                        "large",
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        Photo("www.aaa.com/photo1")
                    ),
                    Food(
                        222,
                        "bbb",
                        2.0,
                        "large",
                        100.00,
                        200.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        Photo("www.aaa.com/photo2")
                    ),
                    Food(
                        333,
                        "ccc",
                        3.0,
                        "large",
                        100.00,
                        300.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        Photo("www.aaa.com/photo3")
                    ),
                    Food(
                        444,
                        "ddd",
                        4.0,
                        "large",
                        100.00,
                        400.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        Photo("www.aaa.com/photo4")
                    ),
                    Food(
                        555,
                        "eee",
                        5.0,
                        "large",
                        100.00,
                        500.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        100.00,
                        Photo("www.aaa.com/photo5")
                    ),
                )
            ),
            onSearch = {},
            onRefresh = {},
            onClick = {}
        )
    }
}