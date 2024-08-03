package com.gmartinsdev.nutri_demo.domain

import com.gmartinsdev.nutri_demo.data.FoodRepository
import com.gmartinsdev.nutri_demo.data.model.FoodWithIngredients
import com.gmartinsdev.nutri_demo.data.remote.ApiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *   usecase class responsible for isolating logic from view layer and getting all food items
 *   along with its ingredients by food id from the database
 */
class GetFoodWithIngredients @Inject constructor(private val repository: FoodRepository) {
    operator fun invoke(foodId: Int): Flow<ApiResult<List<FoodWithIngredients>>> {
        return repository.getFoodIngredientsFromDb(foodId)
    }
}