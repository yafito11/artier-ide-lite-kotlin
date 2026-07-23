package com.artier.ide.lite.core.model

import kotlinx.serialization.Serializable

/**
 * AI Provider configuration
 */
@Serializable
data class AiProvider(
    val id: String,
    val name: String,
    val baseUrl: String,
    val apiKeyRef: String, // Reference to KeyStore
    val models: List<ModelConfig> = emptyList(),
    val type: ProviderType = ProviderType.OPENAI_COMPATIBLE
)

@Serializable
enum class ProviderType {
    OPENAI_COMPATIBLE,
    ANTHROPIC,
    CUSTOM
}

/**
 * Model configuration
 */
@Serializable
data class ModelConfig(
    val id: String,
    val displayName: String,
    val parameterSize: String? = null, // e.g., "1.5B", "7B", "70B"
    val supportsTools: Boolean = false,
    val supportsFim: Boolean = false,
    val supportsReasoning: Boolean = false,
    val maxTokens: Int = 4096,
    val isDefaultForChat: Boolean = false,
    val isDefaultForAutocomplete: Boolean = false
)

/**
 * Model capability metadata (from probe results)
 */
@Serializable
data class ModelMetadata(
    val modelId: String,
    val providerId: String,
    val supportsTools: Boolean = false,
    val supportsFim: Boolean = false,
    val supportsReasoning: Boolean = false,
    val lastProbedAt: Long = 0
)
