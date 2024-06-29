package com.andrefpc.tvmazeclient.di.hilt

import com.andrefpc.tvmazeclient.core.domain.repository.TvMazeRepository
import com.andrefpc.tvmazeclient.core.domain.repository.TvMazeRepositoryImpl
import com.andrefpc.tvmazeclient.core.domain.room.ShowRoomRepository
import com.andrefpc.tvmazeclient.core.domain.room.ShowRoomRepositoryImpl
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