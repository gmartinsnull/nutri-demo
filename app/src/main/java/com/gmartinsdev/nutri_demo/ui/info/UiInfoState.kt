package com.gmartinsdev.nutri_demo.ui.info

import com.gmartinsdev.nutri_demo.data.model.FoodWithIngredients

/**
 *  state class representing the current/latest UI state in food info screen
 */
sealed class UiInfoState{
    data object Loading : UiInfoState()
    data class Loaded(val data: FoodWithIngredients) : UiInfoState()
    data class Error(val errorMessage: String) : UiInfoState()
}
