package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.core.data.Cast
import com.andrefpc.tvmazeclient.core.data.Character
import com.andrefpc.tvmazeclient.core.data.Image
import com.andrefpc.tvmazeclient.core.data.Person
import com.andrefpc.tvmazeclient.core.data.PersonShow
import com.andrefpc.tvmazeclient.core.data.PersonShowEmbedded
import com.andrefpc.tvmazeclient.core.data.Schedule
import com.andrefpc.tvmazeclient.core.data.Show

object PersonShowMocks {
    val personShow = PersonShow(
        embedded = PersonShowEmbedded(
            show = ShowMocks.show
        )
    )
}