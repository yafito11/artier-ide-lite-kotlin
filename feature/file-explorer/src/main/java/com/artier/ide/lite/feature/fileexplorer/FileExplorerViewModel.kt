package com.artier.ide.lite.feature.fileexplorer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.artier.ide.lite.core.model.FileNode
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

/**
 * ViewModel for file explorer
 */
@HiltViewModel
class FileExplorerViewModel @Inject constructor() : ViewModel() {

    private val _fileTree = mutableStateOf<List<FileNode>>(emptyList())
    val fileTree: List<FileNode> by _fileTree

    private val _selectedFile = mutableStateOf<String?>(null)
    val selectedFile: String? by _selectedFile

    private var rootPath: String? = null

    /**
     * Load directory structure
     */
    fun loadDirectory(path: String) {
        rootPath = path
        _fileTree.value = scanDirectory(File(path))
    }

    /**
     * Scan directory and build file tree
     */
    private fun scanDirectory(directory: File): List<FileNode> {
        if (!directory.exists() || !directory.isDirectory) return emptyList()

        return directory.listFiles()
            ?.filter { !it.name.startsWith(".") } // Hide hidden files
            ?.sortedWith(compareBy<File> { !it.isDirectory }.thenBy { it.name.lowercase() })
            ?.map { file ->
                if (file.isDirectory) {
                    FileNode.Directory(
                        name = file.name,
                        path = file.absolutePath,
                        children = scanDirectory(file),
                        isExpanded = false
                    )
                } else {
                    FileNode.File(
                        name = file.name,
                        path = file.absolutePath,
                        extension = file.extension,
                        size = file.length()
                    )
                }
            } ?: emptyList()
    }

    /**
     * Toggle directory expansion
     */
    fun toggleDirectory(path: String) {
        _fileTree.value = toggleNode(_fileTree.value, path)
    }

    private fun toggleNode(nodes: List<FileNode>, path: String): List<FileNode> {
        return nodes.map { node ->
            when (node) {
                is FileNode.Directory -> {
                    if (node.path == path) {
                        node.copy(isExpanded = !node.isExpanded)
                    } else {
                        node.copy(children = toggleNode(node.children, path))
                    }
                }
                is FileNode.File -> node
            }
        }
    }

    /**
     * Select a file
     */
    fun selectFile(path: String) {
        _selectedFile.value = path
    }

    /**
     * Get file extension
     */
    fun getFileExtension(path: String): String {
        return File(path).extension
    }

    /**
     * Get language from extension
     */
    fun getLanguageFromExtension(extension: String): String {
        return when (extension.lowercase()) {
            "kt", "kts" -> "kotlin"
            "java" -> "java"
            "xml" -> "xml"
            "json" -> "json"
            "js", "jsx" -> "javascript"
            "ts", "tsx" -> "typescript"
            "py" -> "python"
            "md" -> "markdown"
            "yaml", "yml" -> "yaml"
            "gradle" -> "groovy"
            else -> "text"
        }
    }
}
