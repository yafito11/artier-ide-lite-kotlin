package com.artier.ide.lite.core.common

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for extension functions
 */
class ExtensionsTest {

    @Test
    fun long_toFormattedDate() {
        // 2024-01-15 14:30:00 UTC
        val timestamp = 1705329000000L
        val formatted = timestamp.toFormattedDate()
        assertNotNull(formatted)
        assertTrue(formatted.contains(":"))
    }

    @Test
    fun long_toFormattedSize_bytes() {
        val bytes = 512L
        assertEquals("512 B", bytes.toFormattedSize())
    }

    @Test
    fun long_toFormattedSize_kilobytes() {
        val kilobytes = 1024L * 2
        assertEquals("2 KB", kilobytes.toFormattedSize())
    }

    @Test
    fun long_toFormattedSize_megabytes() {
        val megabytes = 1024L * 1024 * 5
        assertEquals("5 MB", megabytes.toFormattedSize())
    }

    @Test
    fun long_toFormattedSize_gigabytes() {
        val gigabytes = 1024L * 1024 * 1024 * 2
        assertEquals("2 GB", gigabytes.toFormattedSize())
    }

    @Test
    fun string_getFileExtension() {
        assertEquals("kt", "file.kt".getFileExtension())
        assertEquals("xml", "layout.xml".getFileExtension())
        assertEquals("json", "config.json".getFileExtension())
        assertEquals("", "noextension".getFileExtension())
    }

    @Test
    fun string_getFileName() {
        assertEquals("file.kt", "/path/to/file.kt".getFileName())
        assertEquals("file.kt", "file.kt".getFileName())
        assertEquals("file.kt", "dir/subdir/file.kt".getFileName())
    }
}
