package com.andrefpc.tvmazeclient.use_case

import com.andrefpc.tvmazeclient.core.domain.room.ShowRoomRepository
import com.andrefpc.tvmazeclient.core.domain.use_case.SwitchFavoriteUseCase
import com.andrefpc.tvmazeclient.util.ShowMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SwitchFavoriteUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var showRoomRepository: ShowRoomRepository

    private lateinit var switchFavoriteUseCase: SwitchFavoriteUseCase

    @Before
    fun setup() {
        switchFavoriteUseCase = SwitchFavoriteUseCase(showRoomRepository)
    }

    @Test
    fun `should delete show if it is already a favorite`() = runTest {
        // Given
        val show = ShowMocks.show
        coEvery { showRoomRepository.isFavorite(show.id) } returns true
        coEvery { showRoomRepository.delete(show.id) } returns Unit

        // When
        switchFavoriteUseCase(show)

        // Then
        coVerify(exactly = 1) { showRoomRepository.isFavorite(show.id) }
        coVerify(exactly = 1) { showRoomRepository.delete(show.id) }
        coVerify(exactly = 0) { showRoomRepository.insert(show) }
    }

    @Test
    fun `should insert show if it is not already a favorite`() = runTest {
        // Given
        val show = ShowMocks.show
        coEvery { showRoomRepository.isFavorite(show.id) } returns false
        coEvery { showRoomRepository.insert(show) } returns Unit

        // When
        switchFavoriteUseCase(show)

        // Then
        coVerify(exactly = 1) { showRoomRepository.isFavorite(show.id) }
        coVerify(exactly = 1) { showRoomRepository.insert(show) }
        coVerify(exactly = 0) { showRoomRepository.delete(show.id) }
    }
}