package com.andrefpc.tvmazeclient.room

import android.content.Context
import androidx.room.*
import com.andrefpc.tvmazeclient.BuildConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@Database(
    entities = [ShowEntity::class],
    version = BuildConfig.VERSION_CODE,
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
