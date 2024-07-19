package com.andrefpc.tvmazeclient.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PersonShow(
    @SerializedName("_embedded") var embedded: PersonShowEmbedded
) : Serializable
