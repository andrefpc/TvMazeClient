package com.andrefpc.tvmazeclient.di.hilt

import com.andrefpc.tvmazeclient.util.CoroutineContextProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {
    @Provides
    @Singleton
    @ProdCoroutineContext
    fun provideCoroutineContextProvider(): CoroutineContextProvider {
        return CoroutineContextProvider()
    }

    @Singleton
    @Provides
    @ProdCoroutineContext
    fun provideIODispatcher(): CoroutineContext {
        return Dispatchers.IO
    }

    @Singleton
    @Provides
    @ProdCoroutineContext
    fun provideMainDispatcher(): CoroutineContext {
        return Dispatchers.Main
    }
}
