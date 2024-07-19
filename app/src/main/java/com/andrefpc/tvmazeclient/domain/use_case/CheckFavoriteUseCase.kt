package com.andrefpc.tvmazeclient.domain.use_case

import com.andrefpc.tvmazeclient.domain.repository.database.ShowRoomRepository
import javax.inject.Inject

class CheckFavoriteUseCase @Inject constructor(
    private val showRoomRepository: ShowRoomRepository
) {
    suspend operator fun invoke(id: Int): Boolean {
        return showRoomRepository.isFavorite(id)
    }
}