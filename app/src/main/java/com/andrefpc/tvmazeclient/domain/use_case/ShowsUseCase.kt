package com.andrefpc.tvmazeclient.domain.use_case

import javax.inject.Inject

data class ShowsUseCase @Inject constructor(
    val getShows: GetShowsUseCase
)
