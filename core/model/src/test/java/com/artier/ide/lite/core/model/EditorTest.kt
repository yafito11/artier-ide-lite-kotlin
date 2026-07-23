package com.artier.ide.lite.core.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for Editor model
 */
class EditorTest {

    @Test
    fun fileTab_creation() {
        val tab = FileTab(
            id = "1",
            filePath = "/path/to/file.kt",
            fileName = "file.kt",
            isModified = false,
            language = "kotlin"
        )

        assertEquals("1", tab.id)
        assertEquals("/path/to/file.kt", tab.filePath)
        assertEquals("file.kt", tab.fileName)
        assertFalse(tab.isModified)
        assertEquals("kotlin", tab.language)
    }

    @Test
    fun fileTab_modified() {
        val tab = FileTab(
            id = "1",
            filePath = "/path/to/file.kt",
            fileName = "file.kt",
            isModified = true,
            language = "kotlin"
        )

        assertTrue(tab.isModified)
    }

    @Test
    fun editorState_default() {
        val state = EditorState()

        assertTrue(state.tabs.isEmpty())
        assertNull(state.activeTabId)
        assertEquals(1, state.cursorLine)
        assertEquals(1, state.cursorColumn)
        assertNull(state.selectionStart)
        assertNull(state.selectionEnd)
    }

    @Test
    fun editorState_withTabs() {
        val tabs = listOf(
            FileTab("1", "/path/file1.kt", "file1.kt", language = "kotlin"),
            FileTab("2", "/path/file2.kt", "file2.kt", language = "kotlin")
        )
        val state = EditorState(
            tabs = tabs,
            activeTabId = "1"
        )

        assertEquals(2, state.tabs.size)
        assertEquals("1", state.activeTabId)
    }

    @Test
    fun fileNode_file() {
        val file = FileNode.File(
            name = "test.kt",
            path = "/path/test.kt",
            extension = "kt",
            size = 1024
        )

        assertEquals("test.kt", file.name)
        assertEquals("/path/test.kt", file.path)
        assertEquals("kt", file.extension)
        assertEquals(1024, file.size)
    }

    @Test
    fun fileNode_directory() {
        val dir = FileNode.Directory(
            name = "src",
            path = "/path/src",
            children = emptyList(),
            isExpanded = false
        )

        assertEquals("src", dir.name)
        assertEquals("/path/src", dir.path)
        assertTrue(dir.children.isEmpty())
        assertFalse(dir.isExpanded)
    }
}
