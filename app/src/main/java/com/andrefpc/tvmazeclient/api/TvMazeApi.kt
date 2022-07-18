package com.andrefpc.tvmazeclient.api

import com.andrefpc.tvmazeclient.data.Episode
import com.andrefpc.tvmazeclient.data.Season
import com.andrefpc.tvmazeclient.data.Show
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
     * Get Seasons
     */
    @GET("shows/{id}/seasons")
    suspend fun getSeasons(@Path(value = "id") id: Int): Response<List<Season>>

    /**
     * Get Episodes
     */
    @GET("shows/{id}/episodes")
    suspend fun getEpisodes(@Path(value = "id") id: Int): Response<List<Episode>>
}