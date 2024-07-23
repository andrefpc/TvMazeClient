package com.andrefpc.tvmazeclient.presentation.model

import android.os.Parcelable
import com.andrefpc.tvmazeclient.domain.model.Image
import com.andrefpc.tvmazeclient.domain.model.Schedule
import com.andrefpc.tvmazeclient.domain.model.Show
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShowViewState(
    var id: Int,
    var name: String,
    var thumb: String?,
    var image: String?,
    var time: String,
    var days: ArrayList<String>,
    var genres: ArrayList<String>,
    var summary: String,
    var premiered: String?,
    var ended: String?
) : Parcelable {
    constructor(show: Show) : this(
        id = show.id,
        name = show.name,
        thumb = show.image?.medium,
        image = show.image?.original,
        time = show.schedule.time,
        days = show.schedule.days,
        genres = show.genres,
        summary = show.summary,
        premiered = show.premiered,
        ended = show.ended
    )

    fun toDomain(): Show {
        return Show(
            id = id,
            name = name,
            image = Image(
                medium = thumb,
                original = image
            ),
            schedule = Schedule(
                time = time,
                days = days
            ),
            genres = genres,
            summary = summary,
            premiered = premiered,
            ended = ended
        )
    }
}
