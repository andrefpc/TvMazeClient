package com.andrefpc.tvmazeclient.core.data

import java.io.Serializable

data class Cast(
    var person: Person,
    var character: Character
) : Serializable
