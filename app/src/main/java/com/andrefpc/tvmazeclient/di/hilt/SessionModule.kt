package com.andrefpc.tvmazeclient.di.hilt

import com.andrefpc.tvmazeclient.domain.repository.preferences.PinRepository
import com.andrefpc.tvmazeclient.data.repository.preferences.PinRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SessionModule {
    @Binds
    abstract fun bindPinSession(pinSessionImpl: PinRepositoryImpl): PinRepository
}