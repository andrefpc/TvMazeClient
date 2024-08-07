package com.andrefpc.tvmazeclient.view_model.show_details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.andrefpc.tvmazeclient.presentation.model.ScreenViewState
import com.andrefpc.tvmazeclient.domain.use_case.CheckFavoriteUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetCastUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetEpisodesUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetSeasonEpisodesUseCase
import com.andrefpc.tvmazeclient.domain.use_case.GetSeasonsUseCase
import com.andrefpc.tvmazeclient.domain.use_case.SwitchFavoriteUseCase
import com.andrefpc.tvmazeclient.presentation.compose.screen.show_details.ShowDetailsViewModel
import com.andrefpc.tvmazeclient.presentation.model.handler.ShowDetailsUseCaseHandler
import com.andrefpc.tvmazeclient.util.TestCoroutineContextProvider
import com.andrefpc.tvmazeclient.util.CastMocks
import com.andrefpc.tvmazeclient.util.EpisodeMocks
import com.andrefpc.tvmazeclient.util.PersonMocks
import com.andrefpc.tvmazeclient.util.SeasonEpisodeMocks
import com.andrefpc.tvmazeclient.util.ShowMocks
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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

class ShowDetailsViewModelTest {

    @get:Rule
    var instantExecutorRule: TestRule = InstantTaskExecutorRule()

    @MockK
    lateinit var getCastUseCase: GetCastUseCase

    @MockK
    lateinit var getSeasonEpisodesUseCase: GetSeasonEpisodesUseCase

    @MockK
    lateinit var getSeasonsUseCase: GetSeasonsUseCase

    @MockK
    lateinit var getEpisodesUseCase: GetEpisodesUseCase

    @MockK
    lateinit var switchFavoriteUseCase: SwitchFavoriteUseCase

    @MockK
    lateinit var checkFavoriteUseCase: CheckFavoriteUseCase

    private lateinit var showDetailsHandler: ShowDetailsUseCaseHandler
    private lateinit var viewModel: ShowDetailsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        Dispatchers.setMain(testDispatcher)

        showDetailsHandler = ShowDetailsUseCaseHandler(
            getCast = getCastUseCase,
            getSeasonEpisodes = getSeasonEpisodesUseCase,
            getSeasons = getSeasonsUseCase,
            getEpisodes = getEpisodesUseCase,
            switchFavorite = switchFavoriteUseCase,
            checkFavorite = checkFavoriteUseCase
        )
        viewModel = ShowDetailsViewModel(showDetailsHandler, TestCoroutineContextProvider())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getCastAndEpisodes should update listCastState and listSeasonEpisodesState correctly`() = runTest {
        // Given
        val cast = listOf(CastMocks.cast)
        val seasonEpisodes = listOf(SeasonEpisodeMocks.seasonEpisodes)
        val castViewState = listOf(CastMocks.castViewState)
        val seasonEpisodesViewState = listOf(SeasonEpisodeMocks.seasonEpisodesViewState)
        val showId = 1

        coEvery { getCastUseCase(showId) } returns cast
        coEvery { getSeasonEpisodesUseCase(showId) } returns seasonEpisodes

        // When
        viewModel.getCastAndEpisodes(showId)
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(castViewState, viewModel.listCastState.value)
        assertEquals(seasonEpisodesViewState, viewModel.listSeasonEpisodesState.value)
        assertEquals(ScreenViewState.Success, viewModel.screenState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getCastAndEpisodes should handle errors correctly`() = runTest {
        // Given
        val error = RuntimeException("Test Error")
        val showId = 1

        coEvery { getCastUseCase(showId) } throws error
        coEvery { getSeasonEpisodesUseCase(showId) } throws error

        // When
        viewModel.getCastAndEpisodes(showId)

        // Then
        val emittedError = viewModel.showError.first()
        assertEquals(error, emittedError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onFavoriteButtonClicked should switch favorite status correctly`() = runTest {
        // Given
        val show = ShowMocks.show
        val showViewState = ShowMocks.showViewState

        // Mocking the favorite state switch
        coEvery { switchFavoriteUseCase(show) } answers { /* Do nothing */ }
        coEvery { checkFavoriteUseCase(show.id) } returns true

        // When
        viewModel.onFavoriteButtonClicked(showViewState)
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        coVerify(exactly = 1) { switchFavoriteUseCase(show) }
        assertEquals(true, viewModel.favoriteState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `checkFavorite should update favoriteState correctly`() = runTest {
        // Given
        val show = ShowMocks.show
        val showViewState = ShowMocks.showViewState
        val isFavorite = true

        coEvery { checkFavoriteUseCase(show.id) } returns isFavorite

        // When
        viewModel.checkFavorite(showViewState)
        advanceUntilIdle() // Ensures all coroutines have completed

        // Then
        assertEquals(isFavorite, viewModel.favoriteState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onEpisodeClicked should emit the correct episode`() = runTest {
        // Given
        val episodeViewState = EpisodeMocks.episodeViewState

        // When
        viewModel.onEpisodeClicked(episodeViewState)

        // Then
        assertEquals(episodeViewState, viewModel.openEpisode.first())
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
}