package com.andrefpc.tvmazeclient.screenshot_test

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrefpc.tvmazeclient.presentation.compose.screen.show_details.ShowDetailsActivity
import com.andrefpc.tvmazeclient.presentation.compose.screen.show_details.ShowDetailsViewModel
import com.andrefpc.tvmazeclient.presentation.compose.screen.shows.ShowsViewModel
import com.andrefpc.tvmazeclient.presentation.model.ScreenViewState
import com.andrefpc.tvmazeclient.util.CastMocks
import com.andrefpc.tvmazeclient.util.SeasonEpisodeMocks
import com.andrefpc.tvmazeclient.util.ShowMocks
import com.karumi.shot.ScreenshotTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ShowDetailsScreenshotTest : ScreenshotTest {
    private lateinit var viewModel: ShowDetailsViewModel
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule<ShowDetailsActivity>(
        Intent(context, ShowDetailsActivity::class.java).apply {
            putExtra("show", ShowMocks.showViewState)
        }
    )
    @Before
    fun setup() {
        hiltRule.inject()

        activityScenarioRule.scenario.onActivity { activity ->
            viewModel = activity.viewModel
            viewModel._listSeasonEpisodesState.update { listOf(SeasonEpisodeMocks.seasonEpisodesViewState) }
            viewModel._listCastState.update { listOf(CastMocks.castViewState) }
            viewModel._screenState.update { ScreenViewState.Success }
        }
    }

    @Test
    fun testTakeScreenshot() {
        waitForAnimationsToFinish()
        activityScenarioRule.scenario.onActivity { activity ->
            compareScreenshot(activity, name = "ShowDetailsActivity_screenshot")
        }
    }
}