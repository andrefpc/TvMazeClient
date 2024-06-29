package com.andrefpc.tvmazeclient.view_model.shows

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andrefpc.tvmazeclient.core.data.ScreenState
import com.andrefpc.tvmazeclient.core.domain.use_case.GetShowsUseCase
import com.andrefpc.tvmazeclient.ui.compose.shows.ShowsViewModel
import com.andrefpc.tvmazeclient.ui.compose.shows.domain_use_case.ShowsUseCase
import com.andrefpc.tvmazeclient.util.ShowMocks
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
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
import org.junit.rules.TestRule
import kotlin.test.assertEquals

class ShowsViewModelTest {

    @get:Rule
    var instantExecutorRule: TestRule = InstantTaskExecutorRule()

    @MockK
    lateinit var getShowsUseCase: GetShowsUseCase

    private lateinit var showsUseCase: ShowsUseCase
    private lateinit var viewModel: ShowsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        Dispatchers.setMain(testDispatcher)

        showsUseCase = ShowsUseCase(getShowsUseCase)
        viewModel = ShowsViewModel(showsUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getShows should update listShowState and screenState correctly`() = runTest {
        // Given
        val shows = listOf(ShowMocks.show)
        val page = 0

        coEvery { getShowsUseCase(page = page) } returns shows

        // When
        viewModel.getShows(page)
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(shows, viewModel.listShowState.value)
        assertEquals(ScreenState.Success, viewModel.screenState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getShows should update screenState to Empty when list is empty`() = runTest {
        // Given
        val page = 0
        coEvery { getShowsUseCase(page = page) } returns emptyList()

        // When
        viewModel.getShows(page)
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(ScreenState.Empty, viewModel.screenState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getNextPageShows should update listShowState and screenState correctly`() = runTest {
        // Given
        val showsPage1 = listOf(ShowMocks.show)
        val showsPage2 = listOf(ShowMocks.showUpdated)

        coEvery { getShowsUseCase(page = 0) } returns showsPage1
        coEvery { getShowsUseCase(page = 1) } returns showsPage2

        // When
        viewModel.getShows(0)
        advanceUntilIdle() // Ensures all coroutines have completed

        viewModel.getNextPageShows()
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(showsPage1 + showsPage2, viewModel.listShowState.value)
        assertEquals(ScreenState.Success, viewModel.screenState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchShows should update listShowState and screenState correctly`() = runTest {
        // Given
        val searchResult = listOf(ShowMocks.showUpdated)
        val searchTerm = "search term"

        coEvery { getShowsUseCase(searchTerm = searchTerm) } returns searchResult

        // When
        viewModel.searchShows(searchTerm)
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(searchResult, viewModel.listShowState.value)
        assertEquals(ScreenState.Success, viewModel.screenState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchShows should update screenState to Empty when search result is empty`() = runTest {
        // Given
        val searchTerm = "search term"
        coEvery { getShowsUseCase(searchTerm = searchTerm) } returns emptyList()

        // When
        viewModel.searchShows(searchTerm)
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(ScreenState.Empty, viewModel.screenState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onFavoritesButtonClicked should emit Unit`() = runTest {
        // When
        viewModel.onFavoritesButtonClicked()

        // Then
        assertEquals(Unit, viewModel.openFavorites.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onPeopleButtonClicked should emit Unit`() = runTest {
        // When
        viewModel.onPeopleButtonCLicked()

        // Then
        assertEquals(Unit, viewModel.openPeople.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onShowClicked should emit the correct show`() = runTest {
        // Given
        val show = ShowMocks.show

        // When
        viewModel.onShowClicked(show)

        // Then
        assertEquals(show, viewModel.openShowDetails.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getShows should handle errors correctly`() = runTest {
        // Given
        val error = RuntimeException("Test Error")
        val page = 0
        coEvery { getShowsUseCase(page = page) } throws error

        // When
        viewModel.getShows(page)

        // Then
        val emittedError = viewModel.showError.first()
        assertEquals(error, emittedError)
    }
}