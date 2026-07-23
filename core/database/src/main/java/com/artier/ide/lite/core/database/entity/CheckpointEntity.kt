package com.artier.ide.lite.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checkpoints")
data class CheckpointEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val projectId: String,
    val filePath: String,
    val diffPatch: String,
    val timestamp: Long,
    val aiActionDescription: String,
    val lineNumber: Int
)
