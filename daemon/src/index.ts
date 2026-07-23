import Fastify from 'fastify';
import websocket from '@fastify/websocket';
import cors from '@fastify/cors';
import { AgentLoop } from './agent-loop.js';
import { AutocompleteService } from './autocomplete.js';
import { QuickEditService } from './quickedit.js';
import { CheckpointManager } from './checkpoint.js';

const PORT = parseInt(process.env.PORT || '9527');

const fastify = Fastify({
  logger: true
});

// Register plugins
await fastify.register(websocket);
await fastify.register(cors);

// Initialize services
const agentLoop = new AgentLoop();
const autocompleteService = new AutocompleteService();
const quickEditService = new QuickEditService();
const checkpointManager = new CheckpointManager();

// Health check endpoint
fastify.get('/api/health', async () => {
  return { status: 'ok', timestamp: Date.now() };
});

// Autocomplete endpoint
fastify.post('/api/autocomplete', async (request, reply) => {
  const { prefix, suffix, language, modelId } = request.body as any;
  const result = await autocompleteService.getCompletion(prefix, suffix, language, modelId);
  return result;
});

// Quick edit endpoint
fastify.post('/api/quickedit', async (request, reply) => {
  const { selectedCode, surroundingContext, instruction, filePath, language } = request.body as any;
  const result = await quickEditService.getEdit(selectedCode, surroundingContext, instruction, filePath, language);
  return result;
});

// Checkpoint endpoints
fastify.post('/api/checkpoint/create', async (request, reply) => {
  const { projectId, filePath, diffPatch, description } = request.body as any;
  const result = await checkpointManager.createCheckpoint(projectId, filePath, diffPatch, description);
  return result;
});

fastify.get('/api/checkpoint/:projectId', async (request, reply) => {
  const { projectId } = request.params as any;
  const checkpoints = await checkpointManager.getCheckpoints(projectId);
  return checkpoints;
});

fastify.post('/api/checkpoint/restore', async (request, reply) => {
  const { checkpointId } = request.body as any;
  const result = await checkpointManager.restoreCheckpoint(checkpointId);
  return result;
});

// WebSocket endpoint for streaming chat
fastify.get('/ws', { websocket: true }, (socket, request) => {
  socket.on('message', async (message) => {
    try {
      const data = JSON.parse(message.toString());

      if (data.type === 'chat') {
        const { message: userMessage, mode, history } = JSON.parse(data.payload);

        // Stream response
        const stream = agentLoop.chat(userMessage, mode, history);

        for await (const chunk of stream) {
          socket.send(JSON.stringify({
            id: data.id,
            type: 'stream',
            chunk: chunk
          }));
        }

        socket.send(JSON.stringify({
          id: data.id,
          type: 'done'
        }));
      }
    } catch (error) {
      socket.send(JSON.stringify({
        id: data?.id || 'unknown',
        type: 'error',
        message: error instanceof Error ? error.message : 'Unknown error'
      }));
    }
  });

  socket.on('close', () => {
    fastify.log.info('WebSocket client disconnected');
  });
});

// Start server
try {
  await fastify.listen({ port: PORT, host: '0.0.0.0' });
  fastify.log.info(`Artier Daemon running on port ${PORT}`);
} catch (err) {
  fastify.log.error(err);
  process.exit(1);
}
