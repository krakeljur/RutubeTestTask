package com.example.rutubetesttask.data.di

import com.example.rutubetesttask.data.catalog.CatalogApi
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
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://gist.githubusercontent.com/")
        .client(
            OkHttpClient().newBuilder()
                .addInterceptor(NetworkErrorInterceptor())
                .build()
        )
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build()


    @Provides
    @Singleton
    fun provideCatalogApi(@Named("CatalogRetrofit") retrofit: Retrofit) : CatalogApi = retrofit.create(CatalogApi::class.java)


}