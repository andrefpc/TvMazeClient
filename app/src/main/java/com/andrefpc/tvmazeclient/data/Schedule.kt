package com.andrefpc.tvmazeclient.data

import java.io.Serializable

data class Schedule(
    var time: String,
    var days: ArrayList<String>
): Serializable