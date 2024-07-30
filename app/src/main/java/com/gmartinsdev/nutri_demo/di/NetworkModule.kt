package com.gmartinsdev.nutri_demo.di

import com.gmartinsdev.nutri_demo.BuildConfig
import com.gmartinsdev.nutri_demo.data.remote.FoodService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 *  hilt module responsible for injecting all network related dependencies
 */
@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun provideOkhttp(interceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()


    @Singleton
    @Provides
    fun provideConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @Singleton
    @Provides
    fun provideFoodService(
        converterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient
    ): FoodService = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(converterFactory)
        .client(okHttpClient)
        .build()
        .create(FoodService::class.java)
}