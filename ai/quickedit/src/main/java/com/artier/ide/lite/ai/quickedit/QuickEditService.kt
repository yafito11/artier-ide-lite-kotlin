package com.artier.ide.lite.ai.quickedit

import com.artier.ide.lite.core.model.QuickEditRequest
import com.artier.ide.lite.core.model.QuickEditResponse
import com.artier.ide.lite.daemon.DaemonBridge
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Quick Edit service for inline code editing
 */
@Singleton
class QuickEditService @Inject constructor(
    private val daemonBridge: DaemonBridge
) {
    /**
     * Get edit suggestion for selected code
     */
    suspend fun getEditSuggestion(
        selectedCode: String,
        surroundingContext: String,
        instruction: String,
        filePath: String,
        language: String
    ): QuickEditResponse {
        val request = QuickEditRequest(
            selectedCode = selectedCode,
            surroundingContext = surroundingContext,
            instruction = instruction,
            filePath = filePath,
            language = language
        )

        return daemonBridge.getQuickEdit(request)
    }
}
