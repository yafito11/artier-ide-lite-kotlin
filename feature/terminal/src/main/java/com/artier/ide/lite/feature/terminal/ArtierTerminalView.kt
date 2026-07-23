package com.artier.ide.lite.feature.terminal

import android.content.Context
import android.util.AttributeSet
import com.termux.view.TerminalView
import com.termux.view.TerminalSession
import com.termux.view.TerminalEmulator
import com.termux.view.TerminalViewClient

/**
 * Terminal view wrapper for Artier IDE
 * Integrates Termux TerminalView with proper client implementation
 */
class ArtierTerminalView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TerminalView(context, attrs), TerminalViewClient {

    private var terminalSession: TerminalSession? = null
    var onSessionCreated: ((TerminalSession) -> Unit)? = null

    init {
        setupTerminal()
    }

    private fun setupTerminal() {
        // Set this as the view client
        setTerminalViewClient(this)

        // Create terminal session
        val session = TerminalSession(
            "/system/bin/sh", // shell path
            "/data/data/com.artier.ide.lite/files", // working directory
            arrayOf("/system/bin/sh"), // shell args
            null, // env
            object : TerminalSession.SessionChangedCallback {
                override fun onTextChanged(changedSession: TerminalSession) {
                    // Update terminal display
                    onScreenUpdated()
                }

                override fun onTitleChanged(changedSession: TerminalSession) {
                    // Handle title change
                }

                override fun onProcessFinished(changedSession: TerminalSession) {
                    // Handle process finished
                }

                override fun onCopyTextToClipboard(changedSession: TerminalSession, text: String) {
                    // Handle copy
                }

                override fun onPasteTextFromClipboard(changedSession: TerminalSession) {
                    // Handle paste
                }

                override fun onBell(changedSession: TerminalSession) {
                    // Handle bell
                }

                override fun onColorsChanged(changedSession: TerminalSession) {
                    // Handle colors changed
                }
            }
        )

        terminalSession = session
        attachSession(session)
        onSessionCreated?.invoke(session)
    }

    // TerminalViewClient implementation

    override fun onCodePoint(codePoint: Int, ctrl: Boolean, alt: Boolean) {
        // Handle code point input
    }

    override fun onScale(factor: Float) {
        // Handle scale/zoom
    }

    override fun onSingleTapUp() {
        // Handle single tap
    }

    override fun onLongPress() {
        // Handle long press
    }

    override fun onKeyDown(keyCode: Int, event: android.view.KeyEvent): Boolean {
        // Handle key down
        return false
    }

    override fun onKeyUp(keyCode: Int, event: android.view.KeyEvent): Boolean {
        // Handle key up
        return false
    }

    override fun onKeyMultiple(keyCode: Int, repeatCount: Int, event: android.view.KeyEvent): Boolean {
        // Handle key multiple
        return false
    }

    override fun shouldBackButtonBeMappedToEscape(): Boolean {
        return false
    }

    override fun shouldEnforceCharBasedInput(): Boolean {
        return false
    }

    override fun onEmulatorSet(): Unit = Unit

    override fun onScreenUpdated() {
        // Update screen
        postInvalidate()
    }

    override fun getTerminalCursorStyle(): Int {
        return 0 // Block cursor
    }

    override fun isTerminalViewKeyInputAllowed(): Boolean {
        return true
    }

    /**
     * Send input to terminal
     */
    fun sendInput(text: String) {
        terminalSession?.write(text)
    }

    /**
     * Send key event to terminal
     */
    fun sendKey(keyCode: Int, ctrl: Boolean = false, alt: Boolean = false) {
        terminalSession?.sendKeyEvent(keyCode, 0, ctrl, alt)
    }

    /**
     * Get terminal emulator
     */
    fun getTerminalEmulator(): TerminalEmulator? {
        return terminalSession?.mTerminal
    }

    /**
     * Resize terminal
     */
    fun resizeTerminal(columns: Int, rows: Int) {
        terminalSession?.updateSize(columns, rows)
    }

    /**
     * Get current session
     */
    fun getSession(): TerminalSession? = terminalSession

    /**
     * Finish terminal session
     */
    fun finishSession() {
        terminalSession?.finishIfRunning()
        terminalSession = null
    }
}
