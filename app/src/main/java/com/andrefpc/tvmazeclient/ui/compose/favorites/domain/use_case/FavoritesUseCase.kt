package com.andrefpc.tvmazeclient.ui.compose.favorites.domain.use_case

import com.andrefpc.tvmazeclient.core.domain.use_case.DeleteFavoriteUseCase
import com.andrefpc.tvmazeclient.core.domain.use_case.GetFavoritesUseCase
import javax.inject.Inject

data class FavoritesUseCase @Inject constructor(
    val getFavorites: GetFavoritesUseCase,
    val deleteFavorite: DeleteFavoriteUseCase
)
