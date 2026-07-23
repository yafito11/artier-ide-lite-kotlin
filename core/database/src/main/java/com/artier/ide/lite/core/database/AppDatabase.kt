package com.artier.ide.lite.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.artier.ide.lite.core.database.dao.CheckpointDao
import com.artier.ide.lite.core.database.dao.ConversationDao
import com.artier.ide.lite.core.database.dao.MessageDao
import com.artier.ide.lite.core.database.dao.ModelMetadataDao
import com.artier.ide.lite.core.database.dao.ProjectDao
import com.artier.ide.lite.core.database.entity.CheckpointEntity
import com.artier.ide.lite.core.database.entity.ConversationEntity
import com.artier.ide.lite.core.database.entity.MessageEntity
import com.artier.ide.lite.core.database.entity.ModelMetadataEntity
import com.artier.ide.lite.core.database.entity.ProjectEntity

@Database(
    entities = [
        CheckpointEntity::class,
        ConversationEntity::class,
        MessageEntity::class,
        ModelMetadataEntity::class,
        ProjectEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun checkpointDao(): CheckpointDao
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
    abstract fun modelMetadataDao(): ModelMetadataDao
    abstract fun projectDao(): ProjectDao
}
