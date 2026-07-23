import { generateText } from 'ai';
import { createOpenAI } from '@ai-sdk/openai';

interface QuickEditResult {
  oldBlock: string;
  newBlock: string;
  explanation: string;
}

export class QuickEditService {
  private openai: ReturnType<typeof createOpenAI>;

  constructor() {
    this.openai = createOpenAI({
      baseURL: process.env.AI_BASE_URL || 'https://api.openai.com/v1',
      apiKey: process.env.AI_API_KEY || ''
    });
  }

  /**
   * Get edit suggestion for selected code
   */
  async getEdit(
    selectedCode: string,
    surroundingContext: string,
    instruction: string,
    filePath: string,
    language: string
  ): Promise<QuickEditResult> {
    const prompt = `You are a code editor assistant. The user wants to edit the following code.

File: ${filePath}
Language: ${language}

Selected code:
\`\`\`${language}
${selectedCode}
\`\`\`

Surrounding context:
\`\`\`${language}
${surroundingContext}
\`\`\`

User instruction: ${instruction}

Provide the edited code. Return ONLY the new code block without explanation.`;

    try {
      const { text } = await generateText({
        model: this.openai('gpt-4o-mini'),
        prompt,
        maxTokens: 1000
      });

      // Extract code block from response
      const codeMatch = text.match(/```(?:\w+)?\n([\s\S]*?)```/);
      const newBlock = codeMatch ? codeMatch[1].trim() : text.trim();

      return {
        oldBlock: selectedCode,
        newBlock,
        explanation: `Applied edit: ${instruction}`
      };
    } catch (error) {
      console.error('Quick edit error:', error);
      return {
        oldBlock: selectedCode,
        newBlock: selectedCode,
        explanation: 'Failed to generate edit'
      };
    }
  }
}
