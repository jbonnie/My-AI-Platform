package rag.markdown_creator.application.port.in;

import org.springframework.ai.chat.prompt.Prompt;

public interface GeneratePromptUseCase {
    Prompt execute(String documentContent);
}
