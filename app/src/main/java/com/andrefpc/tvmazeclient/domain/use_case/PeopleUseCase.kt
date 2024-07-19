package com.andrefpc.tvmazeclient.domain.use_case

import javax.inject.Inject

data class PeopleUseCase @Inject constructor(
    val getPeople: GetPeopleUseCase,
)
