package com.andrefpc.tvmazeclient.ui.compose.people.domain.use_case

import com.andrefpc.tvmazeclient.core.domain.use_case.GetPeopleUseCase
import javax.inject.Inject

data class PeopleUseCase @Inject constructor(
    val getPeople: GetPeopleUseCase,
)
