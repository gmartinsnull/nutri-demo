package com.gmartinsdev.nutri_demo.data.remote

import com.gmartinsdev.nutri_demo.data.remote.google_maps.NearbySearchApiResponse
import com.gmartinsdev.nutri_demo.data.remote.nutri.RemoteDataSource
import com.gmartinsdev.nutri_demo.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 *  repository responsible for handling nearby places data from the network
 */
class NearbySearchRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    /**
     * retrieves nearby search data from local database or fetches from remote data source
     */
    fun getNearbyPlaces(keyword: String): Flow<ApiResult<NearbySearchApiResponse>> = flow {
        val vancouver = "49.283832198,-123.119332856"
        emit(remoteDataSource.fetchNearbyPlaces(keyword, vancouver).first())
    }.flowOn(ioDispatcher)
}