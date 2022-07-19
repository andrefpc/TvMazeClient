package com.andrefpc.tvmazeclient.repositories

import com.andrefpc.tvmazeclient.api.TvMazeApi
import com.andrefpc.tvmazeclient.data.*
import com.google.gson.Gson
import retrofit2.Response

class TvMazeRepositoryImpl(
    private val tvMazeApi: TvMazeApi
) : TvMazeRepository {
    override suspend fun getShows(page: Int): ApiResult<List<Show>> {
        try {
            val params = hashMapOf("page" to page.toString())
            val response: Response<List<Show>> = tvMazeApi.getShows(params)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val apiError: ApiError = Gson().fromJson(errorBody, ApiError::class.java)
                return ApiResult.Error(apiError)
            }

            response.body()?.let {
                return ApiResult.Success(it)
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
            val response: Response<List<Search>> = tvMazeApi.search(params)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val apiError: ApiError = Gson().fromJson(errorBody, ApiError::class.java)
                return ApiResult.Error(apiError)
            }

            response.body()?.let { list ->
                return ApiResult.Success(list.map { it.show })
            } ?: kotlin.run {
                return ApiResult.Error(ApiError())
            }
        } catch (e: Exception) {
            return ApiResult.Error(ApiError())
        }
    }

    override suspend fun getSeasons(id: Int): ApiResult<List<Season>> {
        try {
            val response: Response<List<Season>> = tvMazeApi.getSeasons(id)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val apiError: ApiError = Gson().fromJson(errorBody, ApiError::class.java)
                return ApiResult.Error(apiError)
            }

            response.body()?.let {
                return ApiResult.Success(it)
            } ?: kotlin.run {
                return ApiResult.Error(ApiError())
            }
        } catch (e: Exception) {
            return ApiResult.Error(ApiError())
        }
    }

    override suspend fun getEpisodes(id: Int): ApiResult<List<Episode>> {
        try {
            val response: Response<List<Episode>> = tvMazeApi.getEpisodes(id)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val apiError: ApiError = Gson().fromJson(errorBody, ApiError::class.java)
                return ApiResult.Error(apiError)
            }

            response.body()?.let {
                return ApiResult.Success(it)
            } ?: kotlin.run {
                return ApiResult.Error(ApiError())
            }
        } catch (e: Exception) {
            return ApiResult.Error(ApiError())
        }
    }

    override suspend fun getCast(id: Int): ApiResult<List<Cast>> {
        try {
            val response: Response<List<Cast>> = tvMazeApi.getCast(id)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val apiError: ApiError = Gson().fromJson(errorBody, ApiError::class.java)
                return ApiResult.Error(apiError)
            }

            response.body()?.let {
                return ApiResult.Success(it)
            } ?: kotlin.run {
                return ApiResult.Error(ApiError())
            }
        } catch (e: Exception) {
            return ApiResult.Error(ApiError())
        }
    }

    override suspend fun getPersonShows(id: Int): ApiResult<List<Show>> {
        try {
            val response: Response<List<PersonShow>> = tvMazeApi.getPersonShows(id)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val apiError: ApiError = Gson().fromJson(errorBody, ApiError::class.java)
                return ApiResult.Error(apiError)
            }

            response.body()?.let { personShow ->
                return ApiResult.Success(personShow.map { it.embedded.show })
            } ?: kotlin.run {
                return ApiResult.Error(ApiError())
            }
        } catch (e: Exception) {
            return ApiResult.Error(ApiError())
        }
    }
}