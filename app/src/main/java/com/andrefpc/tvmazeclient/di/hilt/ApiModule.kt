package com.andrefpc.tvmazeclient.di.hilt

import com.andrefpc.tvmazeclient.api.TvMazeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Singleton
    @Provides
    fun provideTvMazeApi(retrofit: Retrofit): TvMazeApi = retrofit.create(TvMazeApi::class.java)
}