package com.andrefpc.tvmazeclient.repository

import com.andrefpc.tvmazeclient.data.remote.TvMazeApi
import com.andrefpc.tvmazeclient.data.remote.model.CastDto
import com.andrefpc.tvmazeclient.data.remote.model.EpisodeDto
import com.andrefpc.tvmazeclient.data.remote.model.SearchDto
import com.andrefpc.tvmazeclient.data.remote.model.SeasonDto
import com.andrefpc.tvmazeclient.data.remote.model.ShowDto
import com.andrefpc.tvmazeclient.data.repository.api.ShowRepositoryImpl
import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.util.CastMocks
import com.andrefpc.tvmazeclient.util.EpisodeMocks
import com.andrefpc.tvmazeclient.util.SeasonMocks
import com.andrefpc.tvmazeclient.util.ShowMocks
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ShowRepositoryTest {
    @MockK
    lateinit var tvMazeApi: TvMazeApi

    private lateinit var showRepository: ShowRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        showRepository = ShowRepositoryImpl(tvMazeApi)
    }

    @Test
    fun `getShows should return success when response is successful`() = runTest {
        // Given
        val shows = listOf(ShowMocks.showDto)
        val response = Response.success(shows)
        coEvery { tvMazeApi.getShows(any()) } returns response

        // When
        val result = showRepository.getShows(0)

        // Then
        assert(result is ApiResult.Success && result.result == shows.map { it.toDomain() })
        coVerify { tvMazeApi.getShows(any()) }
    }

    @Test
    fun `getShows should return error when response is unsuccessful`() = runTest {
        // Given
        val errorBody = ResponseBody.create(null, "{\"message\":\"error\"}")
        val response = Response.error<List<ShowDto>>(400, errorBody)
        coEvery { tvMazeApi.getShows(any()) } returns response

        // When
        val result = showRepository.getShows(0)

        // Then
        assert(result is ApiResult.Error && result.apiError.message == "error")
        coVerify { tvMazeApi.getShows(any()) }
    }

    @Test
    fun `getShows should return error when exception is thrown`() = runTest {
        // Given
        coEvery { tvMazeApi.getShows(any()) } throws RuntimeException("Test exception")

        // When
        val result = showRepository.getShows(0)

        // Then
        assert(result is ApiResult.Error)
        coVerify { tvMazeApi.getShows(any()) }
    }

    @Test
    fun `searchShows should return success when response is successful`() = runTest {
        // Given
        val searchResults = listOf(SearchDto(ShowMocks.showDto))
        val response = Response.success(searchResults)
        coEvery { tvMazeApi.search(any()) } returns response

        // When
        val result = showRepository.searchShows("test")

        // Then
        assert(result is ApiResult.Success && result.result == searchResults.map { it.show.toDomain() })
        coVerify { tvMazeApi.search(any()) }
    }

    @Test
    fun `searchShows should return error when response is unsuccessful`() = runTest {
        // Given
        val errorBody = ResponseBody.create(null, "{\"message\":\"error\"}")
        val response = Response.error<List<SearchDto>>(400, errorBody)
        coEvery { tvMazeApi.search(any()) } returns response

        // When
        val result = showRepository.searchShows("test")

        // Then
        assert(result is ApiResult.Error && result.apiError.message == "error")
        coVerify { tvMazeApi.search(any()) }
    }

    @Test
    fun `searchShows should return error when exception is thrown`() = runTest {
        // Given
        coEvery { tvMazeApi.search(any()) } throws RuntimeException("Test exception")

        // When
        val result = showRepository.searchShows("test")

        // Then
        assert(result is ApiResult.Error)
        coVerify { tvMazeApi.search(any()) }
    }

    @Test
    fun `getSeasons should return success when response is successful`() = runTest {
        // Given
        val seasons = listOf(SeasonMocks.seasonDto)
        val response = Response.success(seasons)
        coEvery { tvMazeApi.getSeasons(any()) } returns response

        // When
        val result = showRepository.getSeasons(1)

        // Then
        assert(result is ApiResult.Success && result.result == seasons.map { it.toDomain() })
        coVerify { tvMazeApi.getSeasons(any()) }
    }

    @Test
    fun `getSeasons should return error when response is unsuccessful`() = runTest {
        // Given
        val errorBody = ResponseBody.create(null, "{\"message\":\"error\"}")
        val response = Response.error<List<SeasonDto>>(400, errorBody)
        coEvery { tvMazeApi.getSeasons(any()) } returns response

        // When
        val result = showRepository.getSeasons(1)

        // Then
        assert(result is ApiResult.Error && result.apiError.message == "error")
        coVerify { tvMazeApi.getSeasons(any()) }
    }

    @Test
    fun `getSeasons should return error when exception is thrown`() = runTest {
        // Given
        coEvery { tvMazeApi.getSeasons(any()) } throws RuntimeException("Test exception")

        // When
        val result = showRepository.getSeasons(1)

        // Then
        assert(result is ApiResult.Error)
        coVerify { tvMazeApi.getSeasons(any()) }
    }

    @Test
    fun `getEpisodes should return success when response is successful`() = runTest {
        // Given
        val episodes = listOf(EpisodeMocks.episodeDto)
        val response = Response.success(episodes)
        coEvery { tvMazeApi.getEpisodes(any()) } returns response

        // When
        val result = showRepository.getEpisodes(1)

        // Then
        assert(result is ApiResult.Success && result.result == episodes.map { it.toDomain() })
        coVerify { tvMazeApi.getEpisodes(any()) }
    }

    @Test
    fun `getEpisodes should return error when response is unsuccessful`() = runTest {
        // Given
        val errorBody = ResponseBody.create(null, "{\"message\":\"error\"}")
        val response = Response.error<List<EpisodeDto>>(400, errorBody)
        coEvery { tvMazeApi.getEpisodes(any()) } returns response

        // When
        val result = showRepository.getEpisodes(1)

        // Then
        assert(result is ApiResult.Error && result.apiError.message == "error")
        coVerify { tvMazeApi.getEpisodes(any()) }
    }

    @Test
    fun `getEpisodes should return error when exception is thrown`() = runTest {
        // Given
        coEvery { tvMazeApi.getEpisodes(any()) } throws RuntimeException("Test exception")

        // When
        val result = showRepository.getEpisodes(1)

        // Then
        assert(result is ApiResult.Error)
        coVerify { tvMazeApi.getEpisodes(any()) }
    }

    @Test
    fun `getCast should return success when response is successful`() = runTest {
        // Given
        val cast = listOf(CastMocks.castDto)
        val response = Response.success(cast)
        coEvery { tvMazeApi.getCast(any()) } returns response

        // When
        val result = showRepository.getCast(1)

        // Then
        assert(result is ApiResult.Success && result.result == cast.map { it.toDomain() })
        coVerify { tvMazeApi.getCast(any()) }
    }

    @Test
    fun `getCast should return error when response is unsuccessful`() = runTest {
        // Given
        val errorBody = ResponseBody.create(null, "{\"message\":\"error\"}")
        val response = Response.error<List<CastDto>>(400, errorBody)
        coEvery { tvMazeApi.getCast(any()) } returns response

        // When
        val result = showRepository.getCast(1)

        // Then
        assert(result is ApiResult.Error && result.apiError.message == "error")
        coVerify { tvMazeApi.getCast(any()) }
    }

    @Test
    fun `getCast should return error when exception is thrown`() = runTest {
        // Given
        coEvery { tvMazeApi.getCast(any()) } throws RuntimeException("Test exception")

        // When
        val result = showRepository.getCast(1)

        // Then
        assert(result is ApiResult.Error)
        coVerify { tvMazeApi.getCast(any()) }
    }
}