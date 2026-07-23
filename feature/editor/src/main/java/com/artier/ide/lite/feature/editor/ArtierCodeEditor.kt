package com.artier.ide.lite.feature.editor

import android.content.Context
import android.util.AttributeSet
import io.github.rosemoe.sora.langs.textmate.TextMateColorScheme
import io.github.rosemoe.sora.langs.textmate.TextMateLanguage
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme

/**
 * Custom CodeEditor wrapper with Artier IDE styling
 * Extends Sora Editor with Artier's color scheme and configuration
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
        // Enable line numbers
        isLineNumberEnabled = true

        // Enable highlight current line
        isHighlightCurrentLine = true

        // Enable bracket auto completion
        setBracketAutoCompletionEnabled(true)

        // Enable auto indent
        isAutoIndentEnabled = true

        // Set tab width
        tabWidth = 4

        // Enable undo/redo
        setUndoEnabled(true)

        // Set color scheme
        colorScheme = TextMateColorScheme(context)
    }

    /**
     * Set content with syntax highlighting
     */
    fun setContentWithHighlight(content: String, language: String) {
        setText(content)
        // TODO: Set language for syntax highlighting
    }

    /**
     * Insert text at cursor position
     */
    fun insertTextAtCursor(text: String) {
        val cursor = cursor
        this.text.insert(cursor.leftLine, cursor.leftColumn, text)
    }

    /**
     * Replace text in range
     */
    fun replaceText(startLine: Int, startColumn: Int, endLine: Int, endColumn: Int, newText: String) {
        text.delete(startLine, startColumn, endLine, endColumn)
        text.insert(startLine, startColumn, newText)
    }

    /**
     * Get current cursor line
     */
    fun getCurrentLine(): Int = cursor.leftLine

    /**
     * Get current cursor column
     */
    fun getCurrentColumn(): Int = cursor.leftColumn

    /**
     * Get selected text
     */
    fun getSelectedText(): String? {
        val selection = cursor
        return if (selection.isSelected) {
            text.substring(selection.left, selection.right)
        } else {
            null
        }
    }
}
