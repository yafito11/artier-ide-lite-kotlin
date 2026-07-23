package com.artier.ide.lite.core.model

import kotlinx.serialization.Serializable

/**
 * Checkpoint - Diff-based snapshot before AI edits
 */
@Serializable
data class Checkpoint(
    val id: String,
    val sessionId: String,
    val projectId: String,
    val filePath: String,
    val diffPatch: String,         // Unified diff format
    val timestamp: Long,           // epoch millis
    val aiActionDescription: String,
    val lineNumber: Int            // line where edit occurred
)

/**
 * File edit for Fast Apply
 */
@Serializable
data class FileEdit(
    val filePath: String,
    val searchBlock: String,
    val replaceBlock: String,
    val startLine: Int,
    val endLine: Int
)

/**
 * Quick Edit request/response
 */
@Serializable
data class QuickEditRequest(
    val selectedCode: String,
    val surroundingContext: String,
    val instruction: String,
    val filePath: String,
    val language: String
)

@Serializable
data class QuickEditResponse(
    val oldBlock: String,
    val newBlock: String,
    val explanation: String
)

/**
 * Ghost text for autocomplete
 */
@Serializable
data class GhostTextRequest(
    val prefix: String,
    val suffix: String,
    val language: String,
    val modelId: String? = null
)

@Serializable
data class GhostTextResponse(
    val completion: String,
    val isPartial: Boolean = false
)
