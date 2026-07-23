package com.artier.ide.lite.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.artier.ide.lite.ui.theme.ArtierColors

/**
 * Activity Bar with icon buttons - Cursor IDE style
 *
 * Width: 48dp (consistent with VS Code/Cursor)
 * Icons:
 * 📁 File Explorer
 * 🤖 AI Assistant
 * 🧩 Extensions
 * ⚙ Settings
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sidebar(
    modifier: Modifier = Modifier,
    onFileExplorerClick: () -> Unit = {},
    onAiAssistantClick: () -> Unit = {},
    onExtensionsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .width(48.dp)
            .fillMaxHeight()
            .background(ArtierColors.surface)
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SidebarIcon(
            icon = Icons.Default.Folder,
            contentDescription = "File Explorer",
            onClick = onFileExplorerClick
        )

        Spacer(modifier = Modifier.weight(1f))

        SidebarIcon(
            icon = Icons.Default.Psychology,
            contentDescription = "AI Assistant",
            onClick = onAiAssistantClick
        )

        SidebarIcon(
            icon = Icons.Default.Extension,
            contentDescription = "Extensions",
            onClick = onExtensionsClick
        )

        SidebarIcon(
            icon = Icons.Default.Settings,
            contentDescription = "Settings",
            onClick = onSettingsClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SidebarIcon(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tooltipState = rememberTooltipState()

    TooltipBox(
        positionProvider = TooltipState.rememberTooltipPositionProvider(),
        tooltip = {
            PlainTooltip {
                androidx.compose.material3.Text(contentDescription)
            }
        },
        state = tooltipState
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = modifier
                .size(40.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable(onClick = onClick)
                .padding(4.dp),
            tint = ArtierColors.onSurface
        )
    }
}
