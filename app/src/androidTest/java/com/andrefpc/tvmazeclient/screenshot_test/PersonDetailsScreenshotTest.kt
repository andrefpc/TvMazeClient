package com.andrefpc.tvmazeclient.screenshot_test

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrefpc.tvmazeclient.presentation.compose.screen.person_details.PersonDetailsActivity
import com.andrefpc.tvmazeclient.util.PersonMocks
import com.karumi.shot.ScreenshotTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PersonDetailsScreenshotTest : ScreenshotTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule<PersonDetailsActivity>(
        Intent(context, PersonDetailsActivity::class.java).apply {
            putExtra("person", PersonMocks.person)
        }
    )

    @Test
    fun testTakeScreenshot() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.intent.putExtra("person", PersonMocks.person)
            compareScreenshot(activity, name = "PersonDetailsActivity_screenshot")
        }
    }
}