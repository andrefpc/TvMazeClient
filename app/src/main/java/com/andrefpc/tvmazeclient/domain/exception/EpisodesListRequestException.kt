package com.andrefpc.tvmazeclient.domain.exception

import com.andrefpc.tvmazeclient.domain.model.ApiError

class EpisodesListRequestException(val apiError: ApiError) : Exception() {
    override val message: String
        get() = apiError.message ?: "Episode list not found"
}