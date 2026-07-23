package com.artier.ide.lite.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artier.ide.lite.ui.theme.ArtierColors

/**
 * Status bar at the bottom - Cursor IDE style
 *
 * Shows: Mode, branch, line:col, encoding, language
 * Height: 24dp (consistent with VS Code/Cursor)
 */
@Composable
fun StatusBar(
    modifier: Modifier = Modifier,
    mode: String = "Agent Mode",
    branch: String = "main",
    line: Int = 1,
    column: Int = 1,
    encoding: String = "UTF-8",
    language: String = "Kotlin"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left side - Project/mode info
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatusBarItem(text = mode)
            StatusBarItem(text = branch)
        }

        // Right side - Editor info
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatusBarItem(text = "Ln $line, Col $column")
            StatusBarItem(text = encoding)
            StatusBarItem(text = language)
        }
    }
}

@Composable
private fun StatusBarItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = ArtierColors.onSurface,
        modifier = modifier
    )
}
