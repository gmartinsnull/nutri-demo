package com.gmartinsdev.nutri_demo.data.remote

/**
 *  data class representing food API request body for food nutrients
 */
data class FoodApiRequest(
    val query: String,
    val include_subrecipe: Boolean = true
)
