package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.core.data.Episode
import com.andrefpc.tvmazeclient.core.data.Image

object EpisodeMocks {
    val episode = Episode(
        id = 1,
        name = "Episode 1",
        number = 1,
        season = 1,
        summary = "Summary",
        image = Image(
            medium = "",
            original = ""
        ),
        airdate = "",
        airtime = "",
        runtime = 0
    )
}