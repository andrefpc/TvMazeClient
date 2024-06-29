package com.andrefpc.tvmazeclient.core.domain.use_case

import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.core.domain.room.ShowRoomRepository
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val showRoomRepository: ShowRoomRepository
) {
    suspend operator fun invoke(term: String? = null): List<Show> {
        return term?.let {
            showRoomRepository.search(it)
        } ?: run {
            showRoomRepository.getAll()
        }
    }
}