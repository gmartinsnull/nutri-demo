package com.gmartinsdev.nutri_demo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmartinsdev.nutri_demo.data.remote.Status
import com.gmartinsdev.nutri_demo.domain.GetFoodByTitle
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
    private val getFoodByTitle: GetFoodByTitle
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
        viewModelScope.launch {
            _state.value = UiState.Loading
            if (title.isEmpty()) {
                getAllFoods()
            } else {
                getFoodsByTitle(title)
            }
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
     * get all foods stored locally
     */
    private suspend fun getFoodsByTitle(title: String) {
        getFoodByTitle(title).collect { result ->
            _state.value = when (result.status) {
                Status.SUCCESS -> UiState.Loaded(result.data ?: emptyList())
                Status.ERROR -> UiState.Error(
                    result.error?.message ?: "error while retrieving food by title: $title"
                )
            }
        }
    }
}