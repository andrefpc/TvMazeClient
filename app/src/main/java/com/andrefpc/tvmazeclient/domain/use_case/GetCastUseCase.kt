package com.andrefpc.tvmazeclient.domain.use_case

import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.domain.model.Cast
import com.andrefpc.tvmazeclient.data.exception.CastListNullException
import com.andrefpc.tvmazeclient.data.exception.CastListRequestException
import com.andrefpc.tvmazeclient.domain.repository.api.TvMazeRepository
import javax.inject.Inject

class GetCastUseCase @Inject constructor(
    private val tvMazeRepository: TvMazeRepository
) {
    @Throws(CastListNullException::class, CastListRequestException::class)
    suspend operator fun invoke(id: Int): List<Cast> {
        when (val result = tvMazeRepository.getCast(id)) {
            is ApiResult.Success -> {
                result.result?.let { list ->
                    return list
                } ?: kotlin.run {
                    throw CastListNullException()
                }
            }

            is ApiResult.Error -> {
                throw CastListRequestException(result.apiError)
            }
        }
    }
}