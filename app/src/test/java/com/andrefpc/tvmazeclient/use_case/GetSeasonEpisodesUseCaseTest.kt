package com.andrefpc.tvmazeclient.use_case

import com.andrefpc.tvmazeclient.domain.model.ApiError
import com.andrefpc.tvmazeclient.data.exception.EpisodesListNullException
import com.andrefpc.tvmazeclient.data.exception.EpisodesListRequestException
import com.andrefpc.tvmazeclient.data.exception.SeasonListNullException
import com.andrefpc.tvmazeclient.data.exception.SeasonListRequestException
import com.andrefpc.tvmazeclient.domain.use_case.GetEpisodesUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetSeasonEpisodesUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetSeasonsUseCase
import com.andrefpc.tvmazeclient.util.EpisodeMocks
import com.andrefpc.tvmazeclient.util.SeasonEpisodeMocks
import com.andrefpc.tvmazeclient.util.SeasonMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetSeasonEpisodesUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var getSeasonsUseCase: GetSeasonsUseCase

    @MockK
    lateinit var getEpisodesUseCase: GetEpisodesUseCase

    private lateinit var getSeasonEpisodesUseCase: GetSeasonEpisodesUseCase

    @Before
    fun setup() {
        getSeasonEpisodesUseCase = GetSeasonEpisodesUseCase(getSeasonsUseCase, getEpisodesUseCase)
    }

    @Test
    fun `should return season episodes list when both use cases are successful`() = runTest {
        // Given
        val id = 1
        val seasons = listOf(SeasonMocks.season)
        val episodes = listOf(EpisodeMocks.episode)
        val expected = listOf(SeasonEpisodeMocks.seasonEpisodeStatus)

        coEvery { getSeasonsUseCase(id) } returns seasons
        coEvery { getEpisodesUseCase(id) } returns episodes

        // When
        val result = getSeasonEpisodesUseCase(id)

        // Then
        assertEquals(expected, result)
        coVerify(exactly = 1) { getSeasonsUseCase(id) }
        coVerify(exactly = 1) { getEpisodesUseCase(id) }
    }

    @Test
    fun `should throw SeasonListNullException when seasons are null`() = runTest {
        // Given
        val id = 1
        coEvery { getSeasonsUseCase(id) } throws SeasonListNullException()
        coEvery { getEpisodesUseCase(id) } returns listOf()

        // When / Then
        assertFailsWith<SeasonListNullException> {
            getSeasonEpisodesUseCase(id)
        }
        coVerify(exactly = 1) { getSeasonsUseCase(id) }
    }

    @Test
    fun `should throw EpisodesListNullException when episodes are null`() = runTest {
        // Given
        val id = 1
        coEvery { getSeasonsUseCase(id) } returns listOf()
        coEvery { getEpisodesUseCase(id) } throws EpisodesListNullException()

        // When / Then
        assertFailsWith<EpisodesListNullException> {
            getSeasonEpisodesUseCase(id)
        }
        coVerify(exactly = 1) { getEpisodesUseCase(id) }
    }

    @Test
    fun `should throw SeasonListRequestException when seasons request fails`() = runTest {
        // Given
        val id = 1
        coEvery { getSeasonsUseCase(id) } throws SeasonListRequestException(ApiError("Error"))
        coEvery { getEpisodesUseCase(id) } returns listOf()

        // When / Then
        assertFailsWith<SeasonListRequestException> {
            getSeasonEpisodesUseCase(id)
        }
        coVerify(exactly = 1) { getSeasonsUseCase(id) }
    }

    @Test
    fun `should throw EpisodesListRequestException when episodes request fails`() = runTest {
        // Given
        val id = 1
        coEvery { getSeasonsUseCase(id) } returns listOf()
        coEvery { getEpisodesUseCase(id) } throws EpisodesListRequestException(ApiError("Error"))

        // When / Then
        assertFailsWith<EpisodesListRequestException> {
            getSeasonEpisodesUseCase(id)
        }
        coVerify(exactly = 1) { getEpisodesUseCase(id) }
    }
}