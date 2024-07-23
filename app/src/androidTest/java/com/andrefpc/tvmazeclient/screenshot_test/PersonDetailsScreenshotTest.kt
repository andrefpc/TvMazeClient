package com.andrefpc.tvmazeclient.screenshot_test

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrefpc.tvmazeclient.presentation.compose.screen.people.PeopleViewModel
import com.andrefpc.tvmazeclient.presentation.compose.screen.person_details.PersonDetailsActivity
import com.andrefpc.tvmazeclient.presentation.compose.screen.person_details.PersonDetailsViewModel
import com.andrefpc.tvmazeclient.presentation.model.ScreenViewState
import com.andrefpc.tvmazeclient.util.PersonMocks
import com.andrefpc.tvmazeclient.util.ShowMocks
import com.karumi.shot.ScreenshotTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.update
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PersonDetailsScreenshotTest : ScreenshotTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var viewModel: PersonDetailsViewModel

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule<PersonDetailsActivity>(
        Intent(context, PersonDetailsActivity::class.java).apply {
            putExtra("person", PersonMocks.personViewState)
        }
    )

    @Before
    fun setup() {
        hiltRule.inject()
        activityScenarioRule.scenario.onActivity { activity ->
            viewModel = activity.viewModel
            viewModel._listShowState.update { listOf(ShowMocks.showViewState) }
            viewModel._screenState.update { ScreenViewState.Success }
        }
    }

    @Test
    fun testTakeScreenshot() {
        waitForAnimationsToFinish()
        activityScenarioRule.scenario.onActivity { activity ->
            compareScreenshot(activity, name = "PersonDetailsActivity_screenshot")
        }
    }
}