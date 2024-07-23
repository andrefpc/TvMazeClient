package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.domain.model.SeasonEpisodes
import com.andrefpc.tvmazeclient.presentation.model.SeasonEpisodesViewState

object SeasonEpisodeMocks {
    val seasonEpisodes = SeasonEpisodes(
       season = SeasonMocks.season,
        episodes = arrayListOf(EpisodeMocks.episode)
    )

    val seasonEpisodesViewState = SeasonEpisodesViewState(seasonEpisodes)
}