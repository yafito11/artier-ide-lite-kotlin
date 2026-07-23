package com.artier.ide.lite.core.model

import kotlinx.serialization.Serializable

/**
 * Project data model
 */
@Serializable
data class Project(
    val id: String,
    val name: String,
    val rootPath: String,
    val lastOpenedAt: Long,
    val vscodeSettingsJson: String? = null
)
