import Database from 'better-sqlite3';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

interface Checkpoint {
  id: string;
  sessionId: string;
  projectId: string;
  filePath: string;
  diffPatch: string;
  timestamp: number;
  aiActionDescription: string;
  lineNumber: number;
}

export class CheckpointManager {
  private db: Database.Database;

  constructor() {
    const dbPath = path.join(__dirname, '..', 'checkpoints.db');
    this.db = new Database(dbPath);
    this.initDatabase();
  }

  private initDatabase() {
    this.db.exec(`
      CREATE TABLE IF NOT EXISTS checkpoints (
        id TEXT PRIMARY KEY,
        sessionId TEXT NOT NULL,
        projectId TEXT NOT NULL,
        filePath TEXT NOT NULL,
        diffPatch TEXT NOT NULL,
        timestamp INTEGER NOT NULL,
        aiActionDescription TEXT,
        lineNumber INTEGER
      )
    `);
  }

  /**
   * Create a new checkpoint
   */
  async createCheckpoint(
    projectId: string,
    filePath: string,
    diffPatch: string,
    description: string,
    lineNumber: number = 0
  ): Promise<Checkpoint> {
    const checkpoint: Checkpoint = {
      id: crypto.randomUUID(),
      sessionId: crypto.randomUUID(),
      projectId,
      filePath,
      diffPatch,
      timestamp: Date.now(),
      aiActionDescription: description,
      lineNumber
    };

    const stmt = this.db.prepare(`
      INSERT INTO checkpoints (id, sessionId, projectId, filePath, diffPatch, timestamp, aiActionDescription, lineNumber)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    `);

    stmt.run(
      checkpoint.id,
      checkpoint.sessionId,
      checkpoint.projectId,
      checkpoint.filePath,
      checkpoint.diffPatch,
      checkpoint.timestamp,
      checkpoint.aiActionDescription,
      checkpoint.lineNumber
    );

    return checkpoint;
  }

  /**
   * Get all checkpoints for a project
   */
  async getCheckpoints(projectId: string): Promise<Checkpoint[]> {
    const stmt = this.db.prepare(`
      SELECT * FROM checkpoints
      WHERE projectId = ?
      ORDER BY timestamp DESC
    `);

    return stmt.all(projectId) as Checkpoint[];
  }

  /**
   * Get checkpoints for a specific file
   */
  async getFileCheckpoints(filePath: string): Promise<Checkpoint[]> {
    const stmt = this.db.prepare(`
      SELECT * FROM checkpoints
      WHERE filePath = ?
      ORDER BY timestamp DESC
    `);

    return stmt.all(filePath) as Checkpoint[];
  }

  /**
   * Restore to a specific checkpoint
   */
  async restoreCheckpoint(checkpointId: string): Promise<{ success: boolean; filePath: string; diffPatch: string }> {
    const stmt = this.db.prepare(`
      SELECT * FROM checkpoints WHERE id = ?
    `);

    const checkpoint = stmt.get(checkpointId) as Checkpoint | undefined;

    if (!checkpoint) {
      return { success: false, filePath: '', diffPatch: '' };
    }

    // Return the diff patch for the caller to apply
    return {
      success: true,
      filePath: checkpoint.filePath,
      diffPatch: checkpoint.diffPatch
    };
  }

  /**
   * Delete checkpoints for a session
   */
  async deleteSessionCheckpoints(sessionId: string): Promise<void> {
    const stmt = this.db.prepare(`
      DELETE FROM checkpoints WHERE sessionId = ?
    `);

    stmt.run(sessionId);
  }

  /**
   * Delete all checkpoints for a project
   */
  async deleteProjectCheckpoints(projectId: string): Promise<void> {
    const stmt = this.db.prepare(`
      DELETE FROM checkpoints WHERE projectId = ?
    `);

    stmt.run(projectId);
  }
}
