package com.artier.ide.lite.core.model

import kotlinx.serialization.Serializable

/**
 * File tab in editor
 */
@Serializable
data class FileTab(
    val id: String,
    val filePath: String,
    val fileName: String,
    val isModified: Boolean = false,
    val language: String = "text"
)

/**
 * Editor state
 */
@Serializable
data class EditorState(
    val tabs: List<FileTab> = emptyList(),
    val activeTabId: String? = null,
    val cursorLine: Int = 1,
    val cursorColumn: Int = 1,
    val selectionStart: Int? = null,
    val selectionEnd: Int? = null
)

/**
 * File explorer node
 */
sealed class FileNode {
    abstract val name: String
    abstract val path: String

    data class File(
        override val name: String,
        override val path: String,
        val extension: String,
        val size: Long
    ) : FileNode()

    data class Directory(
        override val name: String,
        override val path: String,
        val children: List<FileNode> = emptyList(),
        val isExpanded: Boolean = false
    ) : FileNode()
}
