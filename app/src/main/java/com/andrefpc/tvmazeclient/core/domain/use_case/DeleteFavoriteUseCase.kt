package com.andrefpc.tvmazeclient.core.domain.use_case

import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.core.room.ShowRoomRepository
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    private val showRoomRepository: ShowRoomRepository
) {
    suspend operator fun invoke(show: Show): List<Show> {
        showRoomRepository.delete(show.id)
        return showRoomRepository.getAll()
    }
}