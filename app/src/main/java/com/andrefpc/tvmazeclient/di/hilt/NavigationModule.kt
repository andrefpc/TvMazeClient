package com.andrefpc.tvmazeclient.di.hilt

import com.andrefpc.tvmazeclient.presentation.compose.navigation.AppNavigation
import com.andrefpc.tvmazeclient.presentation.compose.navigation.AppNavigationImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {
    @Binds
    abstract fun bindAppNavigation(appNavigationImpl: AppNavigationImpl): AppNavigation
}