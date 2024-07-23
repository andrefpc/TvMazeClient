package com.andrefpc.tvmazeclient.ui_test
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.presentation.compose.screen.chooser.ChooserActivity
import com.andrefpc.tvmazeclient.presentation.compose.screen.chooser.ChooserViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ChooserViewTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ChooserActivity>()

    lateinit var viewModel: ChooserViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.activityRule.scenario.onActivity { activity ->
            viewModel = activity.viewModel
        }
    }

    @Test
    fun testJetpackComposeButtonClick() = runBlocking {
        // Click the Jetpack Compose button
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.jetpack_compose)).performClick()

        assert(viewModel.openMain.first() == Unit)
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

    @Test
    fun testChooseEvaluateTextDisplayed() {
        // Verify the choose_evaluate text is displayed
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.choose_evaluate)).assertExists()
    }
}