package com.andrefpc.tvmazeclient.core.domain.use_case

import com.andrefpc.tvmazeclient.core.data.ApiResult
import com.andrefpc.tvmazeclient.core.data.Cast
import com.andrefpc.tvmazeclient.core.data.Season
import com.andrefpc.tvmazeclient.core.data.Show
import com.andrefpc.tvmazeclient.core.domain.exception.CastListNullException
import com.andrefpc.tvmazeclient.core.domain.exception.CastListRequestException
import com.andrefpc.tvmazeclient.core.domain.exception.PeopleListNullException
import com.andrefpc.tvmazeclient.core.domain.exception.PeopleListRequestException
import com.andrefpc.tvmazeclient.core.domain.exception.SeasonListNullException
import com.andrefpc.tvmazeclient.core.domain.exception.SeasonListRequestException
import com.andrefpc.tvmazeclient.core.domain.exception.ShowListNullException
import com.andrefpc.tvmazeclient.core.domain.exception.ShowListRequestException
import com.andrefpc.tvmazeclient.core.domain.repository.TvMazeRepository
import javax.inject.Inject

class GetSeasonsUseCase @Inject constructor(
    private val tvMazeRepository: TvMazeRepository
) {
    @Throws(SeasonListNullException::class, SeasonListRequestException::class)
    suspend operator fun invoke(id: Int): List<Season> {
        when (val result = tvMazeRepository.getSeasons(id)) {
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