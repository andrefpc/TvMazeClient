package com.andrefpc.tvmazeclient.core.data

import java.io.Serializable

data class ApiError(
    var message: String? = null
) : Serializable
