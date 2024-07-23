package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.data.remote.model.ShowDto
import com.andrefpc.tvmazeclient.domain.model.Image
import com.andrefpc.tvmazeclient.domain.model.Schedule
import com.andrefpc.tvmazeclient.domain.model.Show

object ShowMocks {
    val showDto = ShowDto(
        id = 1,
        name = "Test Show",
        image = Image(
            medium = "",
            original = ""
        ),
        schedule = Schedule(
            time = "12:00",
            days = arrayListOf("Monday")
        ),
        genres = arrayListOf("Comedy"),
        summary = "Test Show Summary",
        premiered = "",
        ended = ""
    )
    val show = showDto.toDomain()

    val showUpdatedDto = ShowDto(
        id = 2,
        name = "Updated Show",
        image = Image(
            medium = "",
            original = ""
        ),
        schedule = Schedule(
            time = "12:00",
            days = arrayListOf("Monday")
        ),
        genres = arrayListOf("Comedy"),
        summary = "Test Show Summary",
        premiered = "",
        ended = ""
    )

    val showUpdated = showUpdatedDto.toDomain()
}