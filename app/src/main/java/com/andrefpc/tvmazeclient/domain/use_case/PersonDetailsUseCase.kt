package com.andrefpc.tvmazeclient.domain.use_case

import javax.inject.Inject

data class PersonDetailsUseCase @Inject constructor(
    val getPersonShows: GetPersonShowsUseCase
)
