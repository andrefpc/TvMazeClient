package com.andrefpc.tvmazeclient.data.remote.model

import com.andrefpc.tvmazeclient.domain.model.Search
import com.andrefpc.tvmazeclient.domain.model.Show

data class SearchDto(
    var show: ShowDto
) {
    fun toShowDomain(): Show {
        return show.toDomain()
    }
}
