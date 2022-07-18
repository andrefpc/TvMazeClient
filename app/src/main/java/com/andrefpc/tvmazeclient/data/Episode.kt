package com.andrefpc.tvmazeclient.data

data class Episode(
    var id: Int,
    var name: String,
    var number: Int,
    var season: Int,
    val summary: String,
    var image: Image,
    var airDate: String,
    var airTime: String,
    var runtime: Int
)