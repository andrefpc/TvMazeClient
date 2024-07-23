package com.andrefpc.tvmazeclient.data.remote.model

import com.andrefpc.tvmazeclient.domain.model.Schedule

data class ScheduleDto(
    var time: String,
    var days: ArrayList<String>
) {
    fun toDomain(): Schedule {
        return Schedule(time, days)
    }
}