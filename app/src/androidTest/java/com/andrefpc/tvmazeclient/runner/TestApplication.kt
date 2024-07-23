package com.andrefpc.tvmazeclient.runner

import android.app.Application
import com.andrefpc.tvmazeclient.di.koin.apiModule
import com.andrefpc.tvmazeclient.di.koin.coroutineContextProviderModule
import com.andrefpc.tvmazeclient.di.koin.remoteModule
import com.andrefpc.tvmazeclient.di.koin.repositoryModule
import com.andrefpc.tvmazeclient.di.koin.navigationModule
import com.andrefpc.tvmazeclient.di.koin.useCaseModules
import com.andrefpc.tvmazeclient.di.koin.viewModelModule
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.testing.HiltAndroidTest
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@HiltAndroidTest
class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}