package com.artier.ide.lite.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.artier.ide.lite.ui.theme.ArtierColors

/**
 * Editor area placeholder
 * Will be replaced with Sora Editor integration in Phase 2
 *
 * Background: #1F1F1F (Cursor/VS Code editor background)
 */
@Composable
fun EditorArea(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ArtierColors.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Editor Area\nSora Editor will be integrated here",
            style = MaterialTheme.typography.bodyLarge,
            color = ArtierColors.textMuted,
            fontFamily = FontFamily.Monospace
        )
    }
}
