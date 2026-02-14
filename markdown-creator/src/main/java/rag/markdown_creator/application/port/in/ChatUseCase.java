package rag.markdown_creator.application.port.in;

import org.springframework.ai.chat.prompt.Prompt;

public interface ChatUseCase {

    String chat(Prompt prompt);
}
