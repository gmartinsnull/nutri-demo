package com.gmartinsdev.nutri_demo.data

import com.gmartinsdev.nutri_demo.data.model.Food
import com.gmartinsdev.nutri_demo.data.local.FoodDao
import com.gmartinsdev.nutri_demo.data.remote.ApiResponse
import com.gmartinsdev.nutri_demo.data.remote.ApiResult
import com.gmartinsdev.nutri_demo.data.remote.RemoteDataSource
import com.gmartinsdev.nutri_demo.data.remote.FoodNotFoundThrowable
import com.gmartinsdev.nutri_demo.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import networkBoundResource
import javax.inject.Inject

/**
 *  repository responsible for handling food data from the network and storing it in the local database
 *  accordingly
 */
class FoodRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val foodDao: FoodDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    /**
     * attempts to retrieve food data from local database. Otherwise, fetches from API
     */
    fun getFoods(foodName: String): Flow<ApiResult<List<Food>>> {
        return networkBoundResource(
            query = {
                foodDao.getFoodsByName(foodName)
            },
            fetch = {
                if (foodName.isNotEmpty()) {
                    remoteDataSource.fetchFoods(foodName)
                } else {
                    // the API won't handle empty search queries. So in case of an empty db or fresh
                    // install, we return an empty list as a safety measure
                    flowOf(
                        ApiResult.success(
                            ApiResponse(
                                emptyList()
                            )
                        )
                    )
                }
            },
            saveFetchResult = { response ->
                val result = response.first()
                if (result.data == null && result.error != null) { // api may return success but still contain error in body
                    throw FoodNotFoundThrowable(result.error.message)
                } else {
                    result.data?.branded?.forEach {
                        foodDao.insertAll(it)
                    }
                }
            },
            shouldFetch = { localData ->
                // only fetches data if db is empty
                localData.isEmpty()
            }
        ).flowOn(ioDispatcher)
    }

    /**
     * retrieves list of all foods from the database
     */
    fun getFoodsFromDb(): Flow<ApiResult<List<Food>>> = flow {
        val data = foodDao.getAll().first()
        emit(ApiResult.success(data))
    }
}