package com.andrefpc.tvmazeclient

import android.app.Application
import com.andrefpc.tvmazeclient.di.koin.apiModule
import com.andrefpc.tvmazeclient.di.koin.coroutineContextProviderModule
import com.andrefpc.tvmazeclient.di.koin.remoteModule
import com.andrefpc.tvmazeclient.di.koin.repositoryModule
import com.andrefpc.tvmazeclient.di.koin.sessionModule
import com.andrefpc.tvmazeclient.di.koin.viewModelModule
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