package com.andrefpc.tvmazeclient.presentation.model.handler

import com.andrefpc.tvmazeclient.domain.use_case.GetPersonShowsUseCase
import javax.inject.Inject

data class PersonDetailsUseCaseHandler @Inject constructor(
    val getPersonShows: GetPersonShowsUseCase
)
