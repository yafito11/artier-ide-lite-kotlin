package com.artier.ide.lite.core.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "artier-ide-lite.db"
        ).build()
    }

    @Provides
    fun provideCheckpointDao(database: AppDatabase) = database.checkpointDao()

    @Provides
    fun provideConversationDao(database: AppDatabase) = database.conversationDao()

    @Provides
    fun provideMessageDao(database: AppDatabase) = database.messageDao()

    @Provides
    fun provideModelMetadataDao(database: AppDatabase) = database.modelMetadataDao()

    @Provides
    fun provideProjectDao(database: AppDatabase) = database.projectDao()
}
