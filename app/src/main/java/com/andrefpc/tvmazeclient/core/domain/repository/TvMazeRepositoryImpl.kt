package com.andrefpc.tvmazeclient.core.domain.repository

import com.andrefpc.tvmazeclient.api.TvMazeApi
import com.andrefpc.tvmazeclient.core.data.ApiError
import com.andrefpc.tvmazeclient.core.data.ApiResult
import com.andrefpc.tvmazeclient.core.data.Cast
import com.andrefpc.tvmazeclient.core.data.Episode
import com.andrefpc.tvmazeclient.core.data.Person
import com.andrefpc.tvmazeclient.core.data.PersonShow
import com.andrefpc.tvmazeclient.core.data.Search
import com.andrefpc.tvmazeclient.core.data.SearchPeople
import com.andrefpc.tvmazeclient.core.data.Season
import com.andrefpc.tvmazeclient.core.data.Show
import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject

class TvMazeRepositoryImpl @Inject constructor(
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
                return if (personShow.isEmpty()) ApiResult.Success(emptyList())
                else ApiResult.Success(personShow.map { it.embedded.show })
            } ?: kotlin.run {
                return ApiResult.Error(ApiError())
            }
        } catch (e: Exception) {
            return ApiResult.Error(ApiError())
        }
    }

    override suspend fun getPeople(page: Int): ApiResult<List<Person>> {
        try {
            val params = hashMapOf("page" to page.toString())
            val response: Response<List<Person>> = tvMazeApi.getPeople(params)
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

    override suspend fun searchPeople(term: String): ApiResult<List<Person>> {
        try {
            val params = hashMapOf("q" to term)
            val response: Response<List<SearchPeople>> = tvMazeApi.searchPeople(params)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val apiError: ApiError = Gson().fromJson(errorBody, ApiError::class.java)
                return ApiResult.Error(apiError)
            }

            response.body()?.let { list ->
                return ApiResult.Success(list.map { it.person })
            } ?: kotlin.run {
                return ApiResult.Error(ApiError())
            }
        } catch (e: Exception) {
            return ApiResult.Error(ApiError())
        }
    }
}