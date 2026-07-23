package com.artier.ide.lite

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.artier.ide.lite.ui.theme.ArtierTheme
import com.artier.ide.lite.feature.aipanel.AiPanel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for AI Panel component
 */
@RunWith(AndroidJUnit4::class)
class AiPanelUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun aiPanel_isDisplayed() {
        composeTestRule.setContent {
            ArtierTheme {
                AiPanel()
            }
        }

        // Verify AI panel title
        composeTestRule.onNodeWithText("AI Assistant").assertIsDisplayed()
    }

    @Test
    fun aiPanel_sourceSelector_isDisplayed() {
        composeTestRule.setContent {
            ArtierTheme {
                AiPanel()
            }
        }

        // Verify source selector
        composeTestRule.onNodeWithText("Sumber").assertIsDisplayed()
        composeTestRule.onNodeWithText("Built-in Agent").assertIsDisplayed()
    }

    @Test
    fun aiPanel_modeSelector_isDisplayed() {
        composeTestRule.setContent {
            ArtierTheme {
                AiPanel()
            }
        }

        // Verify mode selector
        composeTestRule.onNodeWithText("Mode").assertIsDisplayed()
        composeTestRule.onNodeWithText("Agent").assertIsDisplayed()
    }

    @Test
    fun aiPanel_inputField_isDisplayed() {
        composeTestRule.setContent {
            ArtierTheme {
                AiPanel()
            }
        }

        // Verify input field
        composeTestRule.onNodeWithText("Ask anything...").assertIsDisplayed()
    }

    @Test
    fun aiPanel_emptyMessage_isDisplayed() {
        composeTestRule.setContent {
            ArtierTheme {
                AiPanel()
            }
        }

        // Verify empty message
        composeTestRule.onNodeWithText("Ask anything about your code...").assertIsDisplayed()
    }
}
