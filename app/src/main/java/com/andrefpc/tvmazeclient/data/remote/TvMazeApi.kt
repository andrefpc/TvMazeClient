package com.andrefpc.tvmazeclient.data.remote

import com.andrefpc.tvmazeclient.data.remote.model.CastDto
import com.andrefpc.tvmazeclient.data.remote.model.EpisodeDto
import com.andrefpc.tvmazeclient.data.remote.model.PersonDto
import com.andrefpc.tvmazeclient.data.remote.model.SearchDto
import com.andrefpc.tvmazeclient.data.remote.model.SeasonDto
import com.andrefpc.tvmazeclient.data.remote.model.ShowDto
import com.andrefpc.tvmazeclient.domain.model.Person
import com.andrefpc.tvmazeclient.data.remote.model.PersonShowDto
import com.andrefpc.tvmazeclient.data.remote.model.SearchPeopleDto
import com.andrefpc.tvmazeclient.domain.model.SearchPeople
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface TvMazeApi {
    /**
     * Get Shows
     */
    @GET("shows")
    suspend fun getShows(@QueryMap params: HashMap<String, String>): Response<List<ShowDto>>

    /**
     * Search Shows
     */
    @GET("search/shows")
    suspend fun search(@QueryMap params: HashMap<String, String>): Response<List<SearchDto>>

    /**
     * Get Seasons
     */
    @GET("shows/{id}/seasons")
    suspend fun getSeasons(@Path(value = "id") id: Int): Response<List<SeasonDto>>

    /**
     * Get Episodes
     */
    @GET("shows/{id}/episodes")
    suspend fun getEpisodes(@Path(value = "id") id: Int): Response<List<EpisodeDto>>

    /**
     * Get Cast
     */
    @GET("shows/{id}/cast")
    suspend fun getCast(@Path(value = "id") id: Int): Response<List<CastDto>>

    /**
     * Get Person Shows
     */
    @GET("people/{id}/castcredits?embed=show")
    suspend fun getPersonShows(@Path(value = "id") id: Int): Response<List<PersonShowDto>>

    /**
     * Get People
     */
    @GET("people")
    suspend fun getPeople(@QueryMap params: HashMap<String, String>): Response<List<PersonDto>>

    /**
     * Search People
     */
    @GET("search/people")
    suspend fun searchPeople(@QueryMap params: HashMap<String, String>): Response<List<SearchPeopleDto>>
}