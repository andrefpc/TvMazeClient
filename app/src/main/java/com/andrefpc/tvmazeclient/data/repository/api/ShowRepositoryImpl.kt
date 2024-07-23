package com.andrefpc.tvmazeclient.data.repository.api

import com.andrefpc.tvmazeclient.data.remote.TvMazeApi
import com.andrefpc.tvmazeclient.data.remote.model.CastDto
import com.andrefpc.tvmazeclient.data.remote.model.EpisodeDto
import com.andrefpc.tvmazeclient.data.remote.model.SearchShowDto
import com.andrefpc.tvmazeclient.data.remote.model.SeasonDto
import com.andrefpc.tvmazeclient.data.remote.model.ShowDto
import com.andrefpc.tvmazeclient.domain.model.ApiError
import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.domain.model.Cast
import com.andrefpc.tvmazeclient.domain.model.Episode
import com.andrefpc.tvmazeclient.domain.model.Season
import com.andrefpc.tvmazeclient.domain.model.Show
import com.andrefpc.tvmazeclient.domain.repository.api.ShowRepository
import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject

class ShowRepositoryImpl @Inject constructor(
    private val tvMazeApi: TvMazeApi
) : ShowRepository {
    override suspend fun getShows(page: Int): ApiResult<List<Show>> {
        try {
            val params = hashMapOf("page" to page.toString())
            val response: Response<List<ShowDto>> = tvMazeApi.getShows(params)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val apiError: ApiError = Gson().fromJson(errorBody, ApiError::class.java)
                return ApiResult.Error(apiError)
            }

            response.body()?.let { showsList ->
                return ApiResult.Success(showsList.map { it.toDomain() })
            } ?: kotlin.run {
                return ApiResult.Error(ApiError())
            }
        } catch (e: Exception) {
            return ApiResult.Error(ApiError())
        }
    }

    override suspend fun searchShows(term: String): ApiResult<List<Show>> {
        try {
            val params = hashMapOf("q" to term)
            val response: Response<List<SearchShowDto>> = tvMazeApi.search(params)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val apiError: ApiError = Gson().fromJson(errorBody, ApiError::class.java)
                return ApiResult.Error(apiError)
            }

            response.body()?.let { list ->
                return ApiResult.Success(list.map { it.toShowDomain() })
            } ?: kotlin.run {
                return ApiResult.Error(ApiError())
            }
        } catch (e: Exception) {
            return ApiResult.Error(ApiError())
        }
    }

    override suspend fun getSeasons(id: Int): ApiResult<List<Season>> {
        try {
            val response: Response<List<SeasonDto>> = tvMazeApi.getSeasons(id)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val apiError: ApiError = Gson().fromJson(errorBody, ApiError::class.java)
                return ApiResult.Error(apiError)
            }

            response.body()?.let { list ->
                return ApiResult.Success(list.map { it.toDomain() })
            } ?: kotlin.run {
                return ApiResult.Error(ApiError())
            }
        } catch (e: Exception) {
            return ApiResult.Error(ApiError())
        }
    }

    override suspend fun getEpisodes(id: Int): ApiResult<List<Episode>> {
        try {
            val response: Response<List<EpisodeDto>> = tvMazeApi.getEpisodes(id)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val apiError: ApiError = Gson().fromJson(errorBody, ApiError::class.java)
                return ApiResult.Error(apiError)
            }

            response.body()?.let { list ->
                return ApiResult.Success(list.map { it.toDomain() })
            } ?: kotlin.run {
                return ApiResult.Error(ApiError())
            }
        } catch (e: Exception) {
            return ApiResult.Error(ApiError())
        }
    }

    override suspend fun getCast(id: Int): ApiResult<List<Cast>> {
        try {
            val response: Response<List<CastDto>> = tvMazeApi.getCast(id)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val apiError: ApiError = Gson().fromJson(errorBody, ApiError::class.java)
                return ApiResult.Error(apiError)
            }

            response.body()?.let { list ->
                return ApiResult.Success(list.map { it.toDomain() })
            } ?: kotlin.run {
                return ApiResult.Error(ApiError())
            }
        } catch (e: Exception) {
            return ApiResult.Error(ApiError())
        }
    }
}