package com.artier.ide.lite.daemon

import android.content.Context
import com.artier.ide.lite.core.model.DaemonStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages the Node.js daemon lifecycle in proot
 */
@Singleton
class DaemonManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var daemonProcess: Process? = null
    private val _status = MutableStateFlow<DaemonStatus>(DaemonStatus.Stopped)
    val status: StateFlow<DaemonStatus> = _status.asStateFlow()

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Start the daemon process
     */
    suspend fun start(port: Int = 9527) {
        if (_status.value is DaemonStatus.Running) return

        _status.value = DaemonStatus.Starting

        try {
            // Extract daemon files if needed
            extractDaemonIfNeeded()

            // Start proot with Node.js
            val prootDir = File(context.filesDir, "proot")
            val nodeBin = File(prootDir, "bin/node")
            val daemonScript = File(prootDir, "daemon/dist/index.js")

            val processBuilder = ProcessBuilder(
                nodeBin.absolutePath,
                daemonScript.absolutePath,
                "--port", port.toString()
            ).apply {
                directory(prootDir)
                redirectErrorStream(true)
            }

            daemonProcess = processBuilder.start()

            // Wait for daemon to be healthy
            val isHealthy = waitForHealthy(port, timeoutMs = 15_000)

            if (isHealthy) {
                _status.value = DaemonStatus.Running(port)
            } else {
                _status.value = DaemonStatus.Error("Daemon failed to start within timeout")
            }
        } catch (e: Exception) {
            _status.value = DaemonStatus.Error(e.message ?: "Unknown error")
        }
    }

    /**
     * Stop the daemon process
     */
    suspend fun stop() {
        daemonProcess?.destroy()
        daemonProcess = null
        _status.value = DaemonStatus.Stopped
    }

    /**
     * Wait for daemon health check to pass
     */
    private suspend fun waitForHealthy(port: Int, timeoutMs: Long): Boolean {
        return withTimeoutOrNull(timeoutMs) {
            while (true) {
                try {
                    val request = Request.Builder()
                        .url("http://localhost:$port/api/health")
                        .get()
                        .build()

                    val response = client.newCall(request).execute()
                    if (response.isSuccessful) {
                        return@withTimeoutOrNull true
                    }
                } catch (_: Exception) {
                    // Daemon not ready yet
                }
                delay(500)
            }
            false
        } ?: false
    }

    /**
     * Extract daemon files from assets if not already extracted
     */
    private fun extractDaemonIfNeeded() {
        val prootDir = File(context.filesDir, "proot")
        if (prootDir.exists()) return

        // TODO: Extract from assets
        // For now, create placeholder directories
        prootDir.mkdirs()
        File(prootDir, "bin").mkdirs()
        File(prootDir, "daemon").mkdirs()
    }
}
