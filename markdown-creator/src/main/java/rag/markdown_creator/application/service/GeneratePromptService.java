package rag.markdown_creator.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.azure.openai.AzureOpenAiChatOptions;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rag.markdown_creator.application.port.in.GeneratePromptUseCase;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneratePromptService implements GeneratePromptUseCase {

    @Value("${prompt.system-message}")
    private String systemMessage;

    @Override
    public Prompt execute(String documentContent) {
        String userMessage = "변환하고자하는 문서의 내용은 아래와 같습니다.\n\n" + documentContent;
        List<Message> messages = List.of(
                new SystemMessage(systemMessage),
                new UserMessage(userMessage)
        );
        return Prompt.builder()
                .chatOptions(getOptions())
                .messages(messages)
                .build();
    }

    private AzureOpenAiChatOptions getOptions() {
        return AzureOpenAiChatOptions.builder()
                .temperature(0.5)
                .build();
    }
}
