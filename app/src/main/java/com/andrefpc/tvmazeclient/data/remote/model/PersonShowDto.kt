package com.andrefpc.tvmazeclient.data.remote.model

import com.andrefpc.tvmazeclient.domain.model.PersonShow
import com.google.gson.annotations.SerializedName

data class PersonShowDto(
    @SerializedName("_embedded") var embedded: PersonShowEmbeddedDto
) {
    fun toDomain(): PersonShow {
        return PersonShow(embedded.toDomain())
    }
}
