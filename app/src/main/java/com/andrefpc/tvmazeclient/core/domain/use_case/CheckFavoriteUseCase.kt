package com.andrefpc.tvmazeclient.core.domain.use_case

import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.core.domain.room.ShowRoomRepository
import javax.inject.Inject

class CheckFavoriteUseCase @Inject constructor(
    private val showRoomRepository: ShowRoomRepository
) {
    suspend operator fun invoke(id: Int): Boolean {
        return showRoomRepository.isFavorite(id)
    }
}