package com.andrefpc.tvmazeclient.domain.model

import java.io.Serializable

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
) : Serializable {
    val seasonEpisodeTitle: String
        get() {
            val season = if (season > 9) "S${season}" else "S0${season}"
            val episode = if (number > 9) "E${number}" else "E0${number}"
            return "$season | $episode"
        }
    val timeDurationTitle: String
        get() {
            return "${airtime} (${runtime} minutes)"
        }
}