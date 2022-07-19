package com.andrefpc.tvmazeclient.room

import com.andrefpc.tvmazeclient.data.Show


interface ShowRoomRepository {
    suspend fun getAll(): List<Show>
    suspend fun search(term: String): List<Show>
    suspend fun insert(show: Show)
    suspend fun delete(id: Int)
    suspend fun isFavorite(id: Int): Boolean
}
