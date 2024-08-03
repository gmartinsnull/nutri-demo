package com.gmartinsdev.nutri_demo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmartinsdev.nutri_demo.data.model.CommonFood
import com.gmartinsdev.nutri_demo.data.model.Food
import com.gmartinsdev.nutri_demo.data.remote.Status
import com.gmartinsdev.nutri_demo.domain.GetCommonFoodsByName
import com.gmartinsdev.nutri_demo.domain.GetFoodByName
import com.gmartinsdev.nutri_demo.domain.GetFoods
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *  viewmodel class responsible for handling data from food usercase classes to view layer
 */
@HiltViewModel
class FoodViewModel @Inject constructor(
    private val getFoods: GetFoods,
    private val getFoodByName: GetFoodByName,
    private val getCommonFoodsByName: GetCommonFoodsByName
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
     * get all common foods by title
     */
    private fun getFoodsByTitle(title: String) {
        viewModelScope.launch {
            getCommonFoodsByName(title).collect { result ->
                when (result.status) {
                    Status.SUCCESS -> handleCommonFoods(result.data ?: emptyList())
                    Status.ERROR -> {
                        _state.value = UiState.Error(
                            result.error?.message
                                ?: "error while retrieving common foods by title: $title"
                        )
                    }
                }

            }
        }
    }

    /**
     * handles successful common food data response and gets each food nutrients by name
     */
    private fun handleCommonFoods(commonFoods: List<CommonFood>) {
        viewModelScope.launch {
            val foods = mutableListOf<Food>()
            val fetchJobs = commonFoods.map { commonFood ->
                // using async to fire concurrent coroutines
                async {
                    getFoodByName(commonFood.name).collect { result ->
                        when (result.status) {
                            Status.SUCCESS -> {
                                // add result.data to a list/set
                                result.data?.let { food ->
                                    foods.add(food)
                                }
                            }

                            Status.ERROR -> UiState.Error(
                                result.error?.message
                                    ?: "error while retrieving food nutrients by title: ${commonFood.name}"
                            )

                        }
                    }
                }
            }
            fetchJobs.awaitAll()
            _state.value = UiState.Loaded(foods.sortedBy { it.name })
        }
    }
}