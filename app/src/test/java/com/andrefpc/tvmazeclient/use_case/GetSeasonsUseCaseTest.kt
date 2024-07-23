package com.andrefpc.tvmazeclient.use_case

import com.andrefpc.tvmazeclient.domain.exception.SeasonListNullException
import com.andrefpc.tvmazeclient.domain.exception.SeasonListRequestException
import com.andrefpc.tvmazeclient.domain.model.ApiError
import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.domain.model.Season
import com.andrefpc.tvmazeclient.domain.repository.api.ShowRepository
import com.andrefpc.tvmazeclient.domain.use_case.GetSeasonsUseCase
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

class GetSeasonsUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var showRepository: ShowRepository

    private lateinit var getSeasonsUseCase: GetSeasonsUseCase

    @Before
    fun setup() {
        getSeasonsUseCase = GetSeasonsUseCase(showRepository)
    }

    @Test
    fun `should return season list when repository call is successful`() = runTest {
        // Given
        val id = 1
        val seasons = listOf(SeasonMocks.season)
        val apiResult = ApiResult.Success(seasons)

        coEvery { showRepository.getSeasons(id) } returns apiResult

        // When
        val result = getSeasonsUseCase(id)

        // Then
        assertEquals(seasons, result)
        coVerify(exactly = 1) { showRepository.getSeasons(id) }
    }

    @Test
    fun `should throw SeasonListNullException when result is null`() = runTest {
        // Given
        val id = 1
        val apiResult = ApiResult.Success<List<Season>>(null)

        coEvery { showRepository.getSeasons(id) } returns apiResult

        // When / Then
        assertFailsWith<SeasonListNullException> {
            getSeasonsUseCase(id)
        }
        coVerify(exactly = 1) { showRepository.getSeasons(id) }
    }

    @Test
    fun `should throw SeasonListRequestException when repository call fails`() = runTest {
        // Given
        val id = 1
        val apiError = ApiError("Error")
        val apiResult = ApiResult.Error(apiError)

        coEvery { showRepository.getSeasons(id) } returns apiResult

        // When / Then
        assertFailsWith<SeasonListRequestException> {
            getSeasonsUseCase(id)
        }
        coVerify(exactly = 1) { showRepository.getSeasons(id) }
    }
}