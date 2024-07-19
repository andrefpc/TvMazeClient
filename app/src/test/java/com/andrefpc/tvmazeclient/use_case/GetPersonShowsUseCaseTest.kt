package com.andrefpc.tvmazeclient.use_case

import com.andrefpc.tvmazeclient.domain.model.ApiError
import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.data.exception.ShowListNullException
import com.andrefpc.tvmazeclient.data.exception.ShowListRequestException
import com.andrefpc.tvmazeclient.domain.repository.api.TvMazeRepository
import com.andrefpc.tvmazeclient.domain.use_case.GetPersonShowsUseCase
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

class GetPersonShowsUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var tvMazeRepository: TvMazeRepository

    private lateinit var getPersonShowsUseCase: GetPersonShowsUseCase

    @Before
    fun setup() {
        getPersonShowsUseCase = GetPersonShowsUseCase(tvMazeRepository)
    }

    @Test
    fun `should return show list when api call is successful`() = runTest {
        // Given
        val id = 1
        val showList = listOf(ShowMocks.show)
        coEvery { tvMazeRepository.getPersonShows(id) } returns ApiResult.Success(showList)

        // When
        val result = getPersonShowsUseCase(id)

        // Then
        assertEquals(showList, result)
        coVerify(exactly = 1) { tvMazeRepository.getPersonShows(id) }
    }

    @Test
    fun `should throw ShowListNullException when result is null`() = runTest {
        // Given
        val id = 1
        coEvery { tvMazeRepository.getPersonShows(id) } returns ApiResult.Success(null)

        // When / Then
        assertFailsWith<ShowListNullException> {
            getPersonShowsUseCase(id)
        }
        coVerify(exactly = 1) { tvMazeRepository.getPersonShows(id) }
    }

    @Test
    fun `should throw ShowListRequestException when api call fails`() = runTest {
        // Given
        val id = 1
        val apiError = ApiError("Error")
        coEvery { tvMazeRepository.getPersonShows(id) } returns ApiResult.Error(apiError)

        // When / Then
        assertFailsWith<ShowListRequestException> {
            getPersonShowsUseCase(id)
        }
        coVerify(exactly = 1) { tvMazeRepository.getPersonShows(id) }
    }
}