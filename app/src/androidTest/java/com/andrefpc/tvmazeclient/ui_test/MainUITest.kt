package com.andrefpc.tvmazeclient.ui_test

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.presentation.compose.screen.main.MainActivity
import com.andrefpc.tvmazeclient.presentation.compose.screen.main.MainViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainUITest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.activityRule.scenario.onActivity { activity ->
            viewModel = activity.viewModel
        }
    }

    @Test
    fun testPinFieldAndButtonClick() {
        // Type PIN in the OutlinedTextField
        composeTestRule.onNodeWithTag("PinField").performTextInput("1234")

        // Verify PIN field contains the correct value
        composeTestRule.onNodeWithTag("PinField").assertTextContains("1234")

        // Click the PIN button
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.pin)).performClick()

        // Verify the ViewModel's method was called (this would typically be done via a mock or spy)
    }

    @Test
    fun testBiometricsButtonClick() {
        // Click the Biometrics button
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.phone_authentication)).performClick()

        // Verify the ViewModel's method was called (this would typically be done via a mock or spy)
    }

    @Test
    fun testLottieAnimationDisplayed() {
        // Verify the LottieAnimation is displayed
        composeTestRule.onNodeWithTag("Lottie").assertExists()
    }

    @Test
    fun testToolbarDisplayed() {
        // Verify the CustomToolbar is displayed
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.app_name)).assertExists()
    }
}