package com.andrefpc.tvmazeclient.presentation.model

import android.os.Parcelable
import com.andrefpc.tvmazeclient.domain.model.Person
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonViewState(
    var id: Int,
    var name: String,
    var image: String?,
    var thumb: String?
): Parcelable {
    constructor(person: Person): this(
        id = person.id,
        name = person.name,
        image = person.image?.original,
        thumb = person.image?.medium
    )
}
