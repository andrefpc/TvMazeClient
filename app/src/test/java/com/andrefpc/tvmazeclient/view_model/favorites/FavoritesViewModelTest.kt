package com.andrefpc.tvmazeclient.view_model.favorites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andrefpc.tvmazeclient.core.data.ScreenState
import com.andrefpc.tvmazeclient.core.domain.use_case.DeleteFavoriteUseCase
import com.andrefpc.tvmazeclient.core.domain.use_case.GetFavoritesUseCase
import com.andrefpc.tvmazeclient.ui.compose.favorites.FavoritesViewModel
import com.andrefpc.tvmazeclient.ui.compose.favorites.domain.use_case.FavoritesUseCase
import com.andrefpc.tvmazeclient.util.ShowMocks
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoritesViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var getFavoritesUseCase: GetFavoritesUseCase

    @MockK
    lateinit var deleteFavoriteUseCase: DeleteFavoriteUseCase

    private lateinit var favoritesUseCase: FavoritesUseCase
    private lateinit var viewModel: FavoritesViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        Dispatchers.setMain(testDispatcher)

        favoritesUseCase = FavoritesUseCase(getFavoritesUseCase, deleteFavoriteUseCase)
        viewModel = FavoritesViewModel(favoritesUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getFavorites should update listShowState and screenState correctly`() = runTest {
        // Given
        val favorites = listOf(ShowMocks.show)

        coEvery { getFavoritesUseCase() } returns favorites

        // When
        viewModel.getFavorites()
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(favorites, viewModel.listShowState.first())
        assertEquals(ScreenState.Success, viewModel.screenState.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getFavorites should update screenState to Empty when list is empty`() = runTest {
        // Given
        coEvery { getFavoritesUseCase() } returns emptyList()

        // When
        viewModel.getFavorites()
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(ScreenState.Empty, viewModel.screenState.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onSearchFavorites should update listShowState and screenState correctly`() = runTest {
        // Given
        val searchResult = listOf(ShowMocks.showUpdated)

        coEvery { getFavoritesUseCase("search term") } returns searchResult

        // When
        viewModel.onSearchFavorites("search term")
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(searchResult, viewModel.listShowState.first())
        assertEquals(ScreenState.Success, viewModel.screenState.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onSearchFavorites should update screenState to Empty when search result is empty`() = runTest {
        // Given
        coEvery { getFavoritesUseCase("search term") } returns emptyList()

        // When
        viewModel.onSearchFavorites("search term")
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(ScreenState.Empty, viewModel.screenState.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onShowDeleted should update listShowState and screenState correctly`() = runTest {
        // Given
        val show = ShowMocks.show
        val updatedFavorites = listOf(ShowMocks.showUpdated)

        // Mock the deleteFavoriteUseCase to return the updated list after deletion
        coEvery { favoritesUseCase.deleteFavorite(show) } returns updatedFavorites

        // When
        viewModel.onShowDeleted(show)
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        coVerify(exactly = 1) { favoritesUseCase.deleteFavorite(show) }
        assert(viewModel.listShowState.value == updatedFavorites)
        assert(viewModel.screenState.value == ScreenState.Success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onShowDeleted should update screenState to Empty when no favorites remain`() = runTest {
        // Given
        val show = ShowMocks.show

        coEvery { deleteFavoriteUseCase(show) } returns listOf()

        // When
        viewModel.onShowDeleted(show)
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        coVerify { deleteFavoriteUseCase(show) }
        assertEquals(ScreenState.Empty, viewModel.screenState.first())
    }

    @Test
    fun `onShowClicked should emit the correct show`() = runTest {
        // Given
        val show = ShowMocks.show

        // When
        viewModel.onShowClicked(show)

        // Then
        assertEquals(show, viewModel.openShowDetails.first())
    }

    @Test
    fun `getFavorites should handle errors correctly`() = runTest {
        // Given
        val error = RuntimeException("Test Error")
        coEvery { getFavoritesUseCase() } throws error

        // When
        viewModel.getFavorites()

        // Then
        assertEquals(error, viewModel.showError.first())
    }
}