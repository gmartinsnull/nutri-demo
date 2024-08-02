package com.gmartinsdev.nutri_demo.ui.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmartinsdev.nutri_demo.data.model.Food
import com.gmartinsdev.nutri_demo.data.model.FoodWithIngredients
import com.gmartinsdev.nutri_demo.data.model.Photo
import com.gmartinsdev.nutri_demo.data.model.SubRecipe
import com.gmartinsdev.nutri_demo.ui.components.ErrorMessageScreen
import com.gmartinsdev.nutri_demo.ui.theme.MainTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

/**
 * composable responsible for displaying nutritional info and recipe ingredients of selected food
 */
@Composable
fun FoodInfoScreen(
    foodId: Int,
    vm: FoodInfoViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    LaunchedEffect(key1 = foodId) {
        vm.getFoodWithRecipe(foodId)
    }

    val state by vm.state.collectAsState()
    FoodInfo(
        state = state,
        onUpdateIngredient = {
            vm.updateIngredients(it)
        },
        navigateBack = {
            navigateBack.invoke()
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodInfo(
    modifier: Modifier = Modifier,
    state: UiInfoState,
    onUpdateIngredient: (List<SubRecipe>) -> Unit,
    navigateBack: () -> Unit
) {
    when (state) {
        is UiInfoState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiInfoState.Loaded -> {
            val food = remember { mutableStateOf(state.data.food) }
            val ingredients = remember {
                mutableStateListOf<SubRecipe>().apply {
                    state.data.ingredients.forEach {
                        add(it)
                    }
                }
            }
            BottomSheetScaffold(
                scaffoldState = rememberBottomSheetScaffoldState(
                    SheetState(
                        skipPartiallyExpanded = false,
                        initialValue = SheetValue.Expanded,
                        density = LocalDensity.current,
                        skipHiddenState = true
                    )
                ),
                sheetPeekHeight = 50.dp,
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(food.value.name)
                        },
                        navigationIcon = {
                            IconButton(onClick = navigateBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Localized description"
                                )
                            }
                        },
                    )
                },
                sheetContent = {
                    // TODO: implement user gps permission request to get user current location
                    val vancouver = LatLng(49.283832198, -123.119332856)
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(vancouver, 13f)
                    }
                    GoogleMap(
                        modifier = modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.6f),
                        cameraPositionState = cameraPositionState
                    )
                },
                content = {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.tertiary)
                    ) {
                        FoodInfoHeaderScreen(food = food)
                        FoodInfoBodyScreen(food = food)
                        FoodInfoIngredientsScreen(ingredients = ingredients) { newIngredientValues ->
                            onUpdateIngredient.invoke(newIngredientValues)
                        }
                    }
                }
            )
        }

        is UiInfoState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ErrorMessageScreen(
                    message = state.errorMessage,
                    buttonEnabled = true
                ) {
                }
            }
        }
    }
}

@Preview
@Composable
fun FoodInfoPreview() {
    MainTheme {
        FoodInfo(
            state = UiInfoState.Loaded(
                FoodWithIngredients(
                    food = Food(
                        111,
                        "aaa",
                        1.0,
                        "small",
                        100.00,
                        100.00,
                        20.00,
                        20.00,
                        40.00,
                        100.00,
                        600.00,
                        300.00,
                        300.00,
                        10.00,
                        150.00,
                        120.00,
                        Photo("www.aaa.com/photo1")
                    ),
                    ingredients = listOf(
                        SubRecipe(
                            1,
                            111,
                            1.0,
                            "egg",
                            100.0,
                            1.0,
                            "g"
                        ),
                        SubRecipe(
                            2,
                            222,
                            1.0,
                            "cheese",
                            200.0,
                            2.0,
                            "g"
                        ),
                        SubRecipe(
                            3,
                            333,
                            1.0,
                            "flour",
                            300.0,
                            3.0,
                            "g"
                        )
                    )
                )
            ),
            onUpdateIngredient = {

            },
            navigateBack = {

            }
        )
    }
}