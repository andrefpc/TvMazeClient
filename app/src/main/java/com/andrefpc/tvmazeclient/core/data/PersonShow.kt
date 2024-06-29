package com.andrefpc.tvmazeclient.core.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PersonShow(
    @SerializedName("_embedded") var embedded: PersonShowEmbedded
) : Serializable
