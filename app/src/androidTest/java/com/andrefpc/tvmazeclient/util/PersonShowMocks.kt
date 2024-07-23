package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.data.remote.model.PersonShowDto
import com.andrefpc.tvmazeclient.data.remote.model.PersonShowEmbeddedDto

object PersonShowMocks {
    val personShowDto = PersonShowDto(
        embedded = PersonShowEmbeddedDto(
            show = ShowMocks.showDto
        )
    )
}