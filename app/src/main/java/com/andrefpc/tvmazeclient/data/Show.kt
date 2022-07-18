package com.andrefpc.tvmazeclient.data

import java.io.Serializable

data class Show(
    var id: Int,
    var name: String,
    var image: Image,
    var schedule: Schedule,
    var genres: List<String>,
    val summary: String
): Serializable
