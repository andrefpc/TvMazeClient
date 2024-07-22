package com.andrefpc.tvmazeclient.use_case

import com.andrefpc.tvmazeclient.data.exception.EpisodesListNullException
import com.andrefpc.tvmazeclient.data.exception.EpisodesListRequestException
import com.andrefpc.tvmazeclient.domain.model.ApiError
import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.domain.repository.api.ShowRepository
import com.andrefpc.tvmazeclient.domain.use_case.GetEpisodesUseCase
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
    lateinit var showRepository: ShowRepository

    private lateinit var getEpisodesUseCase: GetEpisodesUseCase

    @Before
    fun setup() {
        getEpisodesUseCase = GetEpisodesUseCase(showRepository)
    }

    @Test
    fun `should return episodes list when result is success`() = runTest {
        // Given
        val id = 1
        val episodesList = listOf(EpisodeMocks.episode)
        coEvery { showRepository.getEpisodes(id) } returns ApiResult.Success(episodesList)

        // When
        val result = getEpisodesUseCase(id)

        // Then
        assertEquals(episodesList, result)
        coVerify(exactly = 1) { showRepository.getEpisodes(id) }
    }

    @Test
    fun `should throw EpisodesListNullException when result is success but episodes list is null`() = runTest {
        // Given
        val id = 1
        coEvery { showRepository.getEpisodes(id) } returns ApiResult.Success(null)

        // When & Then
        assertFailsWith<EpisodesListNullException> {
            getEpisodesUseCase(id)
        }
        coVerify(exactly = 1) { showRepository.getEpisodes(id) }
    }

    @Test
    fun `should throw EpisodesListRequestException when result is error`() = runTest {
        // Given
        val id = 1
        val apiError = ApiError(message = "Error fetching episodes list")
        coEvery { showRepository.getEpisodes(id) } returns ApiResult.Error(apiError)

        // When & Then
        val exception = assertFailsWith<EpisodesListRequestException> {
            getEpisodesUseCase(id)
        }
        assertEquals(apiError, exception.apiError)
        coVerify(exactly = 1) { showRepository.getEpisodes(id) }
    }
}