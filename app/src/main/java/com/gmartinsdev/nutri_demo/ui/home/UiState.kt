package com.gmartinsdev.nutri_demo.ui.home

import com.gmartinsdev.nutri_demo.data.model.Food

/**
 *  state class representing the current/latest UI state
 */
sealed class UiState {
    data object Loading : UiState()
    data class Loaded(val data: List<Food>) : UiState()
    data class Error(val errorMessage: String) : UiState()
}