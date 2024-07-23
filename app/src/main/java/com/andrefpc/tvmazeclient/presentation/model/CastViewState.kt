package com.andrefpc.tvmazeclient.presentation.model

import com.andrefpc.tvmazeclient.domain.model.Cast

data class CastViewState(
    var person: PersonViewState,
    var character: CharacterViewState
) {
    constructor(cast: Cast) : this(
        PersonViewState(cast.person),
        CharacterViewState(cast.character)
    )
}
