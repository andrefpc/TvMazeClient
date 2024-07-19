package com.andrefpc.tvmazeclient.view_model.person_details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andrefpc.tvmazeclient.domain.model.ScreenState
import com.andrefpc.tvmazeclient.domain.use_case.GetPersonShowsUseCase
import com.andrefpc.tvmazeclient.presentation.compose.screen.person_details.PersonDetailsViewModel
import com.andrefpc.tvmazeclient.domain.use_case.PersonDetailsUseCase
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

class PersonDetailsViewModelTest {

    @get:Rule
    var instantExecutorRule: TestRule = InstantTaskExecutorRule()

    @MockK
    lateinit var getPersonShowsUseCase: GetPersonShowsUseCase

    private lateinit var personDetailsUseCase: PersonDetailsUseCase
    private lateinit var viewModel: PersonDetailsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        Dispatchers.setMain(testDispatcher)

        personDetailsUseCase = PersonDetailsUseCase(getPersonShowsUseCase)
        viewModel = PersonDetailsViewModel(personDetailsUseCase)
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
        val personId = 1

        coEvery { getPersonShowsUseCase(personId) } returns shows

        // When
        viewModel.getShows(personId)
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(shows, viewModel.listShowState.value)
        assertEquals(ScreenState.Success, viewModel.screenState.value)
    }

    @Test
    fun `getShows should handle errors correctly`() = runTest {
        // Given
        val error = RuntimeException("Test Error")
        val personId = 1
        coEvery { getPersonShowsUseCase(personId) } throws error

        // When
        viewModel.getShows(personId)

        // Then
        val emittedError = viewModel.showError.first()
        assertEquals(error, emittedError)
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
}