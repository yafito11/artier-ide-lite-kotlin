package com.artier.ide.lite.ai.checkpoint

import com.artier.ide.lite.core.database.dao.CheckpointDao
import com.artier.ide.lite.core.database.entity.CheckpointEntity
import com.artier.ide.lite.core.model.Checkpoint
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Checkpoint manager for diff-based snapshots
 */
@Singleton
class CheckpointManager @Inject constructor(
    private val checkpointDao: CheckpointDao
) {
    /**
     * Create a new checkpoint
     */
    suspend fun createCheckpoint(
        projectId: String,
        filePath: String,
        diffPatch: String,
        description: String,
        lineNumber: Int = 0
    ): Checkpoint {
        val entity = CheckpointEntity(
            id = UUID.randomUUID().toString(),
            sessionId = UUID.randomUUID().toString(),
            projectId = projectId,
            filePath = filePath,
            diffPatch = diffPatch,
            timestamp = System.currentTimeMillis(),
            aiActionDescription = description,
            lineNumber = lineNumber
        )

        checkpointDao.insert(entity)

        return Checkpoint(
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

    /**
     * Get checkpoints for a project
     */
    suspend fun getCheckpoints(projectId: String): List<Checkpoint> {
        return checkpointDao.getByProject(projectId).first().map { entity ->
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

    /**
     * Restore to a checkpoint
     */
    suspend fun restoreCheckpoint(checkpointId: String): Checkpoint? {
        val entity = checkpointDao.getById(checkpointId) ?: return null

        return Checkpoint(
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
