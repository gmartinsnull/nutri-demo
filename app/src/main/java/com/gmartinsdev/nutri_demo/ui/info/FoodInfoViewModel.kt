package com.gmartinsdev.nutri_demo.ui.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmartinsdev.nutri_demo.data.model.SubRecipe
import com.gmartinsdev.nutri_demo.data.remote.Status
import com.gmartinsdev.nutri_demo.domain.GetFoodWithIngredients
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * viewmodel class responsible for handling data from food info usecase classes to view layer
 */
@HiltViewModel
class FoodInfoViewModel @Inject constructor(
    private val getFoodWithIngredients: GetFoodWithIngredients
) : ViewModel() {

    private val _state = MutableStateFlow<UiInfoState>(UiInfoState.Loading)
    val state: StateFlow<UiInfoState> = _state.asStateFlow()

    fun getFoodWithRecipe(foodId: Int) {
        viewModelScope.launch {
            getFoodWithIngredients(foodId).collect { result ->
                _state.value = when (result.status) {
                    Status.SUCCESS -> {
                        if (result.data != null)
                            UiInfoState.Loaded(result.data)
                        else
                            UiInfoState.Error(
                                "null data while retrieving food with recipe: $foodId"
                            )
                    }

                    Status.ERROR -> UiInfoState.Error(
                        result.error?.message ?: "error while retrieving food with recipe: $foodId"
                    )
                }
            }
        }
    }

    /**
     * recalculates nutrients based on changed in ingredient values
     */
    fun updateIngredients(newIngredients: List<SubRecipe>) {
        fetchIngredientsById()
        // TODO: implement nutrients recalculation and update nutrition values
    }

    /**
     * fetches ingredients as food data from data source
     */
    private fun fetchIngredientsById() {
        // TODO: implement thing so it fetches ingredients as food items
    }
}