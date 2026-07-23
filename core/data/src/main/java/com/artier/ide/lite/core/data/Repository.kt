package com.artier.ide.lite.core.data

import com.artier.ide.lite.core.database.dao.CheckpointDao
import com.artier.ide.lite.core.database.dao.ConversationDao
import com.artier.ide.lite.core.database.dao.MessageDao
import com.artier.ide.lite.core.database.dao.ProjectDao
import com.artier.ide.lite.core.model.Checkpoint
import com.artier.ide.lite.core.model.Conversation
import com.artier.ide.lite.core.model.Message
import com.artier.ide.lite.core.model.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for checkpoint data
 */
@Singleton
class CheckpointRepository @Inject constructor(
    private val checkpointDao: CheckpointDao
) {
    fun getByProject(projectId: String): Flow<List<Checkpoint>> {
        return checkpointDao.getByProject(projectId).map { entities ->
            entities.map { entity ->
                Checkpoint(
                    id = entity.id,
                    sessionId = entity.sessionId,
                    projectId = entity.projectId,
                    filePath = entity.filePath,
                    diffPatch = entity.diffPatch,
                    timestamp = entity.timestamp,
                    aiActionDescription = entity.aiActionDescription,
                    lineNumber = entity.lineNumber
                )
            }
        }
    }

    suspend fun insert(checkpoint: Checkpoint) {
        checkpointDao.insert(
            com.artier.ide.lite.core.database.entity.CheckpointEntity(
                id = checkpoint.id,
                sessionId = checkpoint.sessionId,
                projectId = checkpoint.projectId,
                filePath = checkpoint.filePath,
                diffPatch = checkpoint.diffPatch,
                timestamp = checkpoint.timestamp,
                aiActionDescription = checkpoint.aiActionDescription,
                lineNumber = checkpoint.lineNumber
            )
        )
    }

    suspend fun deleteBySession(sessionId: String) {
        checkpointDao.deleteBySession(sessionId)
    }
}

/**
 * Repository for conversation data
 */
@Singleton
class ConversationRepository @Inject constructor(
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao
) {
    fun getByProject(projectId: String): Flow<List<Conversation>> {
        return conversationDao.getByProject(projectId).map { entities ->
            entities.map { entity ->
                Conversation(
                    id = entity.id,
                    projectId = entity.projectId,
                    mode = com.artier.ide.lite.core.model.AiMode.valueOf(entity.mode),
                    source = entity.source,
                    createdAt = entity.createdAt,
                    title = entity.title
                )
            }
        }
    }

    suspend fun insert(conversation: Conversation) {
        conversationDao.insert(
            com.artier.ide.lite.core.database.entity.ConversationEntity(
                id = conversation.id,
                projectId = conversation.projectId,
                mode = conversation.mode.name,
                source = conversation.source,
                createdAt = conversation.createdAt,
                title = conversation.title
            )
        )
    }

    suspend fun deleteById(id: String) {
        conversationDao.deleteById(id)
        messageDao.deleteByConversation(id)
    }
}

/**
 * Repository for message data
 */
@Singleton
class MessageRepository @Inject constructor(
    private val messageDao: MessageDao
) {
    fun getByConversation(conversationId: String): Flow<List<Message>> {
        return messageDao.getByConversation(conversationId).map { entities ->
            entities.map { entity ->
                Message(
                    id = entity.id,
                    conversationId = entity.conversationId,
                    role = com.artier.ide.lite.core.model.MessageRole.valueOf(entity.role),
                    content = entity.content,
                    toolCallJson = entity.toolCallJson,
                    timestamp = entity.timestamp
                )
            }
        }
    }

    suspend fun insert(message: Message) {
        messageDao.insert(
            com.artier.ide.lite.core.database.entity.MessageEntity(
                id = message.id,
                conversationId = message.conversationId,
                role = message.role.name,
                content = message.content,
                toolCallJson = message.toolCallJson,
                timestamp = message.timestamp
            )
        )
    }

    suspend fun insertAll(messages: List<Message>) {
        messageDao.insertAll(messages.map { message ->
            com.artier.ide.lite.core.database.entity.MessageEntity(
                id = message.id,
                conversationId = message.conversationId,
                role = message.role.name,
                content = message.content,
                toolCallJson = message.toolCallJson,
                timestamp = message.timestamp
            )
        })
    }
}

/**
 * Repository for project data
 */
@Singleton
class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao
) {
    fun getAll(): Flow<List<Project>> {
        return projectDao.getAll().map { entities ->
            entities.map { entity ->
                Project(
                    id = entity.id,
                    name = entity.name,
                    rootPath = entity.rootPath,
                    lastOpenedAt = entity.lastOpenedAt
                )
            }
        }
    }

    suspend fun getById(id: String): Project? {
        return projectDao.getById(id)?.let { entity ->
            Project(
                id = entity.id,
                name = entity.name,
                rootPath = entity.rootPath,
                lastOpenedAt = entity.lastOpenedAt
            )
        }
    }

    suspend fun getByPath(path: String): Project? {
        return projectDao.getByPath(path)?.let { entity ->
            Project(
                id = entity.id,
                name = entity.name,
                rootPath = entity.rootPath,
                lastOpenedAt = entity.lastOpenedAt
            )
        }
    }

    suspend fun insert(project: Project) {
        projectDao.insert(
            com.artier.ide.lite.core.database.entity.ProjectEntity(
                id = project.id,
                name = project.name,
                rootPath = project.rootPath,
                lastOpenedAt = project.lastOpenedAt
            )
        )
    }

    suspend fun updateLastOpened(id: String, timestamp: Long) {
        projectDao.updateLastOpened(id, timestamp)
    }

    suspend fun deleteById(id: String) {
        projectDao.deleteById(id)
    }
}
