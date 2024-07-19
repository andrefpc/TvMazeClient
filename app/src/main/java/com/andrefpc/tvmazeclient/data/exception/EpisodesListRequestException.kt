package com.andrefpc.tvmazeclient.data.exception

import com.andrefpc.tvmazeclient.domain.model.ApiError

class EpisodesListRequestException(val apiError: ApiError) : Exception() {
    override val message: String
        get() = apiError.message ?: "Episode list not found"
}