package com.artier.ide.lite

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.artier.ide.lite.ui.theme.ArtierTheme
import com.artier.ide.lite.ui.components.Sidebar
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for Sidebar component
 */
@RunWith(AndroidJUnit4::class)
class SidebarUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun sidebar_allIcons_areDisplayed() {
        composeTestRule.setContent {
            ArtierTheme {
                Sidebar()
            }
        }

        // Verify all sidebar icons
        composeTestRule.onAllNodesWithContentDescription("File Explorer").assertCountEquals(1)
        composeTestRule.onAllNodesWithContentDescription("AI Assistant").assertCountEquals(1)
        composeTestRule.onAllNodesWithContentDescription("Extensions").assertCountEquals(1)
        composeTestRule.onAllNodesWithContentDescription("Settings").assertCountEquals(1)
    }

    @Test
    fun sidebar_fileExplorer_isClickable() {
        composeTestRule.setContent {
            ArtierTheme {
                Sidebar()
            }
        }

        // Verify file explorer icon is clickable
        composeTestRule.onNodeWithContentDescription("File Explorer").assertIsEnabled()
    }

    @Test
    fun sidebar_aiAssistant_isClickable() {
        composeTestRule.setContent {
            ArtierTheme {
                Sidebar()
            }
        }

        // Verify AI assistant icon is clickable
        composeTestRule.onNodeWithContentDescription("AI Assistant").assertIsEnabled()
    }
}
