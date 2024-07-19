package com.andrefpc.tvmazeclient.domain.use_case

import javax.inject.Inject

data class ShowDetailsUseCase @Inject constructor(
    val getCast: GetCastUseCase,
    val getSeasonEpisodes: GetSeasonEpisodesUseCase,
    val getSeasons: GetSeasonsUseCase,
    val getEpisodes: GetEpisodesUseCase,
    val switchFavorite: SwitchFavoriteUseCase,
    val checkFavorite: CheckFavoriteUseCase
)
