package com.andrefpc.tvmazeclient.data.remote.model

import com.andrefpc.tvmazeclient.domain.model.SearchPeople

data class SearchPeopleDto(
    var person: PersonDto
) {
    fun toDomain(): SearchPeople {
        return SearchPeople(person.toDomain())
    }
}
