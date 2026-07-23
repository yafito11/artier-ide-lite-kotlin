package com.artier.ide.lite.feature.editor

import android.content.Context
import android.util.AttributeSet
import io.github.rosemoe.sora.widget.CodeEditor

/**
 * Custom CodeEditor wrapper with Artier IDE styling
 */
class ArtierCodeEditor @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CodeEditor(context, attrs, defStyleAttr) {

    init {
        setupEditor()
    }

    private fun setupEditor() {
        isLineNumberEnabled = true
        isHighlightCurrentLine = true
        setTabWidth(4)
    }

    fun insertTextAtCursor(text: String) {
        val cursor = cursor
        this.text.insert(cursor.leftLine, cursor.leftColumn, text)
    }

    fun replaceText(startLine: Int, startColumn: Int, endLine: Int, endColumn: Int, newText: String) {
        text.delete(startLine, startColumn, endLine, endColumn)
        text.insert(startLine, startColumn, newText)
    }

    fun getCurrentLine(): Int = cursor.leftLine

    fun getCurrentColumn(): Int = cursor.leftColumn

    fun getSelectedText(): String? {
        val selection = cursor
        return if (selection.isSelected) {
            text.substring(selection.left, selection.right)
        } else {
            null
        }
    }
}
