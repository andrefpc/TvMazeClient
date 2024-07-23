package com.andrefpc.tvmazeclient.presentation.model

import com.andrefpc.tvmazeclient.domain.model.Character

data class CharacterViewState(
    var id: Int,
    var name: String,
) {
    constructor(character: Character): this(
        id = character.id,
        name = character.name
    )
}
