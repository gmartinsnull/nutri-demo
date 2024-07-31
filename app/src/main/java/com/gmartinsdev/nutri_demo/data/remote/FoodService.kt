package com.gmartinsdev.nutri_demo.data.remote

import com.gmartinsdev.nutri_demo.BuildConfig
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 *  service class responsible for handling API calls
 */
interface FoodService {
    @Headers(
        "Content-Type: application/json",
        "x-app-id: ${BuildConfig.API_ID}",
        "x-app-key: ${BuildConfig.API_KEY}"
    )
    @POST("v2/natural/nutrients")
    suspend fun fetchFoodInfo(@Body body: FoodApiRequest): Response<FoodApiResponse>
}