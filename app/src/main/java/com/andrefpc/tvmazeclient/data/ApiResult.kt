package com.andrefpc.tvmazeclient.data

sealed class ApiResult<out T> {
    class Error(val apiError: ApiError) : ApiResult<Nothing>()
    class Success<T>(val result: T?) : ApiResult<T>()
}
