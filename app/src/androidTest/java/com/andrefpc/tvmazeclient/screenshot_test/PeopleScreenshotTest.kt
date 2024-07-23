package com.andrefpc.tvmazeclient.screenshot_test

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrefpc.tvmazeclient.presentation.compose.screen.people.PeopleActivity
import com.andrefpc.tvmazeclient.presentation.compose.screen.people.PeopleViewModel
import com.andrefpc.tvmazeclient.presentation.model.ScreenViewState
import com.andrefpc.tvmazeclient.util.PersonMocks
import com.karumi.shot.ScreenshotTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class PeopleScreenshotTest : ScreenshotTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: PeopleViewModel
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(PeopleActivity::class.java)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        hiltRule.inject()
        activityScenarioRule.scenario.onActivity { activity ->
            viewModel = activity.viewModel
            viewModel._listPeopleState.update { listOf(PersonMocks.personViewState) }
            viewModel._screenState.update { ScreenViewState.Success }
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun testTakeScreenshot() {
        testDispatcher.scheduler.advanceUntilIdle()
        waitForAnimationsToFinish()
        activityScenarioRule.scenario.onActivity { activity ->
            compareScreenshot(activity, name = "PeopleActivity_screenshot")
        }
    }
}