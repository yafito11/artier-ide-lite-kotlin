package com.artier.ide.lite.feature.fileexplorer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.artier.ide.lite.core.model.FileNode
import com.artier.ide.lite.ui.theme.ArtierColors

/**
 * File explorer panel - Cursor IDE style
 */
@Composable
fun FileExplorer(
    onFileClick: (String, String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FileExplorerViewModel = hiltViewModel()
) {
    val fileTree by viewModel.fileTree.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ArtierColors.surface)
            .padding(8.dp)
    ) {
        Text(
            text = "EXPLORER",
            style = MaterialTheme.typography.labelMedium,
            color = ArtierColors.textMuted,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(fileTree) { node ->
                FileNodeItem(
                    node = node,
                    depth = 0,
                    onFileClick = onFileClick,
                    onDirectoryClick = { viewModel.toggleDirectory(it) }
                )
            }
        }
    }
}

@Composable
private fun FileNodeItem(
    node: FileNode,
    depth: Int,
    onFileClick: (String, String, String) -> Unit,
    onDirectoryClick: (String) -> Unit
) {
    when (node) {
        is FileNode.Directory -> {
            DirectoryItem(
                node = node,
                depth = depth,
                onClick = { onDirectoryClick(node.path) }
            )
            if (node.isExpanded) {
                node.children.forEach { child ->
                    FileNodeItem(
                        node = child,
                        depth = depth + 1,
                        onFileClick = onFileClick,
                        onDirectoryClick = onDirectoryClick
                    )
                }
            }
        }
        is FileNode.File -> {
            FileItem(
                node = node,
                depth = depth,
                onClick = { onFileClick(node.path, node.name, node.extension) }
            )
        }
    }
}

@Composable
private fun DirectoryItem(
    node: FileNode.Directory,
    depth: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .background(ArtierColors.surface)
            .clickable(onClick = onClick)
            .padding(start = (depth * 16 + 8).dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = if (node.isExpanded) Icons.Default.FolderOpen else Icons.Default.Folder,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = ArtierColors.primary
        )

        Text(
            text = node.name,
            style = MaterialTheme.typography.bodySmall,
            color = ArtierColors.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FileItem(
    node: FileNode.File,
    depth: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .background(ArtierColors.surface)
            .clickable(onClick = onClick)
            .padding(start = (depth * 16 + 8).dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.InsertDriveFile,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = ArtierColors.textMuted
        )

        Text(
            text = node.name,
            style = MaterialTheme.typography.bodySmall,
            color = ArtierColors.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
