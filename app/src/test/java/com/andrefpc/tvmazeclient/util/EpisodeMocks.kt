package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.data.remote.model.EpisodeDto
import com.andrefpc.tvmazeclient.data.remote.model.ImageDto
import com.andrefpc.tvmazeclient.domain.model.Episode
import com.andrefpc.tvmazeclient.domain.model.Image
import com.andrefpc.tvmazeclient.presentation.model.EpisodeViewState

object EpisodeMocks {
    val episodeDto = EpisodeDto(
        id = 1,
        name = "Episode 1",
        number = 1,
        season = 1,
        summary = "Summary",
        image = ImageDto(
            medium = "",
            original = ""
        ),
        airdate = "",
        airtime = "",
        runtime = 0
    )

    val episode = episodeDto.toDomain()
    val episodeViewState = EpisodeViewState(episode)
}