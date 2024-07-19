package com.andrefpc.tvmazeclient.data.remote.model

import com.andrefpc.tvmazeclient.domain.model.Person

data class PersonDto(
    var id: Int,
    var name: String,
    var image: ImageDto?
) {
    fun toDomain(): Person {
        return Person(id, name, image?.toDomain())
    }
}
