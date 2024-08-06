package com.gmartinsdev.nutri_demo.domain

import com.gmartinsdev.nutri_demo.data.remote.nutri.FoodRepository
import com.gmartinsdev.nutri_demo.data.model.CommonFood
import com.gmartinsdev.nutri_demo.data.remote.ApiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *   usecase class responsible for isolating logic from view layer and getting all common food items
 *   by title from the database
 */
class GetCommonFoodsByName @Inject constructor(private val repository: FoodRepository) {
    operator fun invoke(foodName: String): Flow<ApiResult<List<CommonFood>>> {
        return repository.getCommonFoods(foodName)
    }
}