package com.artier.ide.lite.core.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Android KeyStore wrapper for secure API key storage
 */
@Singleton
class KeyStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    /**
     * Generate or get existing secret key
     */
    fun getOrCreateKey(alias: String): SecretKey {
        keyStore.getEntry(alias, null)?.let {
            return (it as KeyStore.SecretKeyEntry).secretKey
        }

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )

        val spec = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    /**
     * Store API key securely
     */
    fun storeApiKey(providerId: String, apiKey: String) {
        // TODO: Encrypt and store in EncryptedSharedPreferences
        // For now, use basic implementation
        val prefs = context.getSharedPreferences("artier_keys", Context.MODE_PRIVATE)
        prefs.edit().putString(providerId, apiKey).apply()
    }

    /**
     * Retrieve API key
     */
    fun getApiKey(providerId: String): String? {
        val prefs = context.getSharedPreferences("artier_keys", Context.MODE_PRIVATE)
        return prefs.getString(providerId, null)
    }

    /**
     * Delete API key
     */
    fun deleteApiKey(providerId: String) {
        val prefs = context.getSharedPreferences("artier_keys", Context.MODE_PRIVATE)
        prefs.edit().remove(providerId).apply()
    }
}
