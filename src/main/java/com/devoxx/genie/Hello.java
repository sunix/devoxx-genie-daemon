package com.devoxx.genie;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.devoxx.genie.chatmodel.ollama.OllamaChatModelFactory;
import com.devoxx.genie.model.ChatModel;
import com.devoxx.genie.model.Constant;
import com.devoxx.genie.model.request.ChatMessageContext;
import com.devoxx.genie.service.DaemonChatMemoryService;
import com.devoxx.genie.service.DevoxxGenieSettingsService;
import com.devoxx.genie.service.PromptExecutionService;

import dev.langchain4j.data.message.AiMessage;

public class Hello {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("Hello, World!");
        System.out.println("ollamaurl :" + DevoxxGenieSettingsService.getInstance().getOllamaModelUrl());

        DaemonChatMemoryService chatMemoryService = DaemonChatMemoryService.getInstance();
        chatMemoryService.init();

         ChatModel chatModel = new ChatModel();
            chatModel.setModelName("llama3");
            chatModel.setBaseUrl(Constant.OLLAMA_MODEL_URL);

        ChatMessageContext context = ChatMessageContext.builder()
            .userPrompt("Can you help me with this?")
            .chatLanguageModel(new OllamaChatModelFactory().createChatModel(chatModel))
            .build();
        PromptExecutionService promptExecutionService = PromptExecutionService.getInstance();
        CompletableFuture<Optional<AiMessage>> result = promptExecutionService.executeQuery(context);
        result.get().ifPresent(aiMessage -> System.out.println(aiMessage.text()));
        
    }
}
