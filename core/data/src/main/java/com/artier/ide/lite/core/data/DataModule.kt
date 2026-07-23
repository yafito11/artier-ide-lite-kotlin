package com.artier.ide.lite.core.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.artier.ide.lite.core.database.dao.CheckpointDao
import com.artier.ide.lite.core.database.dao.ConversationDao
import com.artier.ide.lite.core.database.dao.MessageDao
import com.artier.ide.lite.core.database.dao.ProjectDao

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideCheckpointRepository(checkpointDao: CheckpointDao): CheckpointRepository {
        return CheckpointRepository(checkpointDao)
    }

    @Provides
    fun provideConversationRepository(
        conversationDao: ConversationDao,
        messageDao: MessageDao
    ): ConversationRepository {
        return ConversationRepository(conversationDao, messageDao)
    }

    @Provides
    fun provideMessageRepository(messageDao: MessageDao): MessageRepository {
        return MessageRepository(messageDao)
    }

    @Provides
    fun provideProjectRepository(projectDao: ProjectDao): ProjectRepository {
        return ProjectRepository(projectDao)
    }
}
