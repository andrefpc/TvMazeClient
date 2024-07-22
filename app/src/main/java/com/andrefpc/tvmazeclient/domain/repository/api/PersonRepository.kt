package com.andrefpc.tvmazeclient.domain.repository.api

import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.domain.model.Person
import com.andrefpc.tvmazeclient.domain.model.Show

interface PersonRepository {
    /**
     * Get the shows of a person
     * @param id Identifier of the person
     * @return List of [Show]
     */
    suspend fun getPersonShows(id: Int): ApiResult<List<Show>>

    /**
     * Get People from the api
     * @param page Page of the api
     * @return List of [Person]
     */
    suspend fun getPeople(page: Int): ApiResult<List<Person>>

    /**
     * Search People from the api
     * @param term Search term
     * @return List of [Person]
     */
    suspend fun searchPeople(term: String): ApiResult<List<Person>>
}