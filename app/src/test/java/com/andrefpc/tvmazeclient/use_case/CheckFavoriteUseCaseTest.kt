package com.andrefpc.tvmazeclient.use_case

import com.andrefpc.tvmazeclient.core.domain.room.ShowRoomRepository
import com.andrefpc.tvmazeclient.core.domain.use_case.CheckFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CheckFavoriteUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var showRoomRepository: ShowRoomRepository

    private lateinit var checkFavoriteUseCase: CheckFavoriteUseCase

    @Before
    fun setup() {
        checkFavoriteUseCase = CheckFavoriteUseCase(showRoomRepository)
    }

    @Test
    fun `should return true when show is favorite`() = runTest {
        // Given
        val showId = 1
        coEvery { showRoomRepository.isFavorite(showId) } returns true

        // When
        val result = checkFavoriteUseCase(showId)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { showRoomRepository.isFavorite(showId) }
    }

    @Test
    fun `should return false when show is not favorite`() = runTest {
        // Given
        val showId = 1
        coEvery { showRoomRepository.isFavorite(showId) } returns false

        // When
        val result = checkFavoriteUseCase(showId)

        // Then
        assertFalse(result)
        coVerify(exactly = 1) { showRoomRepository.isFavorite(showId) }
    }
}