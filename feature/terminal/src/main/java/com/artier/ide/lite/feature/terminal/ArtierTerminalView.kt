package com.artier.ide.lite.feature.terminal

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 * Terminal view placeholder - Termux dependency not yet available
 * Will be replaced with actual Termux TerminalView integration
 */
class ArtierTerminalView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    init {
        text = "Terminal (Termux integration pending)"
        setTextColor(0xFFCCCCCC.toInt())
        setBackgroundColor(0xFF1F1F1F.toInt())
        setPadding(16, 16, 16, 16)
    }

    fun sendInput(text: String) {
        append("\n$text")
    }

    fun finishSession() {
        text = "Terminal session ended"
    }
}
