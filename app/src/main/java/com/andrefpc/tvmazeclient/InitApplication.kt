package com.andrefpc.tvmazeclient

import android.app.Application
import com.andrefpc.tvmazeclient.di.koin.apiModule
import com.andrefpc.tvmazeclient.di.koin.coroutineContextProviderModule
import com.andrefpc.tvmazeclient.di.koin.remoteModule
import com.andrefpc.tvmazeclient.di.koin.repositoryModule
import com.andrefpc.tvmazeclient.di.koin.navigationModule
import com.andrefpc.tvmazeclient.di.koin.useCaseModules
import com.andrefpc.tvmazeclient.di.koin.viewModelModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@HiltAndroidApp
class InitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@InitApplication)
            modules(
                listOf(
                    apiModule,
                    repositoryModule,
                    navigationModule,
                    useCaseModules,
                    viewModelModule,
                    remoteModule,
                    coroutineContextProviderModule
                )
            )
        }
    }
}