package com.artier.ide.lite

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.artier.ide.lite.ui.theme.ArtierTheme
import com.artier.ide.lite.ui.components.MainWorkspace
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for Artier IDE Lite
 * Tests the main workspace layout and components
 */
@RunWith(AndroidJUnit4::class)
class MainWorkspaceUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mainWorkspace_isDisplayed() {
        composeTestRule.setContent {
            ArtierTheme {
                MainWorkspace()
            }
        }

        // Verify the main workspace is displayed
        composeTestRule.onRoot().assertIsDisplayed()
    }

    @Test
    fun sidebar_isDisplayed() {
        composeTestRule.setContent {
            ArtierTheme {
                MainWorkspace()
            }
        }

        // Verify sidebar icons are displayed
        composeTestRule.onAllNodesWithContentDescription("File Explorer").assertCountEquals(1)
        composeTestRule.onAllNodesWithContentDescription("AI Assistant").assertCountEquals(1)
        composeTestRule.onAllNodesWithContentDescription("Settings").assertCountEquals(1)
    }

    @Test
    fun statusBar_isDisplayed() {
        composeTestRule.setContent {
            ArtierTheme {
                MainWorkspace()
            }
        }

        // Verify status bar items
        composeTestRule.onNodeWithText("Agent Mode").assertIsDisplayed()
        composeTestRule.onNodeWithText("main").assertIsDisplayed()
        composeTestRule.onNodeWithText("UTF-8").assertIsDisplayed()
    }

    @Test
    fun editorArea_isDisplayed() {
        composeTestRule.setContent {
            ArtierTheme {
                MainWorkspace()
            }
        }

        // Verify editor area placeholder
        composeTestRule.onNodeWithText("Editor Area").assertIsDisplayed()
    }
}
