package com.gmartinsdev.nutri_demo.data.remote

import com.gmartinsdev.nutri_demo.data.model.Food

/**
 *   Created by gmartins on 2024-07-29
 *   Description: represents the response object from the API
 */
data class ApiResponse(
    val branded: List<Food>
)
