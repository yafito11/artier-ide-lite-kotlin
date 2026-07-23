package com.artier.ide.lite.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artier.ide.lite.ui.theme.ArtierColors

/**
 * Main workspace layout - Cursor IDE style
 *
 * Layout:
 * ┌────┬────────────────────────────────┬───────────────────────┐
 * │    │                                │                       │
 * │ S  │   Editor Area                  │   AI Panel            │
 * │ i  │                                │   360dp wide          │
 * │ d  │                                │                       │
 * │ e  │   weight(1f)                   │                       │
 * │ b  │                                │                       │
 * │ a  │                                │                       │
 * │ r  │                                │                       │
 * │48dp├────┴────────────────────────────┴───────────────────────┤
 * │    │  Status bar (24dp)                                      │
 * └────┴─────────────────────────────────────────────────────────┘
 */
@Composable
fun MainWorkspace(
    modifier: Modifier = Modifier
) {
    var isSidebarVisible by remember { mutableStateOf(true) }
    var isAiPanelVisible by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ArtierColors.background)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Activity Bar (48dp) - Cursor/VS Code standard
            AnimatedVisibility(
                visible = isSidebarVisible,
                enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
            ) {
                Sidebar(
                    modifier = Modifier
                        .width(48.dp)
                        .fillMaxHeight()
                        .background(ArtierColors.surface)
                )
            }

            // Main content area (flex)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                EditorArea(
                    modifier = Modifier.fillMaxSize()
                )
            }

            // AI Panel (360dp) - Cursor Composer style
            AnimatedVisibility(
                visible = isAiPanelVisible,
                enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
            ) {
                AiPanel(
                    modifier = Modifier
                        .width(360.dp)
                        .fillMaxHeight()
                        .background(ArtierColors.surface)
                )
            }
        }

        // Status bar at bottom (24dp) - Cursor style
        StatusBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(ArtierColors.surfaceVariant)
        )
    }
}
