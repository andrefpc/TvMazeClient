package com.andrefpc.tvmazeclient.data.remote.model

import com.andrefpc.tvmazeclient.domain.model.Season
import java.io.Serializable

data class SeasonDto(
    var id: Int,
    var number: Int,
    var episodeOrder: Int,
    var opened: Boolean
) : Serializable {
    fun toDomain(): Season {
        return Season(id, number, episodeOrder, opened)
    }
}
