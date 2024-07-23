package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.domain.model.SeasonEpisodes

object SeasonEpisodeMocks {
    val seasonEpisodes = SeasonEpisodes(
       season = SeasonMocks.season,
        opened = false,
        episodes = arrayListOf(EpisodeMocks.episode)
    )
}