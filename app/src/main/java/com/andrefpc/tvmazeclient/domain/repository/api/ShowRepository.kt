package com.andrefpc.tvmazeclient.domain.repository.api

import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.domain.model.Cast
import com.andrefpc.tvmazeclient.domain.model.Episode
import com.andrefpc.tvmazeclient.domain.model.Season
import com.andrefpc.tvmazeclient.domain.model.Show

interface ShowRepository {
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
}