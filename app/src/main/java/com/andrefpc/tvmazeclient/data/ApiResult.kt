package com.andrefpc.tvmazeclient.data

sealed class ApiResult<out T> {
    object Error : ApiResult<Nothing>()
    class Success<T>(val result: T?) : ApiResult<T>()
}
