package com.artier.ide.lite.ai.provider

import com.artier.ide.lite.core.model.AiProvider
import com.artier.ide.lite.core.model.ModelConfig
import com.artier.ide.lite.core.model.ModelMetadata
import com.artier.ide.lite.core.security.KeyStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AI Provider manager with capability probing
 */
@Singleton
class ProviderManager @Inject constructor(
    private val keyStoreManager: KeyStoreManager
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val providers = mutableMapOf<String, AiProvider>()

    /**
     * Add a new provider
     */
    fun addProvider(provider: AiProvider) {
        providers[provider.id] = provider
    }

    /**
     * Get provider by ID
     */
    fun getProvider(id: String): AiProvider? = providers[id]

    /**
     * Get all providers
     */
    fun getAllProviders(): List<AiProvider> = providers.values.toList()

    /**
     * Probe model capabilities
     */
    suspend fun probeModelCapabilities(
        provider: AiProvider,
        model: ModelConfig
    ): ModelMetadata = withContext(Dispatchers.IO) {
        val apiKey = keyStoreManager.getApiKey(provider.id) ?: ""

        val supportsTools = probeToolSupport(provider, model, apiKey)
        val supportsFim = probeFimSupport(provider, model, apiKey)
        val supportsReasoning = probeReasoningSupport(provider, model, apiKey)

        ModelMetadata(
            modelId = model.id,
            providerId = provider.id,
            supportsTools = supportsTools,
            supportsFim = supportsFim,
            supportsReasoning = supportsReasoning,
            lastProbedAt = System.currentTimeMillis()
        )
    }

    private suspend fun probeToolSupport(
        provider: AiProvider,
        model: ModelConfig,
        apiKey: String
    ): Boolean {
        // TODO: Send test request with tool calling
        return false
    }

    private suspend fun probeFimSupport(
        provider: AiProvider,
        model: ModelConfig,
        apiKey: String
    ): Boolean {
        // TODO: Test FIM endpoint
        return false
    }

    private suspend fun probeReasoningSupport(
        provider: AiProvider,
        model: ModelConfig,
        apiKey: String
    ): Boolean {
        // TODO: Check for reasoning/thinking blocks in response
        return false
    }
}
