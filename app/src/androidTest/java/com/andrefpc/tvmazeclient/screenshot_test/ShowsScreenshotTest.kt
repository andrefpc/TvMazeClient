package com.andrefpc.tvmazeclient.screenshot_test

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrefpc.tvmazeclient.presentation.model.ScreenViewState
import com.andrefpc.tvmazeclient.presentation.compose.screen.shows.ShowsActivity
import com.andrefpc.tvmazeclient.presentation.compose.screen.shows.ShowsViewModel
import com.karumi.shot.ScreenshotTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ShowsScreenshotTest : ScreenshotTest {
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: ShowsViewModel

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(ShowsActivity::class.java)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        hiltRule.inject()

        activityScenarioRule.scenario.onActivity { activity ->
            viewModel = activity.viewModel
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testTakeScreenshot() = runTest {
        viewModel.screenState.first { it == ScreenViewState.Success }
        advanceUntilIdle()
        activityScenarioRule.scenario.onActivity { activity ->
            compareScreenshot(activity, name = "ShowsActivity_screenshot")
        }
    }
}