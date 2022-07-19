package com.andrefpc.tvmazeclient.data

import java.io.Serializable

data class Person(
    var id: Int,
    var name: String,
    var image: Image
): Serializable
