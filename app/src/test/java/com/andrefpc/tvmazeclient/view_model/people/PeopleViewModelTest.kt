package com.andrefpc.tvmazeclient.view_model.people

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andrefpc.tvmazeclient.core.data.ScreenState
import com.andrefpc.tvmazeclient.core.domain.use_case.GetPeopleUseCase
import com.andrefpc.tvmazeclient.ui.compose.people.PeopleViewModel
import com.andrefpc.tvmazeclient.ui.compose.people.domain.use_case.PeopleUseCase
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

    private lateinit var peopleUseCase: PeopleUseCase
    private lateinit var viewModel: PeopleViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        Dispatchers.setMain(testDispatcher)

        peopleUseCase = PeopleUseCase(getPeopleUseCase)
        viewModel = PeopleViewModel(peopleUseCase)
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

        coEvery { getPeopleUseCase(page = 0) } returns people

        // When
        viewModel.getPeople()
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(people, viewModel.listPeopleState.value)
        assertEquals(ScreenState.Success, viewModel.screenState.value)
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
        assertEquals(ScreenState.Empty, viewModel.screenState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getNextPageShows should update listPeopleState and screenState correctly`() = runTest {
        // Given
        val peoplePage1 = listOf(PersonMocks.person)
        val peoplePage2 = listOf(PersonMocks.personUpdated)

        coEvery { getPeopleUseCase(page = 0) } returns peoplePage1
        coEvery { getPeopleUseCase(page = 1) } returns peoplePage2

        // When
        viewModel.getPeople(0)
        advanceUntilIdle() // Ensures all coroutines have completed

        viewModel.getNextPageShows()
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(peoplePage1 + peoplePage2, viewModel.listPeopleState.value)
        assertEquals(ScreenState.Success, viewModel.screenState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onSearchPeople should update listPeopleState and screenState correctly`() = runTest {
        // Given
        val searchResult = listOf(PersonMocks.personUpdated)

        coEvery { getPeopleUseCase(searchTerm = "search term") } returns searchResult

        // When
        viewModel.onSearchPeople("search term")
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(searchResult, viewModel.listPeopleState.value)
        assertEquals(ScreenState.Success, viewModel.screenState.value)
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
        assertEquals(ScreenState.Empty, viewModel.screenState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onPersonClicked should emit the correct person`() = runTest {
        // Given
        val person = PersonMocks.person

        // When
        viewModel.onPersonClicked(person)

        // Then
        assertEquals(person, viewModel.openPersonDetails.first())
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