package com.andrefpc.tvmazeclient.core.room

import com.andrefpc.tvmazeclient.core.data.Show


interface ShowRoomRepository {
    /**
     * Get the favorite shows
     * @return List of [Show]
     */
    suspend fun getAll(): List<Show>
    /**
     * Iearch into the favorite shows
     * @param [term] to be searched
     * @return List of [Show]
     */
    suspend fun search(term: String): List<Show>
    /**
     * Insert the show in the database
     * @param [show] Show to be saved in the favorites
     */
    suspend fun insert(show: Show)
    /**
     * Remove the show from the database
     * @param [id] id of the show to be removed
     */
    suspend fun delete(id: Int)
    /**
     * Check if the show is in the favorites list
     * @param [id] id of the show to be checked
     */
    suspend fun isFavorite(id: Int): Boolean
}
