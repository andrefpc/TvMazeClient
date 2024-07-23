package com.andrefpc.tvmazeclient.data.remote.model

import com.google.gson.annotations.SerializedName

data class PersonShowDto(
    @SerializedName("_embedded") var embedded: PersonShowEmbeddedDto
)