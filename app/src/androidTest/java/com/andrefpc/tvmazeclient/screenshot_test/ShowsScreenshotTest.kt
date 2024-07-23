package com.andrefpc.tvmazeclient.screenshot_test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrefpc.tvmazeclient.presentation.compose.screen.shows.ShowsActivity
import com.andrefpc.tvmazeclient.presentation.compose.screen.shows.ShowsViewModel
import com.andrefpc.tvmazeclient.presentation.model.ScreenViewState
import com.karumi.shot.ScreenshotTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
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
class ShowsScreenshotTest : ScreenshotTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: ShowsViewModel

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<ShowsActivity>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        hiltRule.inject()
        composeRule.activityRule.scenario.onActivity { activity ->
            viewModel = activity.viewModel
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun testTakeScreenshot() {
        composeRule.waitUntil(timeoutMillis = 5000) {
            val uiState = viewModel.screenState.value
            val listShowState = viewModel.listShowState.value
            uiState == ScreenViewState.Success || listShowState.isNotEmpty()
        }

        composeRule.activityRule.scenario.onActivity {
            compareScreenshot(it, name = "ShowsActivity_screenshot")
        }
    }
}