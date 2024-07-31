package com.gmartinsdev.nutri_demo.data.remote

import com.gmartinsdev.nutri_demo.data.model.Food

/**
 * represents the response object from the food API
 */
data class FoodApiResponse(
    val foods: List<Food>
)
