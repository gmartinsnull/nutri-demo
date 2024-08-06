package com.gmartinsdev.nutri_demo.ui.info

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmartinsdev.nutri_demo.data.model.Food
import com.gmartinsdev.nutri_demo.data.model.FoodWithIngredients
import com.gmartinsdev.nutri_demo.data.model.Photo
import com.gmartinsdev.nutri_demo.data.model.SubRecipe
import com.gmartinsdev.nutri_demo.data.remote.google_maps.Geolocation
import com.gmartinsdev.nutri_demo.data.remote.google_maps.NearbySearchApiResponse
import com.gmartinsdev.nutri_demo.data.remote.google_maps.NearbySearchGeometry
import com.gmartinsdev.nutri_demo.data.remote.google_maps.NearbySearchResult
import com.gmartinsdev.nutri_demo.ui.components.ErrorMessageScreen
import com.gmartinsdev.nutri_demo.ui.theme.MainTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

/**
 * composable responsible for displaying nutritional info and recipe ingredients of selected food
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodInfoScreen(
    foodId: Int,
    vm: FoodInfoViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = foodId) {
        vm.getFoodWithRecipe(foodId)
    }

    val state by vm.state.collectAsState()
    val stateMap by vm.stateMap.collectAsState()
    val density = LocalDensity.current
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = remember {
            SheetState(
                skipPartiallyExpanded = false,
                initialValue = SheetValue.Expanded,
                density = density,
                skipHiddenState = true
            )
        }
    )
    FoodInfo(
        state = state,
        stateMap = stateMap,
        bottomSheetState = bottomSheetState,
        context = context,
        onIngredientsUpdate = {
            vm.updateNutrients(it)
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
    stateMap: UiMapState,
    bottomSheetState: BottomSheetScaffoldState,
    context: Context,
    onIngredientsUpdate: (List<SubRecipe>) -> Unit,
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
            val ingredients = remember {
                mutableStateListOf<SubRecipe>().apply {
                    state.data.ingredients.forEach {
                        add(it)
                    }
                }
            }
            BottomSheetScaffold(
                scaffoldState = bottomSheetState,
                sheetPeekHeight = 50.dp,
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(state.data.food.name)
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
                    when (stateMap) {
                        is UiMapState.Loading -> {
                            Box(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.6f),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is UiMapState.Loaded -> {
                            // TODO: [IMPROVEMENT] implement user gps permission request to get
                            //  user's current location. Remove hardcoded latlng
                            val vancouver = LatLng(49.283832198, -123.119332856)
                            val cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(vancouver, 13f)
                            }
                            GoogleMap(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.6f),
                                cameraPositionState = cameraPositionState
                            ) {
                                stateMap.data.results.forEach { nearby ->
                                    val (lat, lng) = nearby.geometry.location
                                    Marker(
                                        state = MarkerState(position = LatLng(lat, lng)),
                                        title = nearby.name
                                    )
                                }
                            }
                        }

                        is UiMapState.Error -> {
                            Toast.makeText(
                                context,
                                "Error while fetching nearby places",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                },
                content = {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                    ) {
                        FoodInfoHeaderScreen(food = state.data.food)
                        FoodInfoBodyScreen(food = state.data.food)
                        if (!ingredients.isEmpty()) {
                            FoodInfoIngredientsScreen(ingredients = ingredients) { newIngredientValues ->
                                onIngredientsUpdate.invoke(newIngredientValues)
                            }
                        } else {
                            ErrorMessageScreen(
                                message = "Ingredients not found",
                                buttonEnabled = false,
                                onTryAgainClick = {

                                }
                            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FoodInfoPreview() {
    MainTheme {
        FoodInfo(
            stateMap = UiMapState.Loaded(
                NearbySearchApiResponse(
                    listOf(
                        NearbySearchResult(
                            NearbySearchGeometry(Geolocation(123.0, 234.0)),
                            "uncle bob pizza"
                        ),
                        NearbySearchResult(
                            NearbySearchGeometry(Geolocation(123.0, 234.0)),
                            "two and a half twins pizza"
                        ),
                        NearbySearchResult(
                            NearbySearchGeometry(Geolocation(123.0, 234.0)),
                            "itchy elbow pizza"
                        )
                    )
                )
            ),
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
            bottomSheetState = rememberBottomSheetScaffoldState(),
            context = LocalContext.current,
            onIngredientsUpdate = {

            },
            navigateBack = {

            }
        )
    }
}