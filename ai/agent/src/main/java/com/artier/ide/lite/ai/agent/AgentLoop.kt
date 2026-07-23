package com.artier.ide.lite.ai.agent

import com.artier.ide.lite.core.model.AiMode
import com.artier.ide.lite.daemon.DaemonBridge
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Agent loop for handling AI conversations
 */
@Singleton
class AgentLoop @Inject constructor(
    private val daemonBridge: DaemonBridge
) {
    /**
     * Send message and get streaming response
     */
    fun sendMessage(
        message: String,
        mode: AiMode,
        conversationHistory: List<Pair<String, String>> = emptyList()
    ): Flow<String> {
        return daemonBridge.sendChatMessage(
            message = message,
            mode = mode.name.lowercase(),
            conversationHistory = conversationHistory
        )
    }

    /**
     * Check if daemon is connected
     */
    fun isDaemonConnected(): Boolean = daemonBridge.isConnected()
}
