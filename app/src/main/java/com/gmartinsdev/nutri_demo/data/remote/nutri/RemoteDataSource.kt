package com.gmartinsdev.nutri_demo.data.remote.nutri

import com.gmartinsdev.nutri_demo.BuildConfig
import com.gmartinsdev.nutri_demo.data.remote.ApiResult
import com.gmartinsdev.nutri_demo.data.remote.google_maps.GoogleMapsService
import com.gmartinsdev.nutri_demo.data.remote.google_maps.NearbySearchApiResponse
import com.gmartinsdev.nutri_demo.data.remote.handleApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 *  data source class responsible for fetching data from API endpoint
 */
class RemoteDataSource @Inject constructor(
    private val service: FoodService,
    private val nearbySearchService: GoogleMapsService
) {
    /**
     * fetches data from common foods API
     */
    fun fetchCommonFoodByTitle(foodTitle: String): Flow<ApiResult<CommonFoodsApiResponse>> = flow {
        emit(handleApiCall { service.fetchCommonFoods(foodTitle) })
    }

    /**
     * fetches data from natural language food and nutrients API
     */
    fun fetchFoodNutrientsByTitle(foodTitle: String): Flow<ApiResult<FoodNutrientsApiResponse>> =
        flow {
            emit(handleApiCall { service.fetchFoodNutrients(FoodApiRequest(foodTitle)) })
        }

    /**
     * fetches data from google maps service API
     */
    fun fetchNearbyPlaces(
        keyword: String,
        location: String
    ): Flow<ApiResult<NearbySearchApiResponse>> =
        flow {
            emit(handleApiCall {
                nearbySearchService.searchNearby(
                    keyword,
                    location,
                    apiKey = BuildConfig.MAPS_API_KEY
                )
            })
        }
}