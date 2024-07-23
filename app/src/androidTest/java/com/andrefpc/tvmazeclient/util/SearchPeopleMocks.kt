package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.data.remote.model.SearchPeopleDto

object SearchPeopleMocks {
    val searchPeopleDto = SearchPeopleDto(
        person = PersonMocks.personDto
    )
}