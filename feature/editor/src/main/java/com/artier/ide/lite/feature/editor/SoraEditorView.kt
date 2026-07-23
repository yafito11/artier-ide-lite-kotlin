package com.artier.ide.lite.feature.editor

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.viewinterop.AndroidView
import io.github.rosemoe.sora.widget.CodeEditor

/**
 * Compose wrapper for Sora Editor
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
                isLineNumberEnabled = true
                isHighlightCurrentLine = true
                setTabWidth(4)
                setText(state.content)
                state.editor = this
            }
        },
        modifier = modifier,
        onRelease = { editor ->
            editor.release()
        },
        update = { editor ->
            if (editor.text.toString() != state.content) {
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
    return androidx.compose.runtime.remember { CodeEditorState() }
}
