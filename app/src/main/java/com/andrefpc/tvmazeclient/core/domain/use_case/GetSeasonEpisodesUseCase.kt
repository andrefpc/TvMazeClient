package com.andrefpc.tvmazeclient.core.domain.use_case

import com.andrefpc.tvmazeclient.core.data.Episode
import com.andrefpc.tvmazeclient.core.data.Season
import com.andrefpc.tvmazeclient.core.data.SeasonEpisodeStatus
import com.andrefpc.tvmazeclient.core.domain.exception.SeasonListNullException
import com.andrefpc.tvmazeclient.core.domain.exception.SeasonListRequestException
import javax.inject.Inject

class GetSeasonEpisodesUseCase @Inject constructor(
    private val getSeasonsUseCase: GetSeasonsUseCase,
    private val getEpisodesUseCase: GetEpisodesUseCase
) {
    @Throws(SeasonListNullException::class, SeasonListRequestException::class)
    suspend operator fun invoke(id: Int): List<SeasonEpisodeStatus> {
        val seasons = getSeasonsUseCase(id)
        val episodes = getEpisodesUseCase(id)
        return joinedSeasonsAndEpisodes(seasons, episodes)
    }

    /**
     * Join Seasons and episodes to be used in the list
     */
    private fun joinedSeasonsAndEpisodes(
        seasons: List<Season>,
        episodes: List<Episode>
    ): List<SeasonEpisodeStatus> {
        val seasonEpisodeList: MutableList<SeasonEpisodeStatus> = arrayListOf()
        for (season in seasons) {
            val episodesOfSeason = episodes.filter { it.season == season.number }
            seasonEpisodeList.add(SeasonEpisodeStatus(season, false, episodesOfSeason))
        }

        return seasonEpisodeList
    }
}