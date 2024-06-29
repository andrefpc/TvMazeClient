package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.core.data.Image
import com.andrefpc.tvmazeclient.core.data.Person
import com.andrefpc.tvmazeclient.core.data.Schedule
import com.andrefpc.tvmazeclient.core.data.Show

object PersonMocks {
    val person = Person(
        id = 1,
        name = "Test Person",
        image = Image(
            medium = "",
            original = ""
        )
    )

    val personUpdated = Person(
        id = 2,
        name = "Updated Person",
        image = Image(
            medium = "",
            original = ""
        )
    )
}