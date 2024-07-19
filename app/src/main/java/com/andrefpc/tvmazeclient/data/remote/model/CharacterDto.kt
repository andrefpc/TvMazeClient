package com.andrefpc.tvmazeclient.data.remote.model

import com.andrefpc.tvmazeclient.domain.model.Character

data class CharacterDto(
    var id: Int,
    var name: String,
) {
    fun toDomain(): Character {
        return Character(id, name)
    }
}
