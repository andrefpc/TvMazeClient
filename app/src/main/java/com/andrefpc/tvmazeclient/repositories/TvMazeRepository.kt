package com.andrefpc.tvmazeclient.repositories

import com.andrefpc.tvmazeclient.data.*

interface TvMazeRepository {
    /**
     * Get Shows from the api
     * @param page Page of the api
     * @return List of [Show]
     */
    suspend fun getShows(page: Int): ApiResult<List<Show>>

    /**
     * Search Shows from the api
     * @param term Search term
     * @return List of [Show]
     */
    suspend fun searchShows(term: String): ApiResult<List<Show>>

    /**
     * Get seasons of a specific show
     * @param id Identifier of the show
     * @return List of [Season]
     */
    suspend fun getSeasons(id: Int): ApiResult<List<Season>>

    /**
     * Get episodes of a specific show
     * @param id Identifier of the show
     * @return List of [Episode]
     */
    suspend fun getEpisodes(id: Int): ApiResult<List<Episode>>

    /**
     * Get the cast of a specific show
     * @param id Identifier of the show
     * @return List of [Cast]
     */
    suspend fun getCast(id: Int): ApiResult<List<Cast>>

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