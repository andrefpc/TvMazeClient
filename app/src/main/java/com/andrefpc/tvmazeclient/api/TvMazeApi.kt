package com.andrefpc.tvmazeclient.api

import com.andrefpc.tvmazeclient.data.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface TvMazeApi {
    /**
     * Get Shows
     */
    @GET("shows")
    suspend fun getShows(@QueryMap params: HashMap<String, String>): Response<List<Show>>

    /**
     * Search Shows
     */
    @GET("search/shows")
    suspend fun search(@QueryMap params: HashMap<String, String>): Response<List<Search>>

    /**
     * Get Seasons
     */
    @GET("shows/{id}/seasons")
    suspend fun getSeasons(@Path(value = "id") id: Int): Response<List<Season>>

    /**
     * Get Episodes
     */
    @GET("shows/{id}/episodes")
    suspend fun getEpisodes(@Path(value = "id") id: Int): Response<List<Episode>>

    /**
     * Get Cast
     */
    @GET("shows/{id}/cast")
    suspend fun getCast(@Path(value = "id") id: Int): Response<List<Cast>>

    /**
     * Get Person Shows
     */
    @GET("people/{id}/castcredits?embed=show")
    suspend fun getPersonShows(@Path(value = "id") id: Int): Response<List<PersonShow>>

    /**
     * Get People
     */
    @GET("people")
    suspend fun getPeople(@QueryMap params: HashMap<String, String>): Response<List<Person>>

    /**
     * Search People
     */
    @GET("search/people")
    suspend fun searchPeople(@QueryMap params: HashMap<String, String>): Response<List<SearchPeople>>
}