package rag.markdown_creator.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import rag.markdown_creator.application.port.in.ChatUseCase;

@Service
@RequiredArgsConstructor
public class ChatService implements ChatUseCase {

    private final AzureOpenAiChatModel chatModel;

    @Override
    public String chat(Prompt prompt) {
        ChatResponse response = chatModel.call(prompt);
        return response.getResult().getOutput().getText();
    }
}
