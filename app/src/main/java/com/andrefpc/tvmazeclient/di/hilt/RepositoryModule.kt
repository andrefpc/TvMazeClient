package com.andrefpc.tvmazeclient.di.hilt

import com.andrefpc.tvmazeclient.data.repository.api.PersonRepositoryImpl
import com.andrefpc.tvmazeclient.data.repository.api.ShowRepositoryImpl
import com.andrefpc.tvmazeclient.data.repository.database.ShowRoomRepositoryImpl
import com.andrefpc.tvmazeclient.domain.repository.api.PersonRepository
import com.andrefpc.tvmazeclient.domain.repository.api.ShowRepository
import com.andrefpc.tvmazeclient.domain.repository.database.ShowRoomRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindShowRepository(showRepositoryImpl: ShowRepositoryImpl): ShowRepository
    @Binds
    abstract fun bindPersonRepository(personRepositoryImpl: PersonRepositoryImpl): PersonRepository

    @Binds
    abstract fun bindShowRoomRepository(showRoomRepositoryImpl: ShowRoomRepositoryImpl): ShowRoomRepository
}