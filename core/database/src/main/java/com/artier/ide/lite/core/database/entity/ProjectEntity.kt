package com.artier.ide.lite.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val rootPath: String,
    val lastOpenedAt: Long,
    val vscodeSettingsJson: String? = null
)
