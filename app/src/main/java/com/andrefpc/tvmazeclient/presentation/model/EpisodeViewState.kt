package com.andrefpc.tvmazeclient.presentation.model

import com.andrefpc.tvmazeclient.domain.model.Episode

data class EpisodeViewState(
    var id: Int,
    var name: String,
    var season: Int,
    var seasonEpisode: String,
    val summary: String,
    var thumb: String?,
    var image: String?,
    var airdate: String,
    var time: String
) {
    constructor(episode: Episode) : this(
        id = episode.id,
        name = episode.name,
        season = episode.season,
        seasonEpisode = (if (episode.season > 9) "S${episode.season}" else "S0${episode.season}") +
                " | ${if (episode.number > 9) "E${episode.number}" else "E0${episode.number}"}",
        summary = episode.summary,
        thumb = episode.image?.medium,
        image = episode.image?.original,
        airdate = episode.airdate,
        time = "${episode.airtime} (${episode.runtime} minutes)"
    )
}