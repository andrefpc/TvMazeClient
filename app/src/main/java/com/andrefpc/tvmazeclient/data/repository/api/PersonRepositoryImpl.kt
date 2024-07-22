package com.andrefpc.tvmazeclient.data.repository.api

import com.andrefpc.tvmazeclient.data.remote.TvMazeApi
import com.andrefpc.tvmazeclient.data.remote.model.PersonDto
import com.andrefpc.tvmazeclient.data.remote.model.PersonShowDto
import com.andrefpc.tvmazeclient.data.remote.model.SearchPeopleDto
import com.andrefpc.tvmazeclient.domain.model.ApiError
import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.domain.model.Person
import com.andrefpc.tvmazeclient.domain.model.Show
import com.andrefpc.tvmazeclient.domain.repository.api.PersonRepository
import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val tvMazeApi: TvMazeApi
) : PersonRepository {
    override suspend fun getPersonShows(id: Int): ApiResult<List<Show>> {
        try {
            val response: Response<List<PersonShowDto>> = tvMazeApi.getPersonShows(id)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val apiError: ApiError = Gson().fromJson(errorBody, ApiError::class.java)
                return ApiResult.Error(apiError)
            }

            response.body()?.let { personShows ->
                return if (personShows.isEmpty()) ApiResult.Success(emptyList())
                else ApiResult.Success(personShows.map { it.embedded.show.toDomain() })
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
            val response: Response<List<PersonDto>> = tvMazeApi.getPeople(params)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val apiError: ApiError = Gson().fromJson(errorBody, ApiError::class.java)
                return ApiResult.Error(apiError)
            }

            response.body()?.let { list ->
                return ApiResult.Success( list.map { it.toDomain() })
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
            val response: Response<List<SearchPeopleDto>> = tvMazeApi.searchPeople(params)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val apiError: ApiError = Gson().fromJson(errorBody, ApiError::class.java)
                return ApiResult.Error(apiError)
            }

            response.body()?.let { list ->
                return ApiResult.Success(list.map { it.person.toDomain() })
            } ?: kotlin.run {
                return ApiResult.Error(ApiError())
            }
        } catch (e: Exception) {
            return ApiResult.Error(ApiError())
        }
    }
}