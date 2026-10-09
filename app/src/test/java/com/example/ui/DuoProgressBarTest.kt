package com.example.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.ui.components.DuoProgressBar
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class DuoProgressBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun duoProgressBar_initialZeroProgress_displaysCorrectly() {
        composeTestRule.setContent {
            DuoProgressBar(
                progress = 0f,
                currentStep = 0,
                totalSteps = 10,
                testTag = "test_progress_bar"
            )
        }

        composeTestRule.onNodeWithTag("test_progress_bar").assertIsDisplayed()
        composeTestRule.onNodeWithText("0 / 10").assertIsDisplayed()
    }

    @Test
    fun duoProgressBar_updatesWhenProgressAdvances() {
        val currentStep = 5
        val totalSteps = 10
        val progress = currentStep.toFloat() / totalSteps.toFloat()

        composeTestRule.setContent {
            DuoProgressBar(
                progress = progress,
                currentStep = currentStep,
                totalSteps = totalSteps,
                testTag = "test_progress_bar"
            )
        }

        composeTestRule.onNodeWithTag("test_progress_bar").assertIsDisplayed()
        composeTestRule.onNodeWithText("5 / 10").assertIsDisplayed()
    }

    @Test
    fun duoProgressBar_completedState_displaysFullProgress() {
        composeTestRule.setContent {
            DuoProgressBar(
                progress = 1f,
                currentStep = 10,
                totalSteps = 10,
                testTag = "test_progress_bar"
            )
        }

        composeTestRule.onNodeWithTag("test_progress_bar").assertIsDisplayed()
        composeTestRule.onNodeWithText("10 / 10").assertIsDisplayed()
    }
}
