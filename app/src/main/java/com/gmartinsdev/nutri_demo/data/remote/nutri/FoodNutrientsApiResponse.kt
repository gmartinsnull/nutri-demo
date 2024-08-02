package com.gmartinsdev.nutri_demo.data.remote.nutri

import com.gmartinsdev.nutri_demo.data.model.FoodNutrientsResponse

/**
 * represents the response object from the food nutrients API
 */
data class FoodNutrientsApiResponse(
    val foods: List<FoodNutrientsResponse>,
    val message: String?
)
