package com.andrefpc.tvmazeclient.data.exception

import com.andrefpc.tvmazeclient.domain.model.ApiError

class PeopleListRequestException(val apiError: ApiError) : Exception() {
    override val message: String
        get() = apiError.message ?: "People list not found"
}