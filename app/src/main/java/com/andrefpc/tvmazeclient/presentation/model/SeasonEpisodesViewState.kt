package com.andrefpc.tvmazeclient.presentation.model

import com.andrefpc.tvmazeclient.domain.model.SeasonEpisodes

data class SeasonEpisodesViewState(
    var season: SeasonViewState,
    var seasonName: String = "Season ${season.number}",
    var opened: Boolean = false,
    var episodes: List<EpisodeViewState>
) {
    constructor(seasonEpisodes: SeasonEpisodes) : this(
        season = SeasonViewState(seasonEpisodes.season),
        seasonName = "Season ${seasonEpisodes.season.number}",
        episodes = seasonEpisodes.episodes.map { EpisodeViewState(it) }
    )
}
