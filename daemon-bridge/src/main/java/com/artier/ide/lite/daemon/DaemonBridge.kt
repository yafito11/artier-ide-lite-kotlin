package com.artier.ide.lite.daemon

import com.artier.ide.lite.core.model.DaemonMessage
import com.artier.ide.lite.core.model.GhostTextRequest
import com.artier.ide.lite.core.model.GhostTextResponse
import com.artier.ide.lite.core.model.QuickEditRequest
import com.artier.ide.lite.core.model.QuickEditResponse
import kotlinx.coroutines.flow.Flow

/**
 * Interface for daemon communication
 */
interface DaemonBridge {
    /**
     * Connect to daemon via WebSocket
     */
    suspend fun connect(port: Int = 9527)

    /**
     * Disconnect from daemon
     */
    suspend fun disconnect()

    /**
     * Send a request and wait for response
     */
    suspend fun <T> request(
        type: String,
        payload: Any,
        responseType: Class<T>
    ): T

    /**
     * Send a request and get streaming response
     */
    fun requestStream(
        type: String,
        payload: Any
    ): Flow<String>

    /**
     * Get autocomplete suggestion
     */
    suspend fun getAutocomplete(request: GhostTextRequest): GhostTextResponse

    /**
     * Get quick edit suggestion
     */
    suspend fun getQuickEdit(request: QuickEditRequest): QuickEditResponse

    /**
     * Send chat message and get streaming response
     */
    fun sendChatMessage(
        message: String,
        mode: String,
        conversationHistory: List<Pair<String, String>>
    ): Flow<String>

    /**
     * Check if daemon is connected
     */
    fun isConnected(): Boolean

    /**
     * Observe connection status
     */
    fun observeConnection(): Flow<Boolean>
}
