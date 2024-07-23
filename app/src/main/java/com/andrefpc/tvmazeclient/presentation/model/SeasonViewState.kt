package com.andrefpc.tvmazeclient.presentation.model

import com.andrefpc.tvmazeclient.domain.model.Season

data class SeasonViewState(
    var id: Int,
    var number: Int,
    var episodeOrder: Int,
    var opened: Boolean
) {
    constructor(season: Season): this(
        id = season.id,
        number = season.number,
        episodeOrder = season.episodeOrder,
        opened = false
    )
}
