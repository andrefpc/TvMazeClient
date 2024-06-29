package com.andrefpc.tvmazeclient.core.data

sealed class ScreenState {
    object Initial : ScreenState()
    object Empty : ScreenState()
    object Loading : ScreenState()
    object Success : ScreenState()
    data class Error(val error: Throwable) : ScreenState()
}