package com.andrefpc.tvmazeclient.view_model.favorites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andrefpc.tvmazeclient.presentation.model.ScreenViewState
import com.andrefpc.tvmazeclient.domain.use_case.DeleteFavoriteUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetFavoritesUseCase
import com.andrefpc.tvmazeclient.presentation.compose.screen.favorites.FavoritesViewModel
import com.andrefpc.tvmazeclient.presentation.model.handler.FavoritesUseCaseHandler
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

    private lateinit var favoritesHandler: FavoritesUseCaseHandler
    private lateinit var viewModel: FavoritesViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        Dispatchers.setMain(testDispatcher)

        favoritesHandler = FavoritesUseCaseHandler(getFavoritesUseCase, deleteFavoriteUseCase)
        viewModel = FavoritesViewModel(favoritesHandler)
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
        val favoritesViewState = listOf(ShowMocks.showViewState)

        coEvery { getFavoritesUseCase() } returns favorites

        // When
        viewModel.getFavorites()
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(favoritesViewState, viewModel.listShowState.first())
        assertEquals(ScreenViewState.Success, viewModel.screenState.first())
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
        assertEquals(ScreenViewState.Empty, viewModel.screenState.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onSearchFavorites should update listShowState and screenState correctly`() = runTest {
        // Given
        val searchResult = listOf(ShowMocks.showUpdated)
        val searchResultViewState = listOf(ShowMocks.showUpdatedViewState)

        coEvery { getFavoritesUseCase("search term") } returns searchResult

        // When
        viewModel.onSearchFavorites("search term")
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(searchResultViewState, viewModel.listShowState.first())
        assertEquals(ScreenViewState.Success, viewModel.screenState.first())
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
        assertEquals(ScreenViewState.Empty, viewModel.screenState.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onShowDeleted should update listShowState and screenState correctly`() = runTest {
        // Given
        val show = ShowMocks.show
        val showViewState = ShowMocks.showViewState
        val updatedFavorites = listOf(ShowMocks.showUpdated)
        val updatedFavoritesViewState = listOf(ShowMocks.showUpdatedViewState)

        // Mock the deleteFavoriteUseCase to return the updated list after deletion
        coEvery { favoritesHandler.deleteFavorite(show) } returns updatedFavorites

        // When
        viewModel.onShowDeleted(showViewState)
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        coVerify(exactly = 1) { favoritesHandler.deleteFavorite(show) }
        assert(viewModel.listShowState.value == updatedFavoritesViewState)
        assert(viewModel.screenState.value == ScreenViewState.Success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onShowDeleted should update screenState to Empty when no favorites remain`() = runTest {
        // Given
        val show = ShowMocks.show
        val showViewState = ShowMocks.showViewState

        coEvery { deleteFavoriteUseCase(show) } returns listOf()

        // When
        viewModel.onShowDeleted(showViewState)
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        coVerify { deleteFavoriteUseCase(show) }
        assertEquals(ScreenViewState.Empty, viewModel.screenState.first())
    }

    @Test
    fun `onShowClicked should emit the correct show`() = runTest {
        // Given
        val showViewState = ShowMocks.showViewState

        // When
        viewModel.onShowClicked(showViewState)

        // Then
        assertEquals(showViewState, viewModel.openShowDetails.first())
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