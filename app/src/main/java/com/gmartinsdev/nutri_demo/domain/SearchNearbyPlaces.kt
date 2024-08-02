package com.gmartinsdev.nutri_demo.domain

import com.gmartinsdev.nutri_demo.data.remote.ApiResult
import com.gmartinsdev.nutri_demo.data.remote.NearbySearchRepository
import com.gmartinsdev.nutri_demo.data.remote.google_maps.NearbySearchApiResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *   usecase class responsible for isolating logic from view layer and getting all nearby places via
 *   repository
 */
class SearchNearbyPlaces @Inject constructor(private val repository: NearbySearchRepository) {
    operator fun invoke(keyword: String): Flow<ApiResult<NearbySearchApiResponse>> {
        return repository.getNearbyPlaces(keyword)
    }
}