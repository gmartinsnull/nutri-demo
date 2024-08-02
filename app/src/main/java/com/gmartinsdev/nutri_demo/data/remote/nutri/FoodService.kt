package com.gmartinsdev.nutri_demo.data.remote.nutri

import com.gmartinsdev.nutri_demo.BuildConfig
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 *  service class responsible for handling calls from food API
 */
interface FoodService {
    @Headers(
        "Content-Type: application/json",
        "x-app-id: ${BuildConfig.API_ID}",
        "x-app-key: ${BuildConfig.API_KEY}"
    )
    @GET("v2/search/instant")
    suspend fun fetchCommonFoods(@Query("query") query: String): Response<CommonFoodsApiResponse>

    @Headers(
        "Content-Type: application/json",
        "x-app-id: ${BuildConfig.API_ID}",
        "x-app-key: ${BuildConfig.API_KEY}"
    )
    @POST("v2/natural/nutrients")
    suspend fun fetchFoodNutrients(@Body body: FoodApiRequest): Response<FoodNutrientsApiResponse>
}