package com.artier.ide.lite.core.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Extension functions and utilities
 */

/**
 * Format timestamp to readable string
 */
fun Long.toFormattedDate(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(this))
}

/**
 * Format file size to readable string
 */
fun Long.toFormattedSize(): String {
    return when {
        this < 1024 -> "$this B"
        this < 1024 * 1024 -> "${this / 1024} KB"
        this < 1024 * 1024 * 1024 -> "${this / (1024 * 1024)} MB"
        else -> "${this / (1024 * 1024 * 1024)} GB"
    }
}

/**
 * Get file extension from path
 */
fun String.getFileExtension(): String {
    return this.substringAfterLast('.', "")
}

/**
 * Get file name from path
 */
fun String.getFileName(): String {
    return this.substringAfterLast('/')
}

/**
 * Check if string is a valid email
 */
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
