package com.andrefpc.tvmazeclient.presentation.model.handler

import com.andrefpc.tvmazeclient.domain.use_case.CheckFavoriteUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetCastUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetEpisodesUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetSeasonEpisodesUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetSeasonsUseCase
import com.andrefpc.tvmazeclient.domain.use_case.SwitchFavoriteUseCase
import javax.inject.Inject

data class ShowDetailsUseCaseHandler @Inject constructor(
    val getCast: GetCastUseCase,
    val getSeasonEpisodes: GetSeasonEpisodesUseCase,
    val getSeasons: GetSeasonsUseCase,
    val getEpisodes: GetEpisodesUseCase,
    val switchFavorite: SwitchFavoriteUseCase,
    val checkFavorite: CheckFavoriteUseCase
)
