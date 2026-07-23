package com.artier.ide.lite.core.model

import kotlinx.serialization.Serializable

/**
 * Daemon status
 */
sealed class DaemonStatus {
    data object Stopped : DaemonStatus()
    data object Starting : DaemonStatus()
    data class Running(val port: Int) : DaemonStatus()
    data class Error(val message: String) : DaemonStatus()
}

/**
 * Daemon message types for WebSocket communication
 */
@Serializable
sealed class DaemonMessage {
    @Serializable
    data class Request(
        val id: String,
        val type: String,
        val payload: String
    ) : DaemonMessage()

    @Serializable
    data class Response(
        val id: String,
        val type: String,
        val payload: String
    ) : DaemonMessage()

    @Serializable
    data class Stream(
        val id: String,
        val chunk: String
    ) : DaemonMessage()

    @Serializable
    data class Error(
        val id: String,
        val code: Int,
        val message: String
    ) : DaemonMessage()

    @Serializable
    data object Ping : DaemonMessage()

    @Serializable
    data object Pong : DaemonMessage()
}

/**
 * Conversation and message models
 */
@Serializable
data class Conversation(
    val id: String,
    val projectId: String,
    val mode: AiMode,
    val source: String,
    val createdAt: Long,
    val title: String? = null
)

@Serializable
data class Message(
    val id: String,
    val conversationId: String,
    val role: MessageRole,
    val content: String,
    val toolCallJson: String? = null,
    val timestamp: Long
)

@Serializable
enum class MessageRole {
    USER,
    ASSISTANT,
    TOOL
}
