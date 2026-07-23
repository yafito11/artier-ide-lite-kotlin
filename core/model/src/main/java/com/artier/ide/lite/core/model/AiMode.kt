package com.artier.ide.lite.core.model

import kotlinx.serialization.Serializable

/**
 * AI Mode - Permission levels for AI interaction
 * Based on Void IDE's Agent/Gather/Chat model
 */
@Serializable
enum class AiMode {
    /**
     * Agent Mode - Full access to all tools
     * read: allow, edit: allow, bash: allow, MCP tools: allow
     */
    AGENT,

    /**
     * Gather Mode - Read-only exploration
     * read: allow, edit: deny, bash: deny, MCP tools (read-only): allow
     */
    GATHER,

    /**
     * Chat Mode - No tools, general conversation
     * No tool access
     */
    CHAT
}

/**
 * Permission set for each AI mode
 */
@Serializable
data class AiPermissions(
    val read: Boolean = false,
    val edit: Boolean = false,
    val bash: Boolean = false,
    val mcpRead: Boolean = false,
    val mcpWrite: Boolean = false
) {
    companion object {
        val AGENT = AiPermissions(
            read = true,
            edit = true,
            bash = true,
            mcpRead = true,
            mcpWrite = true
        )

        val GATHER = AiPermissions(
            read = true,
            edit = false,
            bash = false,
            mcpRead = true,
            mcpWrite = false
        )

        val CHAT = AiPermissions()

        fun forMode(mode: AiMode): AiPermissions = when (mode) {
            AiMode.AGENT -> AGENT
            AiMode.GATHER -> GATHER
            AiMode.CHAT -> CHAT
        }
    }
}
