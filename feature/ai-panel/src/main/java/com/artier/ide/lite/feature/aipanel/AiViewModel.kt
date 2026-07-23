package com.artier.ide.lite.feature.aipanel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artier.ide.lite.core.model.AiMode
import com.artier.ide.lite.core.model.Message
import com.artier.ide.lite.core.model.MessageRole
import com.artier.ide.lite.daemon.DaemonBridge
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel for AI Assistant panel
 */
@HiltViewModel
class AiViewModel @Inject constructor(
    private val daemonBridge: DaemonBridge
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _selectedMode = mutableStateOf(AiMode.AGENT)
    val selectedMode: AiMode by _selectedMode

    private val _selectedSource = mutableStateOf("Built-in Agent")
    val selectedSource: String by _selectedSource

    private val _isStreaming = mutableStateOf(false)
    val isStreaming: Boolean by _isStreaming

    private val _inputText = mutableStateOf("")
    val inputText: String by _inputText

    /**
     * Update input text
     */
    fun updateInput(text: String) {
        _inputText.value = text
    }

    /**
     * Set AI mode
     */
    fun setMode(mode: AiMode) {
        _selectedMode.value = mode
    }

    /**
     * Set AI source
     */
    fun setSource(source: String) {
        _selectedSource.value = source
    }

    /**
     * Send message to AI
     */
    fun sendMessage() {
        val text = _inputText.value.trim()
        if (text.isEmpty() || _isStreaming.value) return

        // Add user message
        val userMessage = Message(
            id = UUID.randomUUID().toString(),
            conversationId = "current",
            role = MessageRole.USER,
            content = text,
            timestamp = System.currentTimeMillis()
        )
        _messages.value = _messages.value + userMessage
        _inputText.value = ""

        // Start streaming response
        _isStreaming.value = true

        viewModelScope.launch {
            try {
                val conversationHistory = _messages.value.map { msg ->
                    Pair(
                        when (msg.role) {
                            MessageRole.USER -> "user"
                            MessageRole.ASSISTANT -> "assistant"
                            MessageRole.TOOL -> "tool"
                        },
                        msg.content
                    )
                }

                val responseBuilder = StringBuilder()

                daemonBridge.sendChatMessage(
                    message = text,
                    mode = _selectedMode.name.lowercase(),
                    conversationHistory = conversationHistory.dropLast(1) // Exclude current message
                ).collect { chunk ->
                    responseBuilder.append(chunk)
                    // Update the last message or create new one
                    val currentMessages = _messages.value
                    val lastMessage = currentMessages.lastOrNull()
                    if (lastMessage?.role == MessageRole.ASSISTANT && lastMessage.id == "streaming") {
                        _messages.value = currentMessages.dropLast(1) + lastMessage.copy(
                            content = responseBuilder.toString()
                        )
                    } else {
                        _messages.value = currentMessages + Message(
                            id = "streaming",
                            conversationId = "current",
                            role = MessageRole.ASSISTANT,
                            content = responseBuilder.toString(),
                            timestamp = System.currentTimeMillis()
                        )
                    }
                }

                // Finalize the message
                val finalMessages = _messages.value
                val streamingMessage = finalMessages.lastOrNull()
                if (streamingMessage?.id == "streaming") {
                    _messages.value = finalMessages.dropLast(1) + streamingMessage.copy(
                        id = UUID.randomUUID().toString()
                    )
                }
            } catch (e: Exception) {
                // Add error message
                _messages.value = _messages.value + Message(
                    id = UUID.randomUUID().toString(),
                    conversationId = "current",
                    role = MessageRole.ASSISTANT,
                    content = "Error: ${e.message}",
                    timestamp = System.currentTimeMillis()
                )
            } finally {
                _isStreaming.value = false
            }
        }
    }

    /**
     * Clear conversation
     */
    fun clearConversation() {
        _messages.value = emptyList()
    }
}
