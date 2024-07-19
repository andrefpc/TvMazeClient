package com.andrefpc.tvmazeclient.data.remote.model

import com.andrefpc.tvmazeclient.domain.model.Episode
import java.io.Serializable

data class EpisodeDto(
    var id: Int,
    var name: String,
    var number: Int,
    var season: Int,
    val summary: String,
    var image: ImageDto?,
    var airdate: String,
    var airtime: String,
    var runtime: Int
) : Serializable {
    fun toDomain(): Episode {
        return Episode(
            id,
            name,
            number,
            season,
            summary,
            image?.toDomain(),
            airdate,
            airtime,
            runtime
        )
    }
}