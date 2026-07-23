package com.artier.ide.lite.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "model_metadata")
data class ModelMetadataEntity(
    @PrimaryKey val modelId: String,
    val providerId: String,
    val displayName: String,
    val supportsTools: Boolean = false,
    val supportsFim: Boolean = false,
    val supportsReasoning: Boolean = false,
    val lastProbedAt: Long = 0,
    val parameterSize: String? = null
)
