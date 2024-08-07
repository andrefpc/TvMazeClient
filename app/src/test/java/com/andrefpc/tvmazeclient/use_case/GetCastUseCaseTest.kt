package com.andrefpc.tvmazeclient.use_case

import com.andrefpc.tvmazeclient.domain.model.ApiError
import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.domain.exception.CastListNullException
import com.andrefpc.tvmazeclient.domain.exception.CastListRequestException
import com.andrefpc.tvmazeclient.domain.repository.api.ShowRepository
import com.andrefpc.tvmazeclient.domain.use_case.GetCastUseCase
import com.andrefpc.tvmazeclient.util.CastMocks
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

class GetCastUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var showRepository: ShowRepository

    private lateinit var getCastUseCase: GetCastUseCase

    @Before
    fun setup() {
        getCastUseCase = GetCastUseCase(showRepository)
    }

    @Test
    fun `should return cast list when result is success`() = runTest {
        // Given
        val id = 1
        val castList = listOf(CastMocks.cast)
        coEvery { showRepository.getCast(id) } returns ApiResult.Success(castList)

        // When
        val result = getCastUseCase(id)

        // Then
        assertEquals(castList, result)
        coVerify(exactly = 1) { showRepository.getCast(id) }
    }

    @Test
    fun `should throw CastListNullException when result is success but cast list is null`() = runTest {
        // Given
        val id = 1
        coEvery { showRepository.getCast(id) } returns ApiResult.Success(null)

        // When & Then
        assertFailsWith<CastListNullException> {
            getCastUseCase(id)
        }
        coVerify(exactly = 1) { showRepository.getCast(id) }
    }

    @Test
    fun `should throw CastListRequestException when result is error`() = runTest {
        // Given
        val id = 1
        val apiError = ApiError(message = "Error fetching cast list")
        coEvery { showRepository.getCast(id) } returns ApiResult.Error(apiError)

        // When & Then
        val exception = assertFailsWith<CastListRequestException> {
            getCastUseCase(id)
        }
        assertEquals(apiError, exception.apiError)
        coVerify(exactly = 1) { showRepository.getCast(id) }
    }
}