package com.gmartinsdev.nutri_demo.domain

import com.gmartinsdev.nutri_demo.data.FoodRepository
import com.gmartinsdev.nutri_demo.data.model.Food
import com.gmartinsdev.nutri_demo.data.remote.ApiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *   usecase class responsible for isolating logic from view layer and getting a food item
 *   by title from the database
 */
class GetFoodByName @Inject constructor(private val repository: FoodRepository) {
    operator fun invoke(foodName: String): Flow<ApiResult<Food>> {
        return repository.getFoodByName(foodName)
    }
}