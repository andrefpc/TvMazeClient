package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.data.remote.model.CastDto
import com.andrefpc.tvmazeclient.data.remote.model.CharacterDto
import com.andrefpc.tvmazeclient.data.remote.model.ImageDto
import com.andrefpc.tvmazeclient.data.remote.model.PersonDto
import com.andrefpc.tvmazeclient.domain.model.Cast
import com.andrefpc.tvmazeclient.domain.model.Character
import com.andrefpc.tvmazeclient.domain.model.Image
import com.andrefpc.tvmazeclient.domain.model.Person

object CastMocks {
    val castDto = CastDto(
        person = PersonDto(
            id = 1,
            name = "Test Person",
            image = ImageDto(
                medium = "",
                original = ""
            )
        ),
        character = CharacterDto(
            id = 1,
            name = "Test Character"
        )
    )

    val cast = castDto.toDomain()
}