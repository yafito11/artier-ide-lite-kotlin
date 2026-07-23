package com.artier.ide.lite.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val mode: String,
    val source: String,
    val createdAt: Long,
    val title: String? = null
)
