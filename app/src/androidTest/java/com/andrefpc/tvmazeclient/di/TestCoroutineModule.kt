package com.andrefpc.tvmazeclient.di

import com.andrefpc.tvmazeclient.di.hilt.TestCoroutineContext
import com.andrefpc.tvmazeclient.util.CoroutineContextProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestCoroutineModule {
    @Singleton
    @Provides
    @TestCoroutineContext
    fun provideCoroutineContextProvider(): CoroutineContextProvider {
        return TestCoroutineContextProvider(StandardTestDispatcher())
    }

    @Singleton
    @Provides
    @TestCoroutineContext
    fun provideTestIODispatcher(): TestDispatcher {
        return StandardTestDispatcher()
    }

    @Singleton
    @Provides
    @TestCoroutineContext
    fun provideTestMainDispatcher(): TestDispatcher {
        return StandardTestDispatcher()
    }
}
