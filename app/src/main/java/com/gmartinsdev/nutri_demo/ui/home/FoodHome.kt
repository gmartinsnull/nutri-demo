package com.gmartinsdev.nutri_demo.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.gmartinsdev.nutri_demo.data.model.Food
import com.gmartinsdev.nutri_demo.data.model.Photo
import com.gmartinsdev.nutri_demo.ui.components.ErrorMessageScreen
import com.gmartinsdev.nutri_demo.ui.components.SearchViewComponent
import com.gmartinsdev.nutri_demo.ui.theme.MainTheme

/**
 *  composable class responsible for displaying a list of foods
 */
@Composable
fun FoodHomeScreen() {
    val context = LocalContext.current
    val vm: FoodViewModel = viewModel()
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = { }
    ) { innerPadding ->
        FoodHome(
            modifier = Modifier.padding(innerPadding),
            state = state,
            onClick = {
                Toast.makeText(context, "${it.name} clicked", Toast.LENGTH_LONG).show()
            },
            onSearch = {
                vm.fetchData(it)
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
                    LazyVerticalGrid(
                        modifier = modifier
                            .widthIn(0.dp, 800.dp)
                            .padding(
                                top = 10.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 10.dp
                            ),
                        columns = GridCells.Adaptive(120.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)

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
                        "1",
                        "aaa",
                        100,
                        Photo("www.aaa.com/photo1")
                    ),
                    Food(
                        "2",
                        "bbb",
                        200,
                        Photo("www.aaa.com/photo2")
                    ),
                    Food(
                        "3",
                        "ccc",
                        300,
                        Photo("www.aaa.com/photo3")
                    ),
                    Food(
                        "4",
                        "ddd",
                        400,
                        Photo("www.aaa.com/photo4")
                    ),
                )
            ),
            onSearch = {},
            onRefresh = {},
            onClick = {}
        )
    }
}