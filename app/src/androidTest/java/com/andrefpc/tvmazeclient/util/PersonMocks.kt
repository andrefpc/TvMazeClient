package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.data.remote.model.ImageDto
import com.andrefpc.tvmazeclient.data.remote.model.PersonDto
import com.andrefpc.tvmazeclient.domain.model.Image
import com.andrefpc.tvmazeclient.domain.model.Person

object PersonMocks {
    val personDto = PersonDto(
        id = 1,
        name = "Test Person",
        image = ImageDto(
            medium = "",
            original = ""
        )
    )

    val person = personDto.toDomain()

    val personUpdatedDto = PersonDto(
        id = 2,
        name = "Updated Person",
        image = ImageDto(
            medium = "",
            original = ""
        )
    )

    val personUpdated = personDto.toDomain()
}