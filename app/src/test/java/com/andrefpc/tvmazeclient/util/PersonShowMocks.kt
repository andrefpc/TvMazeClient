package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.data.remote.model.PersonShowDto
import com.andrefpc.tvmazeclient.data.remote.model.PersonShowEmbeddedDto
import com.andrefpc.tvmazeclient.domain.model.PersonShow
import com.andrefpc.tvmazeclient.domain.model.PersonShowEmbedded

object PersonShowMocks {
    val personShowDto = PersonShowDto(
        embedded = PersonShowEmbeddedDto(
            show = ShowMocks.showDto
        )
    )

    val personShow = personShowDto.toDomain()
}