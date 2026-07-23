package com.artier.ide.lite.ai.autocomplete

import com.artier.ide.lite.core.model.GhostTextRequest
import com.artier.ide.lite.core.model.GhostTextResponse
import com.artier.ide.lite.daemon.DaemonBridge
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Autocomplete service with debounce and cancellation
 * Handles FIM requests and ghost text state
 */
@Singleton
class AutocompleteService @Inject constructor(
    private val daemonBridge: DaemonBridge,
    private val scope: CoroutineScope
) {
    private val _inputEvents = MutableSharedFlow<InputEvent>(
        replay = 0,
        extraBufferCapacity = 64
    )

    private val _ghostTextState = MutableStateFlow<GhostTextState>(GhostTextState.Idle)
    val ghostTextState: StateFlow<GhostTextState> = _ghostTextState.asStateFlow()

    private var currentJob: Job? = null
    private var isEnabled = true

    init {
        scope.launch {
            _inputEvents
                .debounce(200) // 200ms debounce
                .distinctUntilChanged()
                .collect { event ->
                    if (!isEnabled) return@collect

                    // Cancel any previous in-flight request
                    currentJob?.cancel()
                    currentJob = scope.launch {
                        try {
                            val request = GhostTextRequest(
                                prefix = event.prefix,
                                suffix = event.suffix,
                                language = event.language
                            )

                            val response = daemonBridge.getAutocomplete(request)

                            if (response.completion.isNotEmpty()) {
                                _ghostTextState.value = GhostTextState.Active(
                                    text = response.completion,
                                    startOffset = event.cursorOffset
                                )
                            }
                        } catch (e: CancellationException) {
                            _ghostTextState.value = GhostTextState.Idle
                            throw e
                        } catch (e: Exception) {
                            _ghostTextState.value = GhostTextState.Idle
                        }
                    }
                }
        }
    }

    /**
     * Process input event (called on each keystroke)
     */
    fun onInputEvent(event: InputEvent) {
        scope.launch {
            _inputEvents.emit(event)
        }
    }

    /**
     * Accept ghost text (Tab pressed)
     */
    fun acceptGhostText(): String? {
        val state = _ghostTextState.value
        if (state is GhostTextState.Active) {
            _ghostTextState.value = GhostTextState.Idle
            return state.text
        }
        return null
    }

    /**
     * Cancel ghost text (Esc or new keystroke)
     */
    fun cancelGhostText() {
        currentJob?.cancel()
        _ghostTextState.value = GhostTextState.Idle
    }

    /**
     * Enable/disable autocomplete
     */
    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
        if (!enabled) {
            cancelGhostText()
        }
    }
}

/**
 * Input event from editor
 */
data class InputEvent(
    val prefix: String,
    val suffix: String,
    val cursorOffset: Int,
    val language: String
)

/**
 * Ghost text state
 */
sealed class GhostTextState {
    data object Idle : GhostTextState()
    data class Active(
        val text: String,
        val startOffset: Int
    ) : GhostTextState()
}
