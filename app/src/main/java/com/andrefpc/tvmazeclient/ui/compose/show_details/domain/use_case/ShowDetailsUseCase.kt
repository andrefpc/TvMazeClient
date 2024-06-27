package com.andrefpc.tvmazeclient.ui.compose.show_details.domain.use_case

import com.andrefpc.tvmazeclient.core.domain.use_case.AddFavoriteUseCase
import com.andrefpc.tvmazeclient.core.domain.use_case.CheckFavoriteUseCase
import com.andrefpc.tvmazeclient.core.domain.use_case.GetCastUseCase
import com.andrefpc.tvmazeclient.core.domain.use_case.GetPersonShowsUseCase
import com.andrefpc.tvmazeclient.core.domain.use_case.GetSeasonEpisodesUseCase
import javax.inject.Inject

data class ShowDetailsUseCase @Inject constructor(
    val getCast: GetCastUseCase,
    val getSeasonEpisodes: GetSeasonEpisodesUseCase,
    val addFavorite: AddFavoriteUseCase,
    val checkFavorite: CheckFavoriteUseCase
)
