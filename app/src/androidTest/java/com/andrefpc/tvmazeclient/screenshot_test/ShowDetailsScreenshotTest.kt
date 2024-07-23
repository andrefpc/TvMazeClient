package com.andrefpc.tvmazeclient.screenshot_test

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrefpc.tvmazeclient.presentation.compose.screen.show_details.ShowDetailsActivity
import com.andrefpc.tvmazeclient.util.ShowMocks
import com.karumi.shot.ScreenshotTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule<ShowDetailsActivity>(
        Intent(context, ShowDetailsActivity::class.java).apply {
            putExtra("show", ShowMocks.show)
        }
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testTakeScreenshot() {
        activityScenarioRule.scenario.onActivity { activity ->
            testScope.advanceUntilIdle()
            compareScreenshot(activity, name = "ShowDetailsActivity_screenshot")
        }
    }
}