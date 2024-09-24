package com.devoxx.genie.daemon;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import org.jboss.logging.Logger;

import com.devoxx.genie.chatmodel.ChatModelFactoryProvider;
import com.devoxx.genie.chatmodel.ollama.OllamaChatModelFactory;
import com.devoxx.genie.model.ChatModel;
import com.devoxx.genie.model.Constant;
import com.devoxx.genie.model.LanguageModel;
import com.devoxx.genie.model.dto.LanguageModelAdapter;
import com.devoxx.genie.model.dto.ModelProviderDTO;
import com.devoxx.genie.model.enumarations.ModelProvider;
import com.devoxx.genie.model.request.ChatMessageContext;
import com.devoxx.genie.service.DaemonChatMemoryService;
import com.devoxx.genie.service.DevoxxGenieSettingsService;
import com.devoxx.genie.service.LLMProviderService;
import com.devoxx.genie.service.PromptExecutionService;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class DevoxxGenieDaemon {

    // inject a logger for this quarkus application
    private static final Logger LOG = Logger.getLogger(DevoxxGenieDaemon.class.getName());


    @Path("/world")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {
        // Call<ModelsListResponse> models = ollamaApi.listModels();

        return "Hello, World!";
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() throws InterruptedException, ExecutionException {

        System.out.println("Hello, World!");
        System.out.println("ollamaurl :" + DevoxxGenieSettingsService.getInstance().getOllamaModelUrl());

        DaemonChatMemoryService chatMemoryService = DaemonChatMemoryService.getInstance();
        chatMemoryService.init();

        ChatModel chatModel = new ChatModel();
        chatModel.setModelName("llama3");
        chatModel.setBaseUrl(Constant.OLLAMA_MODEL_URL);

        ChatLanguageModel chatLanguageModel = new OllamaChatModelFactory().createChatModel(chatModel);
        // check if the chatLanguageModel is null
        if (chatLanguageModel == null) {
            return "chatLanguageModel is null";
        }

        ChatMessageContext context = ChatMessageContext.builder()
                .userPrompt("Can you help me with this?")
                .chatLanguageModel(chatLanguageModel)
                .build();
        PromptExecutionService promptExecutionService = PromptExecutionService.getInstance();
        CompletableFuture<Optional<AiMessage>> result = promptExecutionService.executeQuery(context);
        // return the result
        return result.get().map(AiMessage::text).orElse("No response");
    }

    @GET
    @Path("/modelProviders")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ModelProviderDTO> getModelProviders() {
        Stream<ModelProvider> providers = getModelProvider();
        return providers.map(
                        provider -> new ModelProviderDTO(provider.name(), provider.getName()))
                .toList();

    }

    public static Stream<ModelProvider> getModelProvider() {
        LLMProviderService providerService = LLMProviderService.getInstance();
        Stream<ModelProvider> sorted = Stream.concat(
                providerService.getModelProvidersWithApiKeyConfigured().stream(),
                providerService.getLocalModelProviders().stream())
                .distinct()
                .sorted(Comparator.comparing(ModelProvider::getName));
        return sorted;
    }

    @GET
    @Path("/chatmodels")
    @Produces(MediaType.APPLICATION_JSON)
    // with parameter provider
    public List<LanguageModelAdapter> getChatModels(@QueryParam("modelProvider") String provider) {
        // return the list of chat models
        return ChatModelFactoryProvider.getFactoryByProvider(provider).get().getModels().stream()
                .map(LanguageModelAdapter::new).toList();
    }

    // set the model provider
    @GET
    @Path("/setSelectedProvider")
    @Produces(MediaType.TEXT_PLAIN)
    public String setModelProvider( @QueryParam("projectPath") String projectPath, @QueryParam("modelProvider") String provider) {
        // set the model provider
        DevoxxGenieSettingsService.getInstance().setSelectedProvider(projectPath, provider);
        LOG.info("Model provider set to " + provider + " for project " + projectPath);
        return "Model provider set to " + provider + " for project " + projectPath;
    }

    // set the model
    @GET
    @Path("/setSelectedModel")
    @Produces(MediaType.TEXT_PLAIN)
    public String setLanguageModel( @QueryParam("projectPath") String projectPath, @QueryParam("model") String model) {
        // set the model
        DevoxxGenieSettingsService.getInstance().setSelectedLanguageModel(projectPath, model);
       LOG.info("Language Model set to " + model + " for project " + projectPath);
        return "Language Model set to " + model + " for project " + projectPath;
    }

    // 

}
