package com.gmartinsdev.nutri_demo.data.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 *  data source class responsible for fetching data from API endpoint
 */
class RemoteDataSource @Inject constructor(
    private val service: FoodService
) {
    /**
     * fetches data from food API endpoint
     */
    fun fetchFoods(search: String): Flow<ApiResult<ApiResponse>> = flow {
        emit(handleApiCall { service.fetchFoods(search) })
    }
}