package com.andrefpc.tvmazeclient.repositories

import com.andrefpc.tvmazeclient.api.TvMazeApi
import com.andrefpc.tvmazeclient.data.ApiResult
import com.andrefpc.tvmazeclient.data.Episode
import com.andrefpc.tvmazeclient.data.Season
import com.andrefpc.tvmazeclient.data.Show
import retrofit2.Response

class TvMazeRepositoryImpl(
    private val tvMazeApi: TvMazeApi
) : TvMazeRepository {
    override suspend fun getShows(page: Int): ApiResult<List<Show>> {
        try {
            val params = hashMapOf("page" to page.toString())
            val response: Response<List<Show>> = tvMazeApi.getShows(params)
            if (!response.isSuccessful) {
                return ApiResult.Error
            }

            response.body()?.let {
                return ApiResult.Success(it)
            } ?: kotlin.run {
                return ApiResult.Error
            }
        } catch (e: Exception) {
            return ApiResult.Error
        }
    }

    override suspend fun getSeasons(id: Int): ApiResult<List<Season>> {
        try {
            val response: Response<List<Season>> = tvMazeApi.getSeasons(id)
            if (!response.isSuccessful) {
                return ApiResult.Error
            }

            response.body()?.let {
                return ApiResult.Success(it)
            } ?: kotlin.run {
                return ApiResult.Error
            }
        } catch (e: Exception) {
            return ApiResult.Error
        }
    }

    override suspend fun getEpisode(id: Int): ApiResult<List<Episode>> {
        try {
            val response: Response<List<Episode>> = tvMazeApi.getEpisodes(id)
            if (!response.isSuccessful) {
                return ApiResult.Error
            }

            response.body()?.let {
                return ApiResult.Success(it)
            } ?: kotlin.run {
                return ApiResult.Error
            }
        } catch (e: Exception) {
            return ApiResult.Error
        }
    }
}