package com.andrefpc.tvmazeclient.presentation.compose.navigation

import android.app.Activity
import android.os.Bundle

interface AppNavigation {
    fun navigateTo(activity: Activity, navigatorScreen: NavigatorScreen)
}