package com.andrefpc.tvmazeclient.domain.exception

import com.andrefpc.tvmazeclient.domain.model.ApiError

class SeasonListRequestException(val apiError: ApiError) : Exception() {
    override val message: String
        get() = apiError.message ?: "Season list not found"
}