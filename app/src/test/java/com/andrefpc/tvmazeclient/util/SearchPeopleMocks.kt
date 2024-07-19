package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.data.remote.model.SearchPeopleDto
import com.andrefpc.tvmazeclient.domain.model.SearchPeople

object SearchPeopleMocks {
    val searchPeopleDto = SearchPeopleDto(
        person = PersonMocks.personDto
    )

    val searchPeople = searchPeopleDto.toDomain()
}