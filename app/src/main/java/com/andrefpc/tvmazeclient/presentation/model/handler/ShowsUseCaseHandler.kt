package com.andrefpc.tvmazeclient.presentation.model.handler

import com.andrefpc.tvmazeclient.domain.use_case.GetShowsUseCase
import javax.inject.Inject

data class ShowsUseCaseHandler @Inject constructor(
    val getShows: GetShowsUseCase
)
