package com.artier.ide.lite.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.artier.ide.lite.core.database.entity.ModelMetadataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ModelMetadataDao {
    @Query("SELECT * FROM model_metadata WHERE providerId = :providerId")
    fun getByProvider(providerId: String): Flow<List<ModelMetadataEntity>>

    @Query("SELECT * FROM model_metadata WHERE supportsFim = 1")
    fun getFimCapable(): Flow<List<ModelMetadataEntity>>

    @Query("SELECT * FROM model_metadata WHERE supportsTools = 1")
    fun getToolsCapable(): Flow<List<ModelMetadataEntity>>

    @Query("SELECT * FROM model_metadata WHERE modelId = :modelId")
    suspend fun getById(modelId: String): ModelMetadataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(metadata: ModelMetadataEntity)

    @Query("DELETE FROM model_metadata WHERE providerId = :providerId")
    suspend fun deleteByProvider(providerId: String)
}
