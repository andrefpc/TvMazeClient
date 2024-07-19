package com.andrefpc.tvmazeclient.data.exception

import com.andrefpc.tvmazeclient.domain.model.ApiError

class ShowListRequestException(val apiError: ApiError) : Exception() {
    override val message: String
        get() = apiError.message ?: "Show list not found"
}