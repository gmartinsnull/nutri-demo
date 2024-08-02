package com.gmartinsdev.nutri_demo.data.remote.google_maps

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  service class responsible for handling calls from google maps API
 */
interface GoogleMapsService {
    @GET("place/nearbysearch/json")
    suspend fun searchNearby(
        @Query("keyword") keyword: String,
        @Query("location") location: String,
        @Query("radius") radius: Int = 1500,
        @Query("key") apiKey: String
    ): Response<NearbySearchApiResponse>
}