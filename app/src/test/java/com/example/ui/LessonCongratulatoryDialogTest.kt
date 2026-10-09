package com.example.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.example.ui.components.LessonCongratulatoryDialog
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class LessonCongratulatoryDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun lessonCongratulatoryDialog_rendersScoreAndReturnButton() {
        var returnClicked = false

        composeTestRule.setContent {
            LessonCongratulatoryDialog(
                score = 95,
                xpEarned = 15,
                heartsLeft = 5,
                lessonTitle = "Alif, Ba, Ta, Sa",
                accuracyPercent = 100,
                onReturnToDashboard = { returnClicked = true }
            )
        }

        // Verify elements are displayed
        composeTestRule.onNodeWithTag("congratulatory_dialog").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ajoyib Natija!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Alif, Ba, Ta, Sa").assertIsDisplayed()
        composeTestRule.onNodeWithText("+15 XP", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("5 / 5", substring = true).assertIsDisplayed()

        // Verify return to dashboard button works
        val returnButton = composeTestRule.onNodeWithTag("return_to_dashboard_button")
        returnButton.performScrollTo().assertIsDisplayed()
        returnButton.performClick()

        assertTrue("Clicking return button should trigger callback", returnClicked)
    }
}
