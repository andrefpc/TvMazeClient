package com.andrefpc.tvmazeclient.data.remote.model

import com.andrefpc.tvmazeclient.domain.model.Show

data class SearchShowDto(
    var show: ShowDto
) {
    fun toShowDomain(): Show {
        return show.toDomain()
    }
}
