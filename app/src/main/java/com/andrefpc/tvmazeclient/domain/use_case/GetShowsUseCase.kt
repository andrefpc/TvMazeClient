package com.andrefpc.tvmazeclient.domain.use_case

import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.domain.model.Show
import com.andrefpc.tvmazeclient.data.exception.ShowListNullException
import com.andrefpc.tvmazeclient.data.exception.ShowListRequestException
import com.andrefpc.tvmazeclient.domain.repository.api.TvMazeRepository
import javax.inject.Inject

class GetShowsUseCase @Inject constructor(
    private val tvMazeRepository: TvMazeRepository
) {
    @Throws(ShowListNullException::class, ShowListRequestException::class)
    suspend operator fun invoke(page: Int = 0, searchTerm: String? = null): List<Show> {
        when (
            val result = searchTerm?.let {
                tvMazeRepository.searchShows(it)
            } ?: run {
                tvMazeRepository.getShows(page)
            }
        ) {
            is ApiResult.Success -> {
                result.result?.let { list ->
                    return list
                } ?: kotlin.run {
                    throw ShowListNullException()
                }
            }

            is ApiResult.Error -> {
                throw ShowListRequestException(result.apiError)
            }
        }
    }
}