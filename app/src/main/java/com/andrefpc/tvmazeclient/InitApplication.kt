package com.andrefpc.tvmazeclient

import android.app.Application
import com.andrefpc.tvmazeclient.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class InitApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@InitApplication)
            modules(
                listOf(
                    apiModule,
                    repositoryModule,
                    sessionModule,
                    viewModelModule,
                    remoteModule,
                    coroutineContextProviderModule
                )
            )
        }
    }
}