package com.andrefpc.tvmazeclient.presentation.model.handler

import com.andrefpc.tvmazeclient.domain.use_case.GetPeopleUseCase
import javax.inject.Inject

data class PeopleUseCaseHandler @Inject constructor(
    val getPeople: GetPeopleUseCase,
)
