package com.andrefpc.tvmazeclient.ui_test

import android.content.Intent
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrefpc.tvmazeclient.presentation.compose.screen.person_details.PersonDetailsActivity
import com.andrefpc.tvmazeclient.presentation.compose.screen.person_details.PersonDetailsViewModel
import com.andrefpc.tvmazeclient.presentation.model.ScreenViewState
import com.andrefpc.tvmazeclient.util.PersonMocks
import com.andrefpc.tvmazeclient.util.createAndroidIntentComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class PersonDetailsUITest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidIntentComposeRule<PersonDetailsActivity> {
        Intent(it, PersonDetailsActivity::class.java).apply {
            putExtra("person", PersonMocks.personViewState)
        }
    }

    private lateinit var viewModel: PersonDetailsViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.activityRule.scenario.onActivity { activity ->
            viewModel = activity.viewModel
        }
    }

    @Test
    fun testShowsViewDisplaysSuccessView() {
        // Simulate the ViewModel state change
        composeTestRule.runOnUiThread {
            viewModel._screenState.value = ScreenViewState.Success
        }

        // Verify that the Success view is displayed
        composeTestRule.onNodeWithTag("PersonDetailsSuccessView").assertExists()
    }

    @Test
    fun testShowsViewDisplaysErrorView() {
        // Simulate the ViewModel state change
        composeTestRule.runOnUiThread {
            viewModel._screenState.value = ScreenViewState.Error(Throwable("An error occurred"))
        }

        // Verify that the Error view is displayed
        composeTestRule.onNodeWithTag("ErrorView").assertExists()
    }

    @Test
    fun testShowsViewDisplaysLoadingView() {
        // Simulate the ViewModel state change
        composeTestRule.runOnUiThread {
            viewModel._screenState.value = ScreenViewState.Loading
        }

        // Verify that the Loading view is displayed
        composeTestRule.onNodeWithTag("ShimmerView").assertExists()
    }
}