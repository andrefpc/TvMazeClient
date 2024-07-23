package com.andrefpc.tvmazeclient.screenshot_test

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrefpc.tvmazeclient.presentation.compose.screen.favorites.FavoritesActivity
import com.andrefpc.tvmazeclient.presentation.compose.screen.favorites.FavoritesViewModel
import com.andrefpc.tvmazeclient.presentation.model.ScreenViewState
import com.andrefpc.tvmazeclient.util.ShowMocks
import com.karumi.shot.ScreenshotTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
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
class FavoritesScreenshotTest : ScreenshotTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: FavoritesViewModel

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    /*@get:Rule
    val activityScenarioRule = ActivityScenarioRule(FavoritesActivity::class.java)*/

    @get:Rule
    val composeRule = createAndroidComposeRule<FavoritesActivity>()
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        hiltRule.inject()

        composeRule.activityRule.scenario.onActivity { activity ->
            viewModel = activity.viewModel
            //viewModel._listShowState.update { listOf(ShowMocks.showViewState) }
            //viewModel._screenState.update { ScreenViewState.Success }
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
        composeRule.waitUntil(timeoutMillis = 5000) {
            val uiState = viewModel.screenState.value
            uiState == ScreenViewState.Success || uiState == ScreenViewState.Empty
        }
        composeRule.activityRule.scenario.onActivity {
            compareScreenshot(it, name = "FavoritesActivity_screenshot")
        }
    }
}