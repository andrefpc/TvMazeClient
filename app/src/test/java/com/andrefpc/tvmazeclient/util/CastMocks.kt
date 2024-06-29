package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.core.data.Cast
import com.andrefpc.tvmazeclient.core.data.Character
import com.andrefpc.tvmazeclient.core.data.Image
import com.andrefpc.tvmazeclient.core.data.Person

object CastMocks {
    val cast = Cast(
        person = Person(
            id = 1,
            name = "Test Person",
            image = Image(
                medium = "",
                original = ""
            )
        ),
        character = Character(
            id = 1,
            name = "Test Character"
        )
    )
}