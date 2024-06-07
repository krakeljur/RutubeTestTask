package com.example.rutubetesttask.data.di

import com.example.rutubetesttask.common.Const
import com.example.rutubetesttask.data.catalog.CatalogApi
import com.example.rutubetesttask.data.forecast.ForecastApi
import com.example.rutubetesttask.data.retrofit.NetworkErrorInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    @Named("CatalogRetrofit")
    fun provideCatalogRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Const.BASE_URL_CITIES)
        .client(
            OkHttpClient().newBuilder()
                .addInterceptor(NetworkErrorInterceptor())
                .build()
        )
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build()

    @Singleton
    @Provides
    @Named("ForecastRetrofit")
    fun provideForecastRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Const.BASE_URL_FORECAST)
        .client(
            OkHttpClient().newBuilder()
                .addInterceptor(NetworkErrorInterceptor())
                .build()
        )
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build()


    @Provides
    @Singleton
    fun provideCatalogApi(@Named("CatalogRetrofit") retrofit: Retrofit): CatalogApi =
        retrofit.create(CatalogApi::class.java)

    @Provides
    @Singleton
    fun provideForecastApi(@Named("ForecastRetrofit") retrofit: Retrofit): ForecastApi =
        retrofit.create(ForecastApi::class.java)


}