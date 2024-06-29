package com.andrefpc.tvmazeclient.use_case

import com.andrefpc.tvmazeclient.core.domain.room.ShowRoomRepository
import com.andrefpc.tvmazeclient.core.domain.use_case.GetFavoritesUseCase
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

class GetFavoritesUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var showRoomRepository: ShowRoomRepository

    private lateinit var getFavoritesUseCase: GetFavoritesUseCase

    @Before
    fun setup() {
        getFavoritesUseCase = GetFavoritesUseCase(showRoomRepository)
    }

    @Test
    fun `should return all shows when term is null`() = runTest {
        // Given
        val showsList = listOf(ShowMocks.show)
        coEvery { showRoomRepository.getAll() } returns showsList

        // When
        val result = getFavoritesUseCase()

        // Then
        assertEquals(showsList, result)
        coVerify(exactly = 1) { showRoomRepository.getAll() }
    }

    @Test
    fun `should return searched shows when term is provided`() = runTest {
        // Given
        val term = "Search Term"
        val searchedShowsList = listOf(ShowMocks.show)
        coEvery { showRoomRepository.search(term) } returns searchedShowsList

        // When
        val result = getFavoritesUseCase(term)

        // Then
        assertEquals(searchedShowsList, result)
        coVerify(exactly = 1) { showRoomRepository.search(term) }
    }
}