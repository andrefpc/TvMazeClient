package com.andrefpc.tvmazeclient.domain.use_case

import com.andrefpc.tvmazeclient.domain.model.Show
import com.andrefpc.tvmazeclient.domain.repository.database.ShowRoomRepository
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