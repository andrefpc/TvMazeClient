package com.andrefpc.tvmazeclient.core.data

import java.io.Serializable

data class Season(
    var id: Int,
    var number: Int,
    var episodeOrder: Int,
    var opened: Boolean
): Serializable
