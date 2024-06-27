package com.andrefpc.tvmazeclient.core.data

import java.io.Serializable

data class SeasonEpisodeStatus(
    var season: Season,
    var opened : Boolean = false,
    var episodes: List<Episode>
): Serializable
