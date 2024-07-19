package com.andrefpc.tvmazeclient.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ShowDao {
    @Insert
    suspend fun insert(showEntity: ShowEntity): Long

    @Query("DELETE FROM show WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM show order by name")
    suspend fun getAll(): List<ShowEntity>

    @Query("SELECT * FROM show where name like '%' || :term || '%' order by name")
    suspend fun search(term: String): List<ShowEntity>

    @Query("SELECT * FROM show where id = :id")
    suspend fun get(id: Int): ShowEntity?
}
