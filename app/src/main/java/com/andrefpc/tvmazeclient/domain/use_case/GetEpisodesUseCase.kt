package com.andrefpc.tvmazeclient.domain.use_case

import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.domain.model.Episode
import com.andrefpc.tvmazeclient.data.exception.EpisodesListNullException
import com.andrefpc.tvmazeclient.data.exception.EpisodesListRequestException
import com.andrefpc.tvmazeclient.domain.repository.api.TvMazeRepository
import javax.inject.Inject

class GetEpisodesUseCase @Inject constructor(
    private val tvMazeRepository: TvMazeRepository
) {
    @Throws(EpisodesListNullException::class, EpisodesListRequestException::class)
    suspend operator fun invoke(id: Int): List<Episode> {
        when (val result = tvMazeRepository.getEpisodes(id)) {
            is ApiResult.Success -> {
                result.result?.let { list ->
                    return list
                } ?: kotlin.run {
                    throw EpisodesListNullException()
                }
            }

            is ApiResult.Error -> {
                throw EpisodesListRequestException(result.apiError)
            }
        }
    }
}