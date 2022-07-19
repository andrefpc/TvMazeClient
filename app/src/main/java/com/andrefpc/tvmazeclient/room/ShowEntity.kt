package com.andrefpc.tvmazeclient.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "show")
data class ShowEntity(
    @PrimaryKey @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "medium_image") var mediumImage: String,
    @ColumnInfo(name = "large_image") var largeImage: String,
    @ColumnInfo(name = "time") var time: String,
    @ColumnInfo(name = "days") var days: ArrayList<String>,
    @ColumnInfo(name = "genres") var genres: ArrayList<String>,
    @ColumnInfo(name = "summary") var summary: String,
    @ColumnInfo(name = "premiered") var premiered: String?,
    @ColumnInfo(name = "ended") var ended: String?
) : Serializable
