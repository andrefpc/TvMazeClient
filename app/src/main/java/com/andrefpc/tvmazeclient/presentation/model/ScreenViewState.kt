package com.andrefpc.tvmazeclient.presentation.model

sealed class ScreenViewState {
    data object Initial : ScreenViewState()
    data object Empty : ScreenViewState()
    data object Loading : ScreenViewState()
    data object Success : ScreenViewState()
    data class Error(val error: Throwable) : ScreenViewState()
}