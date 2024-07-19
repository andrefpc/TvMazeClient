package com.andrefpc.tvmazeclient.data.remote.model

import com.andrefpc.tvmazeclient.domain.model.Cast
import java.io.Serializable

data class CastDto(
    var person: PersonDto,
    var character: CharacterDto
) : Serializable {
    fun toDomain(): Cast {
        return Cast(
            person = person.toDomain(),
            character = character.toDomain()
        )
    }
}
