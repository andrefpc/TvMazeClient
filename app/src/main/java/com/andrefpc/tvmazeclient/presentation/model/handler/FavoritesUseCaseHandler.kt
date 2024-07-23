package com.andrefpc.tvmazeclient.presentation.model.handler

import com.andrefpc.tvmazeclient.domain.use_case.DeleteFavoriteUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetFavoritesUseCase
import javax.inject.Inject

data class FavoritesUseCaseHandler @Inject constructor(
    val getFavorites: GetFavoritesUseCase,
    val deleteFavorite: DeleteFavoriteUseCase
)
