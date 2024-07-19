package com.andrefpc.tvmazeclient.data.remote.model

import com.andrefpc.tvmazeclient.domain.model.Image
import com.andrefpc.tvmazeclient.domain.model.Schedule
import com.andrefpc.tvmazeclient.domain.model.Show
import java.io.Serializable

data class ShowDto(
    var id: Int,
    var name: String,
    var image: Image?,
    var schedule: Schedule,
    var genres: ArrayList<String>,
    var summary: String,
    var premiered: String?,
    var ended: String?
) : Serializable {
    fun toDomain(): Show {
        return Show(id, name, image, schedule, genres, summary, premiered, ended)
    }

}
