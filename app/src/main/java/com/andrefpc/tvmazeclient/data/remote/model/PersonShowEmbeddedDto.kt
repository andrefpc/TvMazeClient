package com.andrefpc.tvmazeclient.data.remote.model

import com.andrefpc.tvmazeclient.domain.model.PersonShowEmbedded

data class PersonShowEmbeddedDto(
    var show: ShowDto
) {
    fun toDomain(): PersonShowEmbedded {
        return PersonShowEmbedded(show.toDomain())
    }
}
