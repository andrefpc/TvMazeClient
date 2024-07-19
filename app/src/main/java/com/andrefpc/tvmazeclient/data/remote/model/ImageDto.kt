package com.andrefpc.tvmazeclient.data.remote.model

import com.andrefpc.tvmazeclient.domain.model.Image
import java.io.Serializable

data class ImageDto(
    var medium: String?,
    var original: String?
) : Serializable {
    fun toDomain(): Image {
        return Image(medium, original)
    }
}
