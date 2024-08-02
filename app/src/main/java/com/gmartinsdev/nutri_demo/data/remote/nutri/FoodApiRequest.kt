package com.gmartinsdev.nutri_demo.data.remote.nutri

/**
 *  data class representing food API request body for food nutrients
 */
data class FoodApiRequest(
    val query: String,
    val include_subrecipe: Boolean = true
)
