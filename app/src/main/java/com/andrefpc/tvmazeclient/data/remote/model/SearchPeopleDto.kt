package com.andrefpc.tvmazeclient.data.remote.model

import com.andrefpc.tvmazeclient.domain.model.Person

data class SearchPeopleDto(
    var person: PersonDto
) {
    fun toDomain(): Person {
        return person.toDomain()
    }
}
