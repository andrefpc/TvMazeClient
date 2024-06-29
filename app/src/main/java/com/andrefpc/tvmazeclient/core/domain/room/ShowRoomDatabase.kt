package com.andrefpc.tvmazeclient.core.domain.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@Database(
    entities = [ShowEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(value = [ShowRoomDatabase.Converters::class])
abstract class ShowRoomDatabase : RoomDatabase() {
    abstract fun showDao(): ShowDao

    companion object {
        @Volatile
        private var INSTANCE: ShowRoomDatabase? = null

        fun getDatabase(context: Context): ShowRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShowRoomDatabase::class.java,
                    "show_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }

    object Converters {
        @TypeConverter
        fun fromString(value: String): ArrayList<String> {
            val listType: Type = object : TypeToken<ArrayList<String>>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromArrayList(list: ArrayList<String>): String {
            val gson = Gson()
            return gson.toJson(list)
        }
    }
}
