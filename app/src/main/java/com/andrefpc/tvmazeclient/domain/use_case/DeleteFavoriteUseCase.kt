package com.andrefpc.tvmazeclient.domain.use_case

import com.andrefpc.tvmazeclient.domain.model.Show
import com.andrefpc.tvmazeclient.domain.repository.database.ShowRoomRepository
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    private val showRoomRepository: ShowRoomRepository
) {
    suspend operator fun invoke(show: Show): List<Show> {
        showRoomRepository.delete(show.id)
        return showRoomRepository.getAll()
    }
}