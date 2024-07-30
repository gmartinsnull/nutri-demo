package com.gmartinsdev.nutri_demo.domain

import com.gmartinsdev.nutri_demo.data.FoodRepository
import com.gmartinsdev.nutri_demo.data.model.Food
import com.gmartinsdev.nutri_demo.data.remote.ApiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *   usecase class responsible for isolating logic from view layer and getting all food items by title
 *   from the database
 */
class GetFoodByTitle @Inject constructor(private val repository: FoodRepository) {
    operator fun invoke(search: String): Flow<ApiResult<List<Food>>> {
        return repository.getFoods(search)
    }
}