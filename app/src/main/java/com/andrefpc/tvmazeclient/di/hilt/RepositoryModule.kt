package com.andrefpc.tvmazeclient.di.hilt

import com.andrefpc.tvmazeclient.domain.repository.api.TvMazeRepository
import com.andrefpc.tvmazeclient.data.repository.api.TvMazeRepositoryImpl
import com.andrefpc.tvmazeclient.domain.repository.database.ShowRoomRepository
import com.andrefpc.tvmazeclient.data.repository.database.ShowRoomRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindTvMazeRepository(tvMazeRepositoryImpl: TvMazeRepositoryImpl): TvMazeRepository

    @Binds
    abstract fun bindShowRoomRepository(showRoomRepositoryImpl: ShowRoomRepositoryImpl): ShowRoomRepository
}