package com.gmartinsdev.nutri_demo.data.remote

import com.gmartinsdev.nutri_demo.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 *  service class responsible for handling API calls
 */
interface FoodService {
    @Headers(
        "Content-Type: application/json",
        "x-app-id: ${BuildConfig.API_ID}",
        "x-app-key: ${BuildConfig.API_KEY}"
    )
    @GET("/v2/search/instant")
    suspend fun fetchFoods(@Query("query") query: String): Response<ApiResponse>
}