package com.gmartinsdev.nutri_demo.data.remote

/**
 *  data class representing food api request body
 */
data class FoodApiRequest(
    val query: String,
    val include_subrecipe: Boolean = true,
    val ingredient_statement: Boolean = true
)
