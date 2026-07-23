package com.artier.ide.lite.feature.fileexplorer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.artier.ide.lite.core.model.FileNode
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileExplorerViewModel @Inject constructor() : ViewModel() {

    val fileTree = mutableStateOf<List<FileNode>>(emptyList())
    val selectedFile = mutableStateOf<String?>(null)

    private var rootPath: String? = null

    fun openDirectory(path: String) {
        rootPath = path
        val root = File(path)
        if (root.exists() && root.isDirectory) {
            fileTree.value = root.listFiles()?.map { file ->
                if (file.isDirectory) {
                    FileNode.Directory(
                        name = file.name,
                        path = file.absolutePath,
                        children = emptyList(),
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
            }?.sortedWith(compareBy<FileNode> { it is FileNode.Directory }.thenBy { it.name }) ?: emptyList()
        }
    }

    fun toggleDirectory(path: String) {
        val current = fileTree.value.toMutableList()
        val index = current.indexOfFirst { it is FileNode.Directory && it.path == path }
        if (index >= 0) {
            val dir = current[index] as FileNode.Directory
            if (dir.isExpanded) {
                current[index] = dir.copy(isExpanded = false, children = emptyList())
            } else {
                val children = File(path).listFiles()?.map { file ->
                    if (file.isDirectory) {
                        FileNode.Directory(
                            name = file.name,
                            path = file.absolutePath,
                            children = emptyList(),
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
                }?.sortedWith(compareBy<FileNode> { it is FileNode.Directory }.thenBy { it.name }) ?: emptyList()
                current[index] = dir.copy(isExpanded = true, children = children)
            }
            fileTree.value = current
        }
    }
}
