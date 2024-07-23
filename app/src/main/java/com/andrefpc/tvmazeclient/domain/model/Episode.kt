package com.andrefpc.tvmazeclient.domain.model

data class Episode(
    var id: Int,
    var name: String,
    var number: Int,
    var season: Int,
    val summary: String,
    var image: Image?,
    var airdate: String,
    var airtime: String,
    var runtime: Int
)