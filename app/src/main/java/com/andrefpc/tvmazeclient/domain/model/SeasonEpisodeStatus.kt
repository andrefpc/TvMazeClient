package com.andrefpc.tvmazeclient.domain.model

import java.io.Serializable

data class SeasonEpisodeStatus(
    var season: Season,
    var opened: Boolean = false,
    var episodes: List<Episode>
) : Serializable {
    val seasonName: String
        get() {
            return "Season ${season.number}"
        }
}