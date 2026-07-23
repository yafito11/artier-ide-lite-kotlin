package com.artier.ide.lite.daemon

import com.artier.ide.lite.core.model.GhostTextRequest
import com.artier.ide.lite.core.model.GhostTextResponse
import com.artier.ide.lite.core.model.QuickEditRequest
import com.artier.ide.lite.core.model.QuickEditResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import okio.ByteString.Companion.toByteString
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * WebSocket-based daemon communication implementation
 */
@Singleton
class DaemonWebSocket @Inject constructor() : DaemonBridge {

    private var webSocket: WebSocket? = null
    private val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS) // No timeout for WebSocket
        .build()

    private val _isConnected = kotlinx.coroutines.flow.MutableStateFlow(false)
    private val pendingRequests = mutableMapOf<String, kotlinx.coroutines.CompletableDeferred<String>>()

    override suspend fun connect(port: Int) {
        val request = Request.Builder()
            .url("ws://localhost:$port/ws")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                _isConnected.value = true
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val json = JSONObject(text)
                val id = json.optString("id")
                val payload = json.optString("payload")

                pendingRequests[id]?.complete(payload)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                _isConnected.value = false
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _isConnected.value = false
            }
        })
    }

    override suspend fun disconnect() {
        webSocket?.close(1000, "Client disconnect")
        webSocket = null
        _isConnected.value = false
    }

    override suspend fun <T> request(type: String, payload: Any, responseType: Class<T>): T {
        val id = UUID.randomUUID().toString()
        val deferred = kotlinx.coroutines.CompletableDeferred<String>()
        pendingRequests[id] = deferred

        val json = JSONObject().apply {
            put("id", id)
            put("type", type)
            put("payload", payload.toString())
        }

        webSocket?.send(json.toString())
        val result = deferred.await()
        pendingRequests.remove(id)

        @Suppress("UNCHECKED_CAST")
        return result as T
    }

    override fun requestStream(type: String, payload: Any): Flow<String> = callbackFlow {
        val id = UUID.randomUUID().toString()

        val json = JSONObject().apply {
            put("id", id)
            put("type", type)
            put("payload", payload.toString())
        }

        webSocket?.send(json.toString())

        awaitClose {
            // Cleanup if needed
        }
    }

    override suspend fun getAutocomplete(request: GhostTextRequest): GhostTextResponse {
        val result = request("autocomplete", request, String::class.java)
        val json = JSONObject(result)
        return GhostTextResponse(
            completion = json.getString("completion"),
            isPartial = json.optBoolean("isPartial", false)
        )
    }

    override suspend fun getQuickEdit(request: QuickEditRequest): QuickEditResponse {
        val result = request("quickedit", request, String::class.java)
        val json = JSONObject(result)
        return QuickEditResponse(
            oldBlock = json.getString("oldBlock"),
            newBlock = json.getString("newBlock"),
            explanation = json.optString("explanation", "")
        )
    }

    override fun sendChatMessage(
        message: String,
        mode: String,
        conversationHistory: List<Pair<String, String>>
    ): Flow<String> = callbackFlow {
        val id = UUID.randomUUID().toString()

        val historyArray = org.json.JSONArray().apply {
            conversationHistory.forEach { (role, content) ->
                put(JSONObject().apply {
                    put("role", role)
                    put("content", content)
                })
            }
        }

        val json = JSONObject().apply {
            put("id", id)
            put("type", "chat")
            put("payload", JSONObject().apply {
                put("message", message)
                put("mode", mode)
                put("history", historyArray)
            }.toString())
        }

        webSocket?.send(json.toString())

        awaitClose {
            // Cleanup
        }
    }

    override fun isConnected(): Boolean = _isConnected.value

    override fun observeConnection(): Flow<Boolean> = _isConnected
}
