package com.andrefpc.tvmazeclient.domain.use_case

import com.andrefpc.tvmazeclient.data.exception.SeasonListNullException
import com.andrefpc.tvmazeclient.data.exception.SeasonListRequestException
import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.domain.model.Season
import com.andrefpc.tvmazeclient.domain.repository.api.ShowRepository
import javax.inject.Inject

class GetSeasonsUseCase @Inject constructor(
    private val showRepository: ShowRepository
) {
    @Throws(SeasonListNullException::class, SeasonListRequestException::class)
    suspend operator fun invoke(id: Int): List<Season> {
        when (val result = showRepository.getSeasons(id)) {
            is ApiResult.Success -> {
                result.result?.let { list ->
                    return list
                } ?: kotlin.run {
                    throw SeasonListNullException()
                }
            }

            is ApiResult.Error -> {
                throw SeasonListRequestException(result.apiError)
            }
        }
    }
}