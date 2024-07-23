package com.andrefpc.tvmazeclient.domain.exception

import com.andrefpc.tvmazeclient.domain.model.ApiError

class CastListRequestException(val apiError: ApiError) : Exception() {
    override val message: String
        get() = apiError.message ?: "Cast list not found"
}