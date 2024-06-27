package com.andrefpc.tvmazeclient.ui.compose.person_details.domain.use_case

import com.andrefpc.tvmazeclient.core.domain.use_case.GetPersonShowsUseCase
import javax.inject.Inject

data class PersonDetailsUseCase @Inject constructor(
    val getPersonShows: GetPersonShowsUseCase
)
