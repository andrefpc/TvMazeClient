package com.andrefpc.tvmazeclient.use_case

import com.andrefpc.tvmazeclient.core.data.ApiError
import com.andrefpc.tvmazeclient.core.data.ApiResult
import com.andrefpc.tvmazeclient.core.domain.exception.PeopleListNullException
import com.andrefpc.tvmazeclient.core.domain.exception.PeopleListRequestException
import com.andrefpc.tvmazeclient.core.domain.repository.TvMazeRepository
import com.andrefpc.tvmazeclient.core.domain.use_case.GetPeopleUseCase
import com.andrefpc.tvmazeclient.util.PersonMocks
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

class GetPeopleUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var tvMazeRepository: TvMazeRepository

    private lateinit var getPeopleUseCase: GetPeopleUseCase

    @Before
    fun setup() {
        getPeopleUseCase = GetPeopleUseCase(tvMazeRepository)
    }

    @Test
    fun `should return people list when search term is null`() = runTest {
        // Given
        val page = 1
        val peopleList = listOf(PersonMocks.person)
        coEvery { tvMazeRepository.getPeople(page) } returns ApiResult.Success(peopleList)

        // When
        val result = getPeopleUseCase(page)

        // Then
        assertEquals(peopleList, result)
        coVerify(exactly = 1) { tvMazeRepository.getPeople(page) }
    }

    @Test
    fun `should return people list when search term is provided`() = runTest {
        // Given
        val searchTerm = "Search Term"
        val searchedPeopleList = listOf(PersonMocks.person)
        coEvery { tvMazeRepository.searchPeople(searchTerm) } returns ApiResult.Success(
            searchedPeopleList
        )

        // When
        val result = getPeopleUseCase(searchTerm = searchTerm)

        // Then
        assertEquals(searchedPeopleList, result)
        coVerify(exactly = 1) { tvMazeRepository.searchPeople(searchTerm) }
    }

    @Test
    fun `should throw PeopleListNullException when result is null`() = runTest {
        // Given
        val page = 1
        coEvery { tvMazeRepository.getPeople(page) } returns ApiResult.Success(null)

        // When / Then
        assertFailsWith<PeopleListNullException> {
            getPeopleUseCase(page)
        }
        coVerify(exactly = 1) { tvMazeRepository.getPeople(page) }
    }

    @Test
    fun `should throw PeopleListRequestException when api call fails`() = runTest {
        // Given
        val page = 1
        val apiError = ApiError("Error")
        coEvery { tvMazeRepository.getPeople(page) } returns ApiResult.Error(apiError)

        // When / Then
        assertFailsWith<PeopleListRequestException> {
            getPeopleUseCase(page)
        }
        coVerify(exactly = 1) { tvMazeRepository.getPeople(page) }
    }

    @Test
    fun `should throw PeopleListRequestException when search api call fails`() = runTest {
        // Given
        val searchTerm = "Search Term"
        val apiError = ApiError("Error")
        coEvery { tvMazeRepository.searchPeople(searchTerm) } returns ApiResult.Error(apiError)

        // When / Then
        assertFailsWith<PeopleListRequestException> {
            getPeopleUseCase(searchTerm = searchTerm)
        }
        coVerify(exactly = 1) { tvMazeRepository.searchPeople(searchTerm) }
    }
}