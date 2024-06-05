package com.example.rutubetesttask.data.di

import com.example.rutubetesttask.data.RepositoryImpl
import com.example.rutubetesttask.domain.repositories.CitiesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {


    @Binds
    @Singleton
    fun bindRepository(repository: RepositoryImpl) : CitiesRepository



}