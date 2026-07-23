import { streamText } from 'ai';
import { createOpenAI } from '@ai-sdk/openai';

interface CompletionResult {
  completion: string;
  isPartial: boolean;
}

export class AutocompleteService {
  private openai: ReturnType<typeof createOpenAI>;

  constructor() {
    this.openai = createOpenAI({
      baseURL: process.env.AUTOCOMPLETE_BASE_URL || process.env.AI_BASE_URL || 'https://api.openai.com/v1',
      apiKey: process.env.AUTOCOMPLETE_API_KEY || process.env.AI_API_KEY || ''
    });
  }

  /**
   * Get code completion using FIM (Fill-In-the-Middle)
   */
  async getCompletion(
    prefix: string,
    suffix: string,
    language: string,
    modelId?: string
  ): Promise<CompletionResult> {
    const model = modelId || 'gpt-4o-mini';

    const prompt = this.buildFimPrompt(prefix, suffix, language);

    try {
      const result = await streamText({
        model: this.openai(model),
        prompt,
        maxTokens: 150,
        temperature: 0.2
      });

      let completion = '';
      for await (const chunk of result.textStream) {
        completion += chunk;
      }

      return {
        completion: completion.trim(),
        isPartial: false
      };
    } catch (error) {
      console.error('Autocomplete error:', error);
      return {
        completion: '',
        isPartial: false
      };
    }
  }

  /**
   * Build FIM prompt
   */
  private buildFimPrompt(prefix: string, suffix: string, language: string): string {
    // Use standard FIM format
    return `<fim_prefix>${prefix}<fim_suffix>${suffix}<fim_middle>`;
  }
}
