package com.gmartinsdev.nutri_demo.di

import android.content.Context
import androidx.room.Room
import com.gmartinsdev.nutri_demo.data.FoodRepository
import com.gmartinsdev.nutri_demo.data.local.AppDatabase
import com.gmartinsdev.nutri_demo.data.local.FoodDao
import com.gmartinsdev.nutri_demo.data.remote.RemoteDataSource
import com.gmartinsdev.nutri_demo.data.remote.FoodService
import com.gmartinsdev.nutri_demo.domain.GetFoodByName
import com.gmartinsdev.nutri_demo.domain.GetFoods
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

/**
 *  hilt module responsible for injecting all local data related dependencies
 */
@InstallIn(SingletonComponent::class)
@Module
object DataModule {
    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext applicationContext: Context): AppDatabase =
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database"
        ).build()

    @Singleton
    @Provides
    fun provideFoodDao(database: AppDatabase): FoodDao = database.getFoodDao()

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Singleton
    @Provides
    fun provideRemoteDataSource(service: FoodService): RemoteDataSource =
        RemoteDataSource(service)

    @Singleton
    @Provides
    fun provideFoodRepository(
        remoteDataSource: RemoteDataSource,
        foodDao: FoodDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): FoodRepository = FoodRepository(remoteDataSource, foodDao, ioDispatcher)

    @Singleton
    @Provides
    fun provideGetFoods(
        repository: FoodRepository
    ): GetFoods = GetFoods(repository)

    @Singleton
    @Provides
    fun provideGetFoodByName(
        repository: FoodRepository
    ): GetFoodByName = GetFoodByName(repository)
}