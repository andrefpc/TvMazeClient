package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.core.data.Image
import com.andrefpc.tvmazeclient.core.data.Schedule
import com.andrefpc.tvmazeclient.core.data.Show

object ShowMocks {
    val show = Show(
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

    val showUpdated = Show(
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
}