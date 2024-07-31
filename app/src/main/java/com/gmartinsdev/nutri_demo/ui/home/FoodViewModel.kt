package com.gmartinsdev.nutri_demo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmartinsdev.nutri_demo.data.remote.Status
import com.gmartinsdev.nutri_demo.domain.GetFoodByName
import com.gmartinsdev.nutri_demo.domain.GetFoods
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *  viewmodel class responsible for handling data from usercase class to view layer
 */
@HiltViewModel
class FoodViewModel @Inject constructor(
    private val getFoods: GetFoods,
    private val getFoodByName: GetFoodByName
) : ViewModel() {

    init {
        getAllFoods()
    }

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    /**
     * fetching data from repository and parsing it accordingly for view layer consumption
     */
    fun fetchData(title: String) {
        _state.value = UiState.Loading
        if (title.isEmpty()) {
            getAllFoods()
        } else {
            getFoodsByTitle(title.trim())
        }
    }

    /**
     * get all foods stored locally
     */
    private fun getAllFoods() {
        viewModelScope.launch {
            getFoods().collect { result ->
                _state.value = when (result.status) {
                    Status.SUCCESS -> UiState.Loaded(result.data ?: emptyList())
                    Status.ERROR -> UiState.Error(
                        result.error?.message ?: "error while retrieving all foods"
                    )
                }
            }
        }
    }

    /**
     * get all foods by title
     */
    private fun getFoodsByTitle(title: String) {
        viewModelScope.launch {
            getFoodByName(title).collect { result ->
                _state.value = when (result.status) {
                    Status.SUCCESS -> UiState.Loaded(result.data ?: emptyList())
                    Status.ERROR -> UiState.Error(
                        result.error?.message ?: "error while retrieving food by title: $title"
                    )
                }
            }
        }
    }
}