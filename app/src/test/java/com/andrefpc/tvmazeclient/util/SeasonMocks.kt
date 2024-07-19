package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.data.remote.model.SeasonDto
import com.andrefpc.tvmazeclient.domain.model.Season

object SeasonMocks {
    val seasonDto = SeasonDto(
        id = 1,
        number = 1,
        episodeOrder = 1,
        opened = false
    )

    val season = seasonDto.toDomain()
}