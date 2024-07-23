package com.andrefpc.tvmazeclient.screenshot_test

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrefpc.tvmazeclient.presentation.compose.screen.chooser.ChooserActivity
import com.karumi.shot.ScreenshotTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ChooserScreenshotTest : ScreenshotTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(ChooserActivity::class.java)

    @Test
    fun testTakeScreenshot() {
        activityScenarioRule.scenario.onActivity { activity ->
            compareScreenshot(activity, name = "ChooserActivity_screenshot")
        }
    }
}