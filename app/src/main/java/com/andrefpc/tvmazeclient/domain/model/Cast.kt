package com.andrefpc.tvmazeclient.domain.model

import java.io.Serializable

data class Cast(
    var person: Person,
    var character: Character
) : Serializable
