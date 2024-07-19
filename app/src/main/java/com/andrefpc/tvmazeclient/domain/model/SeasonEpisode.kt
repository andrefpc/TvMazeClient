package com.andrefpc.tvmazeclient.domain.model

import java.io.Serializable

data class SeasonEpisode(
    var season: Season? = null,
    var episode: Episode? = null
) : Serializable
