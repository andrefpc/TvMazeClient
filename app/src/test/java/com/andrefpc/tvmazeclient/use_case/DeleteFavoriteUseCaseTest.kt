package com.andrefpc.tvmazeclient.use_case
import com.andrefpc.tvmazeclient.domain.model.Show
import com.andrefpc.tvmazeclient.domain.repository.database.ShowRoomRepository
import com.andrefpc.tvmazeclient.domain.use_case.DeleteFavoriteUseCase
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

class DeleteFavoriteUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var showRoomRepository: ShowRoomRepository

    private lateinit var deleteFavoriteUseCase: DeleteFavoriteUseCase

    @Before
    fun setup() {
        deleteFavoriteUseCase = DeleteFavoriteUseCase(showRoomRepository)
    }

    @Test
    fun `should delete favorite show and return updated list`() = runTest {
        // Given
        val show = ShowMocks.show
        val updatedShows = listOf(ShowMocks.showUpdated)
        coEvery { showRoomRepository.delete(show.id) } returns Unit
        coEvery { showRoomRepository.getAll() } returns updatedShows

        // When
        val result = deleteFavoriteUseCase(show)

        // Then
        assertEquals(updatedShows, result)
        coVerify(exactly = 1) { showRoomRepository.delete(show.id) }
        coVerify(exactly = 1) { showRoomRepository.getAll() }
    }

    @Test
    fun `should delete favorite show and return empty list when no shows remain`() = runTest {
        // Given
        val show = ShowMocks.show
        val updatedShows = emptyList<Show>()
        coEvery { showRoomRepository.delete(show.id) } returns Unit
        coEvery { showRoomRepository.getAll() } returns updatedShows

        // When
        val result = deleteFavoriteUseCase(show)

        // Then
        assertEquals(updatedShows, result)
        coVerify(exactly = 1) { showRoomRepository.delete(show.id) }
        coVerify(exactly = 1) { showRoomRepository.getAll() }
    }
}