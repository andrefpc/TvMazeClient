package com.andrefpc.tvmazeclient.domain.use_case

import com.andrefpc.tvmazeclient.domain.exception.ShowListNullException
import com.andrefpc.tvmazeclient.domain.exception.ShowListRequestException
import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.domain.model.Show
import com.andrefpc.tvmazeclient.domain.repository.api.ShowRepository
import javax.inject.Inject

class GetShowsUseCase @Inject constructor(
    private val showRepository: ShowRepository
) {
    @Throws(ShowListNullException::class, ShowListRequestException::class)
    suspend operator fun invoke(page: Int = 0, searchTerm: String? = null): List<Show> {
        when (
            val result = searchTerm?.let {
                showRepository.searchShows(it)
            } ?: run {
                showRepository.getShows(page)
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