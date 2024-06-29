package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.core.data.SeasonEpisodeStatus

object SeasonEpisodeMocks {
    val seasonEpisodeStatus = SeasonEpisodeStatus(
       season = SeasonMocks.season,
        opened = false,
        episodes = arrayListOf(EpisodeMocks.episode)
    )
}