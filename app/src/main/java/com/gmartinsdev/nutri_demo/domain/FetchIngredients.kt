package com.gmartinsdev.nutri_demo.domain

import com.gmartinsdev.nutri_demo.data.FoodRepository
import com.gmartinsdev.nutri_demo.data.model.IngredientFood
import com.gmartinsdev.nutri_demo.data.model.SubRecipe
import com.gmartinsdev.nutri_demo.data.remote.ApiError
import com.gmartinsdev.nutri_demo.data.remote.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * usecase class responsible for fetching ingredients as foods given a list of ingredients from a food
 * sub-recipe
 */
class FetchIngredients @Inject constructor(private val repository: FoodRepository) {
    operator fun invoke(ingredients: List<SubRecipe>): Flow<ApiResult<List<IngredientFood>>> {
        return flow {
            // combines all flows into one list
            val ingredientFlows = ingredients.map { ingredient ->
                repository.fetchIngredientAsFoodByName(ingredient.food)
            }
            // maps all flow collections into one variable
            val combinedResults = ingredientFlows.map { ingredientFlow ->
                ingredientFlow.first() // collecting flows so repository function gets triggered
            }
            // checks for existing error type in list
            val errors = combinedResults.filterIsInstance<ApiError>()
            emit(
                // emits error if an error type exists
                if (errors.isNotEmpty()) {
                    val errorMessage = errors.joinToString("; ") { it.message }
                    ApiResult.error(ApiError(0, errorMessage))
                } else {
                    // checks for null in ingredients list. If any ingredient is null, return error.
                    // Otherwise, return success with same list object
                    try {
                        val parsedResults =
                            combinedResults.map { it.data?.first() }.requireNoNulls()
                        ApiResult.success(parsedResults)
                    } catch (e: IllegalArgumentException) {
                        ApiResult.error(
                            ApiError(
                                0,
                                e.message
                                    ?: "Error while retrieving ingredient foods: null element found"
                            )

                        )
                    }
                }
            )
        }
    }
}