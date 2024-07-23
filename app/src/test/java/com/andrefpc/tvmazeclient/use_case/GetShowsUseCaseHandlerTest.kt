package com.andrefpc.tvmazeclient.use_case

import com.andrefpc.tvmazeclient.domain.model.ApiError
import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.domain.model.Show
import com.andrefpc.tvmazeclient.domain.exception.ShowListNullException
import com.andrefpc.tvmazeclient.domain.exception.ShowListRequestException
import com.andrefpc.tvmazeclient.domain.repository.api.ShowRepository
import com.andrefpc.tvmazeclient.domain.use_case.GetShowsUseCase
import com.andrefpc.tvmazeclient.util.ShowMocks
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

class GetShowsUseCaseHandlerTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var showRepository: ShowRepository

    private lateinit var getShowsUseCase: GetShowsUseCase

    @Before
    fun setup() {
        getShowsUseCase = GetShowsUseCase(showRepository)
    }

    @Test
    fun `should return show list when repository call is successful with page`() = runTest {
        // Given
        val page = 1
        val shows = listOf(ShowMocks.show)
        val apiResult = ApiResult.Success(shows)

        coEvery { showRepository.getShows(page) } returns apiResult

        // When
        val result = getShowsUseCase(page = page)

        // Then
        assertEquals(shows, result)
        coVerify(exactly = 1) { showRepository.getShows(page) }
    }

    @Test
    fun `should return show list when repository call is successful with search term`() = runTest {
        // Given
        val searchTerm = "Test"
        val shows = listOf(ShowMocks.show)
        val apiResult = ApiResult.Success(shows)

        coEvery { showRepository.searchShows(searchTerm) } returns apiResult

        // When
        val result = getShowsUseCase(searchTerm = searchTerm)

        // Then
        assertEquals(shows, result)
        coVerify(exactly = 1) { showRepository.searchShows(searchTerm) }
    }

    @Test
    fun `should throw ShowListNullException when result is null with page`() = runTest {
        // Given
        val page = 1
        val apiResult = ApiResult.Success<List<Show>>(null)

        coEvery { showRepository.getShows(page) } returns apiResult

        // When / Then
        assertFailsWith<ShowListNullException> {
            getShowsUseCase(page = page)
        }
        coVerify(exactly = 1) { showRepository.getShows(page) }
    }

    @Test
    fun `should throw ShowListNullException when result is null with search term`() = runTest {
        // Given
        val searchTerm = "Test"
        val apiResult = ApiResult.Success<List<Show>>(null)

        coEvery { showRepository.searchShows(searchTerm) } returns apiResult

        // When / Then
        assertFailsWith<ShowListNullException> {
            getShowsUseCase(searchTerm = searchTerm)
        }
        coVerify(exactly = 1) { showRepository.searchShows(searchTerm) }
    }

    @Test
    fun `should throw ShowListRequestException when repository call fails with page`() = runTest {
        // Given
        val page = 1
        val apiError = ApiError("Error")
        val apiResult = ApiResult.Error(apiError)

        coEvery { showRepository.getShows(page) } returns apiResult

        // When / Then
        assertFailsWith<ShowListRequestException> {
            getShowsUseCase(page = page)
        }
        coVerify(exactly = 1) { showRepository.getShows(page) }
    }

    @Test
    fun `should throw ShowListRequestException when repository call fails with search term`() = runTest {
        // Given
        val searchTerm = "Test"
        val apiError = ApiError("Error")
        val apiResult = ApiResult.Error(apiError)

        coEvery { showRepository.searchShows(searchTerm) } returns apiResult

        // When / Then
        assertFailsWith<ShowListRequestException> {
            getShowsUseCase(searchTerm = searchTerm)
        }
        coVerify(exactly = 1) { showRepository.searchShows(searchTerm) }
    }
}