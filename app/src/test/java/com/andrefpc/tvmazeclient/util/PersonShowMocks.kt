package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.core.data.PersonShow
import com.andrefpc.tvmazeclient.core.data.PersonShowEmbedded

object PersonShowMocks {
    val personShow = PersonShow(
        embedded = PersonShowEmbedded(
            show = ShowMocks.show
        )
    )
}