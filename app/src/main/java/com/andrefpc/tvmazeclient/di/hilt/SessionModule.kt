package com.andrefpc.tvmazeclient.di.hilt

import com.andrefpc.tvmazeclient.core.session.PinSession
import com.andrefpc.tvmazeclient.core.session.PinSessionImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SessionModule {
    @Binds
    abstract fun bindPinSession(pinSessionImpl: PinSessionImpl): PinSession
}