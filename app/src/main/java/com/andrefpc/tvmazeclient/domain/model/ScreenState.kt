package com.andrefpc.tvmazeclient.domain.model

sealed class ScreenState {
    object Initial : ScreenState()
    object Empty : ScreenState()
    object Loading : ScreenState()
    object Success : ScreenState()
    data class Error(val error: Throwable) : ScreenState()
}