package com.artier.ide.lite.feature.terminal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.artier.ide.lite.ui.theme.ArtierColors

/**
 * Terminal view composable placeholder
 */
@Composable
fun TerminalView(
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            ArtierTerminalView(context)
        },
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(ArtierColors.background)
    )
}
