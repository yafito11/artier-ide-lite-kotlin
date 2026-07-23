import { streamText } from 'ai';
import { createOpenAI } from '@ai-sdk/openai';

interface ChatMessage {
  role: 'user' | 'assistant' | 'system';
  content: string;
}

interface ToolCall {
  name: string;
  arguments: Record<string, any>;
}

export class AgentLoop {
  private openai: ReturnType<typeof createOpenAI>;

  constructor() {
    // Initialize with OpenAI-compatible provider (NaraRouter, 9Router, or BYOK)
    this.openai = createOpenAI({
      baseURL: process.env.AI_BASE_URL || 'https://api.openai.com/v1',
      apiKey: process.env.AI_API_KEY || ''
    });
  }

  /**
   * Chat with AI model
   */
  async *chat(
    message: string,
    mode: string,
    history: [string, string][] = []
  ): AsyncGenerator<string> {
    const messages: ChatMessage[] = [
      {
        role: 'system',
        content: this.getSystemPrompt(mode)
      },
      ...history.map(([role, content]) => ({
        role: role as 'user' | 'assistant',
        content
      })),
      {
        role: 'user',
        content: message
      }
    ];

    const result = streamText({
      model: this.openai('gpt-4o-mini'),
      messages,
      tools: mode === 'agent' ? this.getTools() : undefined,
      maxSteps: mode === 'agent' ? 10 : 1
    });

    for await (const chunk of result.textStream) {
      yield chunk;
    }
  }

  /**
   * Get system prompt based on mode
   */
  private getSystemPrompt(mode: string): string {
    const basePrompt = `You are Artier AI Assistant, an expert coding assistant for the Artier IDE Lite.
You help users with coding tasks, debugging, and software development.

Target device: Xiaomi/Redmi Pad SE 11" (4GB RAM, Snapdragon 680)
Architecture: Kotlin/Jetpack Compose + Node.js daemon`;

    switch (mode) {
      case 'agent':
        return `${basePrompt}

You are in Agent Mode. You have full access to tools:
- Read files
- Write files
- Edit files
- Execute shell commands
- Search files

Use these tools to help the user with their coding tasks. Always explain what you're doing.`;

      case 'gather':
        return `${basePrompt}

You are in Gather Mode. You can only READ files and search the codebase.
You CANNOT modify files or execute commands.
Help the user explore and understand their code.`;

      case 'chat':
        return `${basePrompt}

You are in Chat Mode. You cannot access the codebase or use any tools.
Have a general conversation about programming and software development.`;

      default:
        return basePrompt;
    }
  }

  /**
   * Get available tools for agent mode
   */
  private getTools() {
    return {
      read_file: {
        description: 'Read the contents of a file',
        parameters: {
          path: {
            type: 'string',
            description: 'The file path to read'
          }
        },
        execute: async ({ path }: { path: string }) => {
          // TODO: Implement file reading
          return `Reading file: ${path}`;
        }
      },
      write_file: {
        description: 'Write content to a file',
        parameters: {
          path: {
            type: 'string',
            description: 'The file path to write'
          },
          content: {
            type: 'string',
            description: 'The content to write'
          }
        },
        execute: async ({ path, content }: { path: string; content: string }) => {
          // TODO: Implement file writing
          return `Written to file: ${path}`;
        }
      },
      edit_file: {
        description: 'Edit a file by replacing a specific section',
        parameters: {
          path: {
            type: 'string',
            description: 'The file path to edit'
          },
          search: {
            type: 'string',
            description: 'The text to search for'
          },
          replace: {
            type: 'string',
            description: 'The replacement text'
          }
        },
        execute: async ({ path, search, replace }: { path: string; search: string; replace: string }) => {
          // TODO: Implement file editing
          return `Edited file: ${path}`;
        }
      },
      run_command: {
        description: 'Execute a shell command',
        parameters: {
          command: {
            type: 'string',
            description: 'The command to execute'
          }
        },
        execute: async ({ command }: { command: string }) => {
          // TODO: Implement command execution
          return `Executed: ${command}`;
        }
      },
      search_files: {
        description: 'Search for files matching a pattern',
        parameters: {
          pattern: {
            type: 'string',
            description: 'The search pattern (glob)'
          }
        },
        execute: async ({ pattern }: { pattern: string }) => {
          // TODO: Implement file search
          return `Searching for: ${pattern}`;
        }
      }
    };
  }
}
