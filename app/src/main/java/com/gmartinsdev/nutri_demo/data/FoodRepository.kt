package com.gmartinsdev.nutri_demo.data

import com.gmartinsdev.nutri_demo.data.local.FoodDao
import com.gmartinsdev.nutri_demo.data.model.CommonFood
import com.gmartinsdev.nutri_demo.data.model.Food
import com.gmartinsdev.nutri_demo.data.model.FoodWithIngredients
import com.gmartinsdev.nutri_demo.data.remote.ApiResult
import com.gmartinsdev.nutri_demo.data.remote.RemoteDataSource
import com.gmartinsdev.nutri_demo.data.remote.FoodNotFoundThrowable
import com.gmartinsdev.nutri_demo.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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
     * retrieves food data from local database or fetches from remote data source
     */
    fun getCommonFoods(foodName: String): Flow<ApiResult<List<CommonFood>>> {
        return networkBoundResource(
            query = {
                foodDao.getCommonFoodByName(foodName)
            },
            fetch = {
                remoteDataSource.fetchCommonFoodByTitle(foodName)
            },
            saveFetchResult = { response ->
                val result = response.first()
                if (result.data == null && result.error != null) { // api may return success but still contain error in body
                    throw FoodNotFoundThrowable(result.error.message)
                } else {
                    result.data?.common?.forEach {
                        foodDao.insertCommonFood(it)
                    }
                }
            },
            shouldFetch = {
                // fetches from remote data source if db is empty
                it.isEmpty()
            }
        ).flowOn(ioDispatcher)
    }

    /**
     * retrieves food data from local database or fetches from remote data source
     */
    fun getFoods(foodName: String): Flow<ApiResult<List<Food>>> {
        return networkBoundResource(
            query = {
                foodDao.getFoodsByName(foodName)
            },
            fetch = {
                remoteDataSource.fetchFoodNutrientsByTitle(foodName)
            },
            saveFetchResult = { response ->
                val result = response.first()
                if (result.data == null && result.error != null) { // api may return success but still contain error in body
                    throw FoodNotFoundThrowable(result.error.message)
                } else {
                    result.data?.foods?.forEach {
                        foodDao.insertFood(it.parseToFood())
                        it.ingredients?.forEach {
                            foodDao.insertIngredients(it)
                        }
                    }
                }
            },
            shouldFetch = {
                // fetches from remote data source if db is empty
                it.isEmpty()
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

    /**
     * retrieves list of all foods from the database
     */
    fun getFoodIngredientsFromDb(foodId: Int): Flow<ApiResult<FoodWithIngredients>> = flow {
        val data = foodDao.getFoodWithIngredients(foodId).first()
        emit(ApiResult.success(data))
    }
}