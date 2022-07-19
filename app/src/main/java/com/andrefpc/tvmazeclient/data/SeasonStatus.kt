package com.andrefpc.tvmazeclient.data

import java.io.Serializable

data class SeasonStatus(
    var season: Season,
    var opened : Boolean = false,
    var episodes: List<Episode>
): Serializable
