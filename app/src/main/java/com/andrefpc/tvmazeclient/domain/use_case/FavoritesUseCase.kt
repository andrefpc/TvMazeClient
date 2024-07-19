package com.andrefpc.tvmazeclient.domain.use_case

import javax.inject.Inject

data class FavoritesUseCase @Inject constructor(
    val getFavorites: GetFavoritesUseCase,
    val deleteFavorite: DeleteFavoriteUseCase
)
