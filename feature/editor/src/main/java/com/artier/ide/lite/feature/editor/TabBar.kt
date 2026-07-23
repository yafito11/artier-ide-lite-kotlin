package com.artier.ide.lite.feature.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artier.ide.lite.core.model.FileTab
import com.artier.ide.lite.ui.theme.ArtierColors

/**
 * Tab bar for open files - Cursor IDE style
 */
@Composable
fun TabBar(
    tabs: List<FileTab>,
    activeTabId: String?,
    onTabClick: (String) -> Unit,
    onTabClose: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(ArtierColors.surfaceVariant)
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        tabs.forEach { tab ->
            TabItem(
                tab = tab,
                isActive = tab.id == activeTabId,
                onClick = { onTabClick(tab.id) },
                onClose = { onTabClose(tab.id) }
            )
        }
    }
}

@Composable
private fun TabItem(
    tab: FileTab,
    isActive: Boolean,
    onClick: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isActive) ArtierColors.background else ArtierColors.surfaceVariant
    val textColor = if (isActive) ArtierColors.onBackground else ArtierColors.textMuted

    Row(
        modifier = modifier
            .height(36.dp)
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = tab.fileName + if (tab.isModified) " *" else "",
            style = MaterialTheme.typography.labelMedium,
            color = textColor
        )

        IconButton(
            onClick = onClose,
            modifier = Modifier.height(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close tab",
                tint = ArtierColors.textMuted
            )
        }
    }
}
