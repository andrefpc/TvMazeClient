package com.andrefpc.tvmazeclient.data

import java.io.Serializable

data class Cast(
    var person: Person,
    var character: Character
): Serializable
