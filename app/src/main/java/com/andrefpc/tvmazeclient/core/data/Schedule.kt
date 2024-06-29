package com.andrefpc.tvmazeclient.core.data

import java.io.Serializable

data class Schedule(
    var time: String,
    var days: ArrayList<String>
) : Serializable