package com.andrefpc.tvmazeclient.view_model.people

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andrefpc.tvmazeclient.presentation.model.ScreenViewState
import com.andrefpc.tvmazeclient.domain.use_case.GetPeopleUseCase
import com.andrefpc.tvmazeclient.presentation.compose.screen.people.PeopleViewModel
import com.andrefpc.tvmazeclient.presentation.model.handler.PeopleUseCaseHandler
import com.andrefpc.tvmazeclient.util.PersonMocks
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

class PeopleViewModelTest {

    @get:Rule
    var instantExecutorRule: TestRule = InstantTaskExecutorRule()

    @MockK
    lateinit var getPeopleUseCase: GetPeopleUseCase

    private lateinit var peopleHandler: PeopleUseCaseHandler
    private lateinit var viewModel: PeopleViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        Dispatchers.setMain(testDispatcher)

        peopleHandler = PeopleUseCaseHandler(getPeopleUseCase)
        viewModel = PeopleViewModel(peopleHandler)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getPeople should update listPeopleState and screenState correctly`() = runTest {
        // Given
        val people = listOf(PersonMocks.person)
        val peopleViewState = listOf(PersonMocks.personViewState)

        coEvery { getPeopleUseCase(page = 0) } returns people

        // When
        viewModel.getPeople()
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(peopleViewState, viewModel.listPeopleState.value)
        assertEquals(ScreenViewState.Success, viewModel.screenState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getPeople should update screenState to Empty when list is empty`() = runTest {
        // Given
        coEvery { getPeopleUseCase(page = 0) } returns emptyList()

        // When
        viewModel.getPeople()
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(ScreenViewState.Empty, viewModel.screenState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getNextPageShows should update listPeopleState and screenState correctly`() = runTest {
        // Given
        val peoplePage1 = listOf(PersonMocks.person)
        val peoplePage2 = listOf(PersonMocks.personUpdated)
        val peoplePage1ViewState = listOf(PersonMocks.personViewState)
        val peoplePage2ViewState = listOf(PersonMocks.personUpdatedViewState)


        coEvery { getPeopleUseCase(page = 0) } returns peoplePage1
        coEvery { getPeopleUseCase(page = 1) } returns peoplePage2

        // When
        viewModel.getPeople(0)
        advanceUntilIdle() // Ensures all coroutines have completed

        viewModel.getNextPageShows()
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(peoplePage1ViewState + peoplePage2ViewState, viewModel.listPeopleState.value)
        assertEquals(ScreenViewState.Success, viewModel.screenState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onSearchPeople should update listPeopleState and screenState correctly`() = runTest {
        // Given
        val searchResult = listOf(PersonMocks.personUpdated)
        val searchResultViewState = listOf(PersonMocks.personUpdatedViewState)

        coEvery { getPeopleUseCase(searchTerm = "search term") } returns searchResult

        // When
        viewModel.onSearchPeople("search term")
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(searchResultViewState, viewModel.listPeopleState.value)
        assertEquals(ScreenViewState.Success, viewModel.screenState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onSearchPeople should update screenState to Empty when search result is empty`() = runTest {
        // Given
        coEvery { getPeopleUseCase(searchTerm = "search term") } returns emptyList()

        // When
        viewModel.onSearchPeople("search term")
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(ScreenViewState.Empty, viewModel.screenState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onPersonClicked should emit the correct person`() = runTest {
        // Given
        val personViewState = PersonMocks.personViewState

        // When
        viewModel.onPersonClicked(personViewState)

        // Then
        assertEquals(personViewState, viewModel.openPersonDetails.first())
    }

    @Test
    fun `getPeople should handle errors correctly`() = runTest {
        // Given
        val error = RuntimeException("Test Error")
        coEvery { getPeopleUseCase(page = 0) } throws error

        // When
        viewModel.getPeople()

        // Then
        assertEquals(error, viewModel.showError.first())
    }
}