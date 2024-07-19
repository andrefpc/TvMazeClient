package com.andrefpc.tvmazeclient.domain.model

import java.io.Serializable

data class ApiError(
    var message: String? = null
) : Serializable
