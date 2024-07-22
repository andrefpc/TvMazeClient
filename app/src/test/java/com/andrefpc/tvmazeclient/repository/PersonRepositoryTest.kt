package com.andrefpc.tvmazeclient.repository

import com.andrefpc.tvmazeclient.data.remote.TvMazeApi
import com.andrefpc.tvmazeclient.data.remote.model.PersonDto
import com.andrefpc.tvmazeclient.data.remote.model.PersonShowDto
import com.andrefpc.tvmazeclient.data.remote.model.SearchPeopleDto
import com.andrefpc.tvmazeclient.data.repository.api.PersonRepositoryImpl
import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.util.PersonMocks
import com.andrefpc.tvmazeclient.util.PersonShowMocks
import com.andrefpc.tvmazeclient.util.SearchPeopleMocks
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class PersonRepositoryTest {
    @MockK
    lateinit var tvMazeApi: TvMazeApi

    private lateinit var personRepository: PersonRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        personRepository = PersonRepositoryImpl(tvMazeApi)
    }

    @Test
    fun `getPersonShows should return success when response is successful`() = runTest {
        // Given
        val personShows = listOf(PersonShowMocks.personShowDto)
        val response = Response.success(personShows)
        coEvery { tvMazeApi.getPersonShows(any()) } returns response

        // When
        val result = personRepository.getPersonShows(1)

        // Then
        assert(result is ApiResult.Success && result.result == personShows.map { it.embedded.show.toDomain() })
        coVerify { tvMazeApi.getPersonShows(any()) }
    }

    @Test
    fun `getPersonShows should return error when response is unsuccessful`() = runTest {
        // Given
        val errorBody = ResponseBody.create(null, "{\"message\":\"error\"}")
        val response = Response.error<List<PersonShowDto>>(400, errorBody)
        coEvery { tvMazeApi.getPersonShows(any()) } returns response

        // When
        val result = personRepository.getPersonShows(1)

        // Then
        assert(result is ApiResult.Error && result.apiError.message == "error")
        coVerify { tvMazeApi.getPersonShows(any()) }
    }

    @Test
    fun `getPersonShows should return error when exception is thrown`() = runTest {
        // Given
        coEvery { tvMazeApi.getPersonShows(any()) } throws RuntimeException("Test exception")

        // When
        val result = personRepository.getPersonShows(1)

        // Then
        assert(result is ApiResult.Error)
        coVerify { tvMazeApi.getPersonShows(any()) }
    }

    @Test
    fun `getPeople should return success when response is successful`() = runTest {
        // Given
        val people = listOf(PersonMocks.personDto)
        val response = Response.success(people)
        coEvery { tvMazeApi.getPeople(any()) } returns response

        // When
        val result = personRepository.getPeople(0)

        // Then
        assert(result is ApiResult.Success && result.result == people.map { it.toDomain() })
        coVerify { tvMazeApi.getPeople(any()) }
    }

    @Test
    fun `getPeople should return error when response is unsuccessful`() = runTest {
        // Given
        val errorBody = ResponseBody.create(null, "{\"message\":\"error\"}")
        val response = Response.error<List<PersonDto>>(400, errorBody)
        coEvery { tvMazeApi.getPeople(any()) } returns response

        // When
        val result = personRepository.getPeople(0)

        // Then
        assert(result is ApiResult.Error && result.apiError.message == "error")
        coVerify { tvMazeApi.getPeople(any()) }
    }

    @Test
    fun `getPeople should return error when exception is thrown`() = runTest {
        // Given
        coEvery { tvMazeApi.getPeople(any()) } throws RuntimeException("Test exception")

        // When
        val result = personRepository.getPeople(0)

        // Then
        assert(result is ApiResult.Error)
        coVerify { tvMazeApi.getPeople(any()) }
    }

    @Test
    fun `searchPeople should return success when response is successful`() = runTest {
        // Given
        val searchResults = listOf(SearchPeopleMocks.searchPeopleDto)
        val response = Response.success(searchResults)
        coEvery { tvMazeApi.searchPeople(any()) } returns response

        // When
        val result = personRepository.searchPeople("test")

        // Then
        assert(result is ApiResult.Success && result.result == searchResults.map { it.person.toDomain() })
        coVerify { tvMazeApi.searchPeople(any()) }
    }

    @Test
    fun `searchPeople should return error when response is unsuccessful`() = runTest {
        // Given
        val errorBody = ResponseBody.create(null, "{\"message\":\"error\"}")
        val response = Response.error<List<SearchPeopleDto>>(400, errorBody)
        coEvery { tvMazeApi.searchPeople(any()) } returns response

        // When
        val result = personRepository.searchPeople("test")

        // Then
        assert(result is ApiResult.Error && result.apiError.message == "error")
        coVerify { tvMazeApi.searchPeople(any()) }
    }

    @Test
    fun `searchPeople should return error when exception is thrown`() = runTest {
        // Given
        coEvery { tvMazeApi.searchPeople(any()) } throws RuntimeException("Test exception")

        // When
        val result = personRepository.searchPeople("test")

        // Then
        assert(result is ApiResult.Error)
        coVerify { tvMazeApi.searchPeople(any()) }
    }
}