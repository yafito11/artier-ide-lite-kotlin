package com.artier.ide.lite.feature.editor

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.viewinterop.AndroidView
import io.github.rosemoe.sora.langs.textmate.TextMateColorScheme
import io.github.rosemoe.sora.langs.textmate.TextMateLanguage
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme

/**
 * Compose wrapper for Sora Editor
 * Hosts the native CodeEditor in a Compose layout
 *
 * Based on official Sora Editor Compose integration guide:
 * https://project-sora.github.io/sora-editor-docs/guide/code-editor-in-compose
 */
@Composable
fun SoraEditorView(
    modifier: Modifier = Modifier,
    state: CodeEditorState = rememberCodeEditorState()
) {
    val editor = state.editor

    AndroidView(
        factory = { context ->
            CodeEditor(context).apply {
                // Configure color scheme
                colorScheme = TextMateColorScheme(context)

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

                // Set text
                setText(state.content)

                state.editor = this
            }
        },
        modifier = modifier,
        onRelease = { editor ->
            editor.release()
        },
        update = { editor ->
            // Sync state changes
            if (editor.text != state.content) {
                editor.setText(state.content)
            }
        }
    )
}

/**
 * State holder for CodeEditor
 */
class CodeEditorState {
    var editor: CodeEditor? = null
    var content: String = ""
        set(value) {
            field = value
            editor?.setText(value)
        }
}

@Composable
fun rememberCodeEditorState(): CodeEditorState {
    return remember { CodeEditorState() }
}
