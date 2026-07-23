package com.artier.ide.lite.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * DataStore preferences manager
 */
@Singleton
class SettingsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        // Daemon settings
        val DAEMON_PORT = intPreferencesKey("daemon_port")
        val AUTO_START_DAEMON = booleanPreferencesKey("auto_start_daemon")

        // Editor settings
        val TAB_SIZE = intPreferencesKey("tab_size")
        val FONT_SIZE = intPreferencesKey("font_size")
        val WORD_WRAP = booleanPreferencesKey("word_wrap")
        val SHOW_LINE_NUMBERS = booleanPreferencesKey("show_line_numbers")

        // AI settings
        val DEFAULT_AI_SOURCE = stringPreferencesKey("default_ai_source")
        val DEFAULT_AI_MODE = stringPreferencesKey("default_ai_mode")
        val AUTOCOMPLETE_ENABLED = booleanPreferencesKey("autocomplete_enabled")
        val AUTOCOMPLETE_MODEL = stringPreferencesKey("autocomplete_model")
        val CHAT_MODEL = stringPreferencesKey("chat_model")

        // Theme settings
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val ACCENT_COLOR = stringPreferencesKey("accent_color")
    }

    // Daemon settings
    val daemonPort: Flow<Int> = dataStore.data.map { it[DAEMON_PORT] ?: 9527 }
    val autoStartDaemon: Flow<Boolean> = dataStore.data.map { it[AUTO_START_DAEMON] ?: true }

    // Editor settings
    val tabSize: Flow<Int> = dataStore.data.map { it[TAB_SIZE] ?: 4 }
    val fontSize: Flow<Int> = dataStore.data.map { it[FONT_SIZE] ?: 14 }
    val wordWrap: Flow<Boolean> = dataStore.data.map { it[WORD_WRAP] ?: false }
    val showLineNumbers: Flow<Boolean> = dataStore.data.map { it[SHOW_LINE_NUMBERS] ?: true }

    // AI settings
    val defaultAiSource: Flow<String> = dataStore.data.map { it[DEFAULT_AI_SOURCE] ?: "Built-in Agent" }
    val defaultAiMode: Flow<String> = dataStore.data.map { it[DEFAULT_AI_MODE] ?: "AGENT" }
    val autocompleteEnabled: Flow<Boolean> = dataStore.data.map { it[AUTOCOMPLETE_ENABLED] ?: true }
    val autocompleteModel: Flow<String> = dataStore.data.map { it[AUTOCOMPLETE_MODEL] ?: "" }
    val chatModel: Flow<String> = dataStore.data.map { it[CHAT_MODEL] ?: "" }

    // Theme settings
    val darkTheme: Flow<Boolean> = dataStore.data.map { it[DARK_THEME] ?: true }

    // Update functions
    suspend fun updateDaemonPort(port: Int) {
        dataStore.edit { it[DAEMON_PORT] = port }
    }

    suspend fun updateAutoStartDaemon(enabled: Boolean) {
        dataStore.edit { it[AUTO_START_DAEMON] = enabled }
    }

    suspend fun updateTabSize(size: Int) {
        dataStore.edit { it[TAB_SIZE] = size }
    }

    suspend fun updateFontSize(size: Int) {
        dataStore.edit { it[FONT_SIZE] = size }
    }

    suspend fun updateWordWrap(enabled: Boolean) {
        dataStore.edit { it[WORD_WRAP] = enabled }
    }

    suspend fun updateShowLineNumbers(enabled: Boolean) {
        dataStore.edit { it[SHOW_LINE_NUMBERS] = enabled }
    }

    suspend fun updateDefaultAiSource(source: String) {
        dataStore.edit { it[DEFAULT_AI_SOURCE] = source }
    }

    suspend fun updateDefaultAiMode(mode: String) {
        dataStore.edit { it[DEFAULT_AI_MODE] = mode }
    }

    suspend fun updateAutocompleteEnabled(enabled: Boolean) {
        dataStore.edit { it[AUTOCOMPLETE_ENABLED] = enabled }
    }

    suspend fun updateAutocompleteModel(model: String) {
        dataStore.edit { it[AUTOCOMPLETE_MODEL] = model }
    }

    suspend fun updateChatModel(model: String) {
        dataStore.edit { it[CHAT_MODEL] = model }
    }
}
