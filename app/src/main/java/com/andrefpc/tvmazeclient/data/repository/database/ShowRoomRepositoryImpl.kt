package com.andrefpc.tvmazeclient.data.repository.database

import android.content.Context
import com.andrefpc.tvmazeclient.domain.repository.database.ShowRoomRepository
import com.andrefpc.tvmazeclient.data.local.ShowEntity
import com.andrefpc.tvmazeclient.data.local.ShowRoomDatabase
import com.andrefpc.tvmazeclient.domain.model.Image
import com.andrefpc.tvmazeclient.domain.model.Schedule
import com.andrefpc.tvmazeclient.domain.model.Show
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ShowRoomRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ShowRoomRepository {
    override suspend fun getAll(): List<Show> {
        val showDao = ShowRoomDatabase.getDatabase(context).showDao()
        return showDao.getAll().map {
            Show(
                id = it.id,
                name = it.name,
                image = Image(it.mediumImage, it.largeImage),
                schedule = Schedule(it.time, it.days),
                genres = it.genres,
                summary = it.summary,
                premiered = it.premiered,
                ended = it.ended
            )
        }
    }

    override suspend fun isFavorite(id: Int): Boolean {
        val showDao = ShowRoomDatabase.getDatabase(context).showDao()
        return showDao.get(id) != null
    }

    override suspend fun search(term: String): List<Show> {
        val showDao = ShowRoomDatabase.getDatabase(context).showDao()
        return showDao.search(term).map {
            Show(
                id = it.id,
                name = it.name,
                image = Image(it.mediumImage, it.largeImage),
                schedule = Schedule(it.time, it.days),
                genres = it.genres,
                summary = it.summary,
                premiered = it.premiered,
                ended = it.ended
            )
        }
    }

    override suspend fun insert(show: Show) {
        val showDao = ShowRoomDatabase.getDatabase(context).showDao()
        if (showDao.get(show.id) == null) {
            showDao.insert(
                ShowEntity(
                    id = show.id,
                    name = show.name,
                    mediumImage = show.image?.medium ?: "",
                    largeImage = show.image?.original ?: "",
                    time = show.schedule.time,
                    days = show.schedule.days,
                    genres = show.genres,
                    summary = show.summary,
                    premiered = show.premiered,
                    ended = show.ended
                )
            )
        }
    }

    override suspend fun delete(id: Int) {
        val showDao = ShowRoomDatabase.getDatabase(context).showDao()
        showDao.delete(id)
    }
}