package com.gmartinsdev.nutri_demo.ui.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmartinsdev.nutri_demo.data.model.IngredientFood
import com.gmartinsdev.nutri_demo.data.model.SubRecipe
import com.gmartinsdev.nutri_demo.data.remote.Status
import com.gmartinsdev.nutri_demo.domain.FetchIngredients
import com.gmartinsdev.nutri_demo.domain.GetFoodWithIngredients
import com.gmartinsdev.nutri_demo.domain.SearchNearbyPlaces
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * viewmodel class responsible for handling data from food info usecase classes to view layer
 */
@HiltViewModel
class FoodInfoViewModel @Inject constructor(
    private val getFoodWithIngredients: GetFoodWithIngredients,
    private val searchNearbyPlaces: SearchNearbyPlaces,
    private val fetchIngredients: FetchIngredients
) : ViewModel() {

    private val _state = MutableStateFlow<UiInfoState>(UiInfoState.Loading)
    val state: StateFlow<UiInfoState> = _state.asStateFlow()
    private val _stateMap = MutableStateFlow<UiMapState>(UiMapState.Loading)
    val stateMap: StateFlow<UiMapState> = _stateMap.asStateFlow()

    private val ingredients: MutableList<IngredientFood> = mutableListOf()

    /**
     * retrieves food and its recipe ingredients by id
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getFoodWithRecipe(foodId: Int) {
        viewModelScope.launch {
            getFoodWithIngredients(foodId).flatMapConcat { result ->
                _state.value = when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null) {
                            getNearbyPlaces(result.data.food.name)
                            UiInfoState.Loaded(result.data)
                        } else {
                            UiInfoState.Error(
                                "null data while retrieving food with recipe: $foodId"
                            )
                        }
                    }

                    Status.ERROR -> UiInfoState.Error(
                        result.error?.message ?: "error while retrieving food with recipe: $foodId"
                    )
                }

                fetchIngredients((state.value as UiInfoState.Loaded).data.ingredients)
            }.collect { ingredientsResult ->
                when (ingredientsResult.status) {
                    Status.SUCCESS -> {
                        ingredients.addAll(ingredientsResult.data ?: emptyList())
                    }

                    Status.ERROR -> UiInfoState.Error(
                        ingredientsResult.error?.message
                            ?: "error while retrieving ingredients as food: ${ingredients.toList()}"
                    )
                }
            }
        }
    }

    /**
     * retrieve nearby places by keyword for google maps markers
     */
    private fun getNearbyPlaces(keyword: String) {
        viewModelScope.launch {
            searchNearbyPlaces(keyword).collect { result ->
                _stateMap.value = when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null) {
                            UiMapState.Loaded(result.data)
                        } else {
                            UiMapState.Error(
                                "null data while retrieving nearby search result with keyword: $keyword"
                            )
                        }
                    }

                    Status.ERROR -> UiMapState.Error(
                        result.error?.message
                            ?: "error while retrieving nearby search result with keyword: $keyword"
                    )
                }
            }
        }
    }

    /**
     * recalculates nutrients based on new ingredient values
     */
    fun updateNutrients(newIngredients: List<SubRecipe>) {
        // TODO: implement nutrients recalculation and update nutrition values
        // EQUATION
        // total cal / total serving weight = x (cal per 1g)
        // new ingredient value * x = y (cal per new ingredient value in g)
        // total cal - (ingredient cal * serving qty) = new total cal
        // new total cal + y = z (new total cal after ingredient value update)
        // repeat for every single macro/micro nutrient
    }
}