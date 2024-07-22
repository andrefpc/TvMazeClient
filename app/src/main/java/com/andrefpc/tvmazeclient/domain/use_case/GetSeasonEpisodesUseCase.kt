package com.andrefpc.tvmazeclient.domain.use_case

import com.andrefpc.tvmazeclient.data.exception.SeasonListNullException
import com.andrefpc.tvmazeclient.data.exception.SeasonListRequestException
import com.andrefpc.tvmazeclient.domain.model.Episode
import com.andrefpc.tvmazeclient.domain.model.Season
import com.andrefpc.tvmazeclient.domain.model.SeasonEpisodeStatus
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