package com.andrefpc.tvmazeclient.use_case

import com.andrefpc.tvmazeclient.core.data.ApiError
import com.andrefpc.tvmazeclient.core.data.ApiResult
import com.andrefpc.tvmazeclient.core.domain.exception.EpisodesListNullException
import com.andrefpc.tvmazeclient.core.domain.exception.EpisodesListRequestException
import com.andrefpc.tvmazeclient.core.domain.repository.TvMazeRepository
import com.andrefpc.tvmazeclient.core.domain.use_case.GetEpisodesUseCase
import com.andrefpc.tvmazeclient.util.EpisodeMocks
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

class GetEpisodesUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var tvMazeRepository: TvMazeRepository

    private lateinit var getEpisodesUseCase: GetEpisodesUseCase

    @Before
    fun setup() {
        getEpisodesUseCase = GetEpisodesUseCase(tvMazeRepository)
    }

    @Test
    fun `should return episodes list when result is success`() = runTest {
        // Given
        val id = 1
        val episodesList = listOf(EpisodeMocks.episode)
        coEvery { tvMazeRepository.getEpisodes(id) } returns ApiResult.Success(episodesList)

        // When
        val result = getEpisodesUseCase(id)

        // Then
        assertEquals(episodesList, result)
        coVerify(exactly = 1) { tvMazeRepository.getEpisodes(id) }
    }

    @Test
    fun `should throw EpisodesListNullException when result is success but episodes list is null`() = runTest {
        // Given
        val id = 1
        coEvery { tvMazeRepository.getEpisodes(id) } returns ApiResult.Success(null)

        // When & Then
        assertFailsWith<EpisodesListNullException> {
            getEpisodesUseCase(id)
        }
        coVerify(exactly = 1) { tvMazeRepository.getEpisodes(id) }
    }

    @Test
    fun `should throw EpisodesListRequestException when result is error`() = runTest {
        // Given
        val id = 1
        val apiError = ApiError(message = "Error fetching episodes list")
        coEvery { tvMazeRepository.getEpisodes(id) } returns ApiResult.Error(apiError)

        // When & Then
        val exception = assertFailsWith<EpisodesListRequestException> {
            getEpisodesUseCase(id)
        }
        assertEquals(apiError, exception.apiError)
        coVerify(exactly = 1) { tvMazeRepository.getEpisodes(id) }
    }
}