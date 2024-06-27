package com.andrefpc.tvmazeclient.ui.compose.shows.domain_use_case

import com.andrefpc.tvmazeclient.core.domain.use_case.GetShowsUseCase
import javax.inject.Inject

data class ShowsUseCase @Inject constructor(
    val getShows: GetShowsUseCase
)
