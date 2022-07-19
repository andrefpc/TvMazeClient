package com.andrefpc.tvmazeclient.repositories

import com.andrefpc.tvmazeclient.data.ApiResult
import com.andrefpc.tvmazeclient.data.Episode
import com.andrefpc.tvmazeclient.data.Season
import com.andrefpc.tvmazeclient.data.Show

interface TvMazeRepository {
    /**
     * Get Shows from the api
     * @param page Page of the api
     * @return List of [Show]
     */
    suspend fun getShows(page: Int): ApiResult<List<Show>>

    /**
     * Search shows Shows from the api
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
}