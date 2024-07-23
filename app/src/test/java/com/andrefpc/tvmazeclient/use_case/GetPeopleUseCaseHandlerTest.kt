package com.andrefpc.tvmazeclient.use_case

import com.andrefpc.tvmazeclient.domain.exception.PeopleListNullException
import com.andrefpc.tvmazeclient.domain.exception.PeopleListRequestException
import com.andrefpc.tvmazeclient.domain.model.ApiError
import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.domain.repository.api.PersonRepository
import com.andrefpc.tvmazeclient.domain.use_case.GetPeopleUseCase
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

class GetPeopleUseCaseHandlerTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var personRepository: PersonRepository

    private lateinit var getPeopleUseCase: GetPeopleUseCase

    @Before
    fun setup() {
        getPeopleUseCase = GetPeopleUseCase(personRepository)
    }

    @Test
    fun `should return people list when search term is null`() = runTest {
        // Given
        val page = 1
        val peopleList = listOf(PersonMocks.person)
        coEvery { personRepository.getPeople(page) } returns ApiResult.Success(peopleList)

        // When
        val result = getPeopleUseCase(page)

        // Then
        assertEquals(peopleList, result)
        coVerify(exactly = 1) { personRepository.getPeople(page) }
    }

    @Test
    fun `should return people list when search term is provided`() = runTest {
        // Given
        val searchTerm = "Search Term"
        val searchedPeopleList = listOf(PersonMocks.person)
        coEvery { personRepository.searchPeople(searchTerm) } returns ApiResult.Success(
            searchedPeopleList
        )

        // When
        val result = getPeopleUseCase(searchTerm = searchTerm)

        // Then
        assertEquals(searchedPeopleList, result)
        coVerify(exactly = 1) { personRepository.searchPeople(searchTerm) }
    }

    @Test
    fun `should throw PeopleListNullException when result is null`() = runTest {
        // Given
        val page = 1
        coEvery { personRepository.getPeople(page) } returns ApiResult.Success(null)

        // When / Then
        assertFailsWith<PeopleListNullException> {
            getPeopleUseCase(page)
        }
        coVerify(exactly = 1) { personRepository.getPeople(page) }
    }

    @Test
    fun `should throw PeopleListRequestException when api call fails`() = runTest {
        // Given
        val page = 1
        val apiError = ApiError("Error")
        coEvery { personRepository.getPeople(page) } returns ApiResult.Error(apiError)

        // When / Then
        assertFailsWith<PeopleListRequestException> {
            getPeopleUseCase(page)
        }
        coVerify(exactly = 1) { personRepository.getPeople(page) }
    }

    @Test
    fun `should throw PeopleListRequestException when search api call fails`() = runTest {
        // Given
        val searchTerm = "Search Term"
        val apiError = ApiError("Error")
        coEvery { personRepository.searchPeople(searchTerm) } returns ApiResult.Error(apiError)

        // When / Then
        assertFailsWith<PeopleListRequestException> {
            getPeopleUseCase(searchTerm = searchTerm)
        }
        coVerify(exactly = 1) { personRepository.searchPeople(searchTerm) }
    }
}