package com.artier.ide.lite.feature.terminal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.artier.ide.lite.ui.theme.ArtierColors

/**
 * Compose wrapper for TerminalView with keyboard toolbar
 */
@Composable
fun TerminalView(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val terminalView = remember { ArtierTerminalView(context) }

    androidx.compose.foundation.layout.Column(
        modifier = modifier
    ) {
        // Keyboard toolbar for special keys
        KeyboardToolbar(
            onKeyClick = { key ->
                when (key) {
                    "TAB" -> terminalView.sendInput("\t")
                    "ESC" -> terminalView.sendInput("")
                    "CTRL" -> { /* Toggle ctrl mode */ }
                    "ALT" -> { /* Toggle alt mode */ }
                    "UP" -> terminalView.sendInput("[A")
                    "DOWN" -> terminalView.sendInput("[B")
                    "LEFT" -> terminalView.sendInput("[D")
                    "RIGHT" -> terminalView.sendInput("[C")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(ArtierColors.surfaceVariant)
        )

        // Terminal view
        AndroidView(
            factory = { terminalView },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onRelease = { view ->
                view.finishSession()
            }
        )
    }
}

@Composable
private fun KeyboardToolbar(
    onKeyClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        ToolbarKey(text = "TAB", onClick = { onKeyClick("TAB") })
        ToolbarKey(text = "ESC", onClick = { onKeyClick("ESC") })
        ToolbarKey(text = "CTRL", onClick = { onKeyClick("CTRL") })
        ToolbarKey(text = "ALT", onClick = { onKeyClick("ALT") })
        ToolbarKey(text = "↑", onClick = { onKeyClick("UP") })
        ToolbarKey(text = "↓", onClick = { onKeyClick("DOWN") })
        ToolbarKey(text = "←", onClick = { onKeyClick("LEFT") })
        ToolbarKey(text = "→", onClick = { onKeyClick("RIGHT") })
    }
}

@Composable
private fun ToolbarKey(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.height(32.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = ArtierColors.onSurface
        )
    }
}
