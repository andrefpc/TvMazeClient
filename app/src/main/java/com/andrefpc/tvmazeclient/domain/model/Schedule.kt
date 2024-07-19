package com.andrefpc.tvmazeclient.domain.model

import java.io.Serializable

data class Schedule(
    var time: String,
    var days: ArrayList<String>
) : Serializable