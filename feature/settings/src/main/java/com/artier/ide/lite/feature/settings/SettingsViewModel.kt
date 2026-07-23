package com.artier.ide.lite.feature.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artier.ide.lite.core.datastore.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Settings screen
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {

    val daemonPort = settingsManager.daemonPort
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 9527)

    val autoStartDaemon = settingsManager.autoStartDaemon
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val tabSize = settingsManager.tabSize
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 4)

    val fontSize = settingsManager.fontSize
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 14)

    val autocompleteEnabled = settingsManager.autocompleteEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val defaultAiMode = settingsManager.defaultAiMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "AGENT")

    fun updateDaemonPort(port: Int) {
        viewModelScope.launch {
            settingsManager.updateDaemonPort(port)
        }
    }

    fun updateAutoStartDaemon(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.updateAutoStartDaemon(enabled)
        }
    }

    fun updateTabSize(size: Int) {
        viewModelScope.launch {
            settingsManager.updateTabSize(size)
        }
    }

    fun updateFontSize(size: Int) {
        viewModelScope.launch {
            settingsManager.updateFontSize(size)
        }
    }

    fun updateAutocompleteEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.updateAutocompleteEnabled(enabled)
        }
    }

    fun updateDefaultAiMode(mode: String) {
        viewModelScope.launch {
            settingsManager.updateDefaultAiMode(mode)
        }
    }
}
