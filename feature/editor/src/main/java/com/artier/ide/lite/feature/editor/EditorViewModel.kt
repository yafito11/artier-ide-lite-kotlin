package com.artier.ide.lite.feature.editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.artier.ide.lite.core.model.EditorState
import com.artier.ide.lite.core.model.FileTab
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for editor state management
 */
@HiltViewModel
class EditorViewModel @Inject constructor() : ViewModel() {

    private val _editorState = mutableStateOf(EditorState())
    val editorState: EditorState by _editorState

    private val _tabs = mutableStateOf<List<FileTab>>(emptyList())
    val tabs: List<FileTab> by _tabs

    private val _activeTabId = mutableStateOf<String?>(null)
    val activeTabId: String? by _activeTabId

    /**
     * Open a file in a new tab
     */
    fun openFile(filePath: String, fileName: String, language: String = "text") {
        val existingTab = _tabs.value.find { it.filePath == filePath }
        if (existingTab != null) {
            _activeTabId.value = existingTab.id
            return
        }

        val newTab = FileTab(
            id = filePath.hashCode().toString(),
            filePath = filePath,
            fileName = fileName,
            language = language
        )
        _tabs.value = _tabs.value + newTab
        _activeTabId.value = newTab.id
    }

    /**
     * Close a tab
     */
    fun closeTab(tabId: String) {
        _tabs.value = _tabs.value.filter { it.id != tabId }
        if (_activeTabId.value == tabId) {
            _activeTabId.value = _tabs.value.lastOrNull()?.id
        }
    }

    /**
     * Switch to a tab
     */
    fun switchTab(tabId: String) {
        _activeTabId.value = tabId
    }

    /**
     * Mark tab as modified
     */
    fun markTabModified(tabId: String, modified: Boolean) {
        _tabs.value = _tabs.value.map {
            if (it.id == tabId) it.copy(isModified = modified) else it
        }
    }

    /**
     * Update cursor position
     */
    fun updateCursorPosition(line: Int, column: Int) {
        _editorState.value = _editorState.value.copy(
            cursorLine = line,
            cursorColumn = column
        )
    }

    /**
     * Update selection
     */
    fun updateSelection(start: Int?, end: Int?) {
        _editorState.value = _editorState.value.copy(
            selectionStart = start,
            selectionEnd = end
        )
    }

    /**
     * Get active tab
     */
    fun getActiveTab(): FileTab? {
        return _tabs.value.find { it.id == _activeTabId.value }
    }
}
