package com.devoxx.genie.daemon;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.jboss.logging.Logger;

import com.devoxx.genie.FileAdapter;
import com.devoxx.genie.chatmodel.ChatModelFactoryProvider;
import com.devoxx.genie.chatmodel.ChatModelProvider;
import com.devoxx.genie.model.LanguageModel;
import com.devoxx.genie.model.enumarations.ModelProvider;
import com.devoxx.genie.model.request.ChatMessageContext;
import com.devoxx.genie.model.request.EditorInfo;
import com.devoxx.genie.service.ChatMemoryService;
import com.devoxx.genie.service.DevoxxGenieSettingsService;
import com.devoxx.genie.service.PromptExecutionService;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.Session;

@ServerEndpoint("/chat")
@ApplicationScoped
public class DevoxxGenieDaemonMessages {

    private static final Logger LOG = Logger.getLogger(DevoxxGenieDaemonMessages.class.getName());

    private static final Set<Session> sessions = new HashSet<>();

    private final ChatModelProvider chatModelProvider = new ChatModelProvider();


    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        LOG.info("New session opened: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        LOG.info("Session closed: " + session.getId());
    }

    @OnMessage
    public void onMessage(String jsonMessage, Session session) throws InterruptedException, ExecutionException {
        // prettier json
        
        JsonObject jsonObject = new JsonObject(jsonMessage);

        LOG.info("Received message from " + session.getId() + ": " + jsonObject.encodePrettily());
        String projectPath = jsonObject.getString("projectPath");
        String message = jsonObject.getString("message");
        String selectedText = jsonObject.getString("selectedText");
        List<String> selectedFiles = jsonObject.containsKey("openFiles") ? jsonObject.getJsonArray("openFiles").getList() : List.of();


        // should get the list of selected files
        // should get selected text
        EditorInfo editorInfo = new EditorInfo();
        editorInfo.setSelectedText(selectedText);
        List<FileAdapter> files = // for each selectedFiles, use new JavaFileAdapter(new File) in a stream
            selectedFiles.stream()
                .map(File::new).filter(File::exists)
                .map(JavaFileAdapter::new)
                .map(FileAdapter.class::cast)
                .toList();
        editorInfo.setSelectedFiles(files);

        

        // DaemonChatMemoryService chatMemoryService = DaemonChatMemoryService.getInstance();
        // chatMemoryService.init();
        // get setting service
        DevoxxGenieSettingsService settingsService = DevoxxGenieSettingsService.getInstance();

        ChatMemoryService.getInstance().init();


        String modelName = settingsService.getSelectedLanguageModel(projectPath);

        List<LanguageModel> models = ChatModelFactoryProvider.getFactoryByProvider(settingsService.getSelectedProvider(projectPath)).get().getModels();
        // get the model from the list of models and the selected model name
        LanguageModel selectedLanguageModel = models.stream()
                .filter(model -> model.getDisplayName().equals(modelName))
                .findFirst()
                .orElse(null);
        // if no language model is found, use createDefaultLanguageModel (to copy from ActionButtonsPanel#createDefaultLanguageModel)

        if (selectedLanguageModel == null) {
            selectedLanguageModel = createDefaultLanguageModel(projectPath);
        }
        // See com.devoxx.genie.ui.panel.ActionButtonsPanel#validateAndPreparePrompt() which calls  ChatMessageContextUtil#createContext
        ChatMessageContext context = ChatMessageContext.builder()
            //.project(new IntellijProjectHandler(project))
            .editorInfo(editorInfo)
            .name(String.valueOf(System.currentTimeMillis()))
            .userPrompt(message)
            .userMessage(UserMessage.userMessage(message))
            .languageModel(selectedLanguageModel) // retrieved from modelNameComboBox.getSelectedItem(); (see ActionButtonsPanel#validateAndPreparePrompt)
            //.webSearchRequested(actionCommand.equals(Constant.TAVILY_SEARCH_ACTION) ||
            //                    actionCommand.equals(Constant.GOOGLE_SEARCH_ACTION))
            //.totalFileCount(totalFileCount)
            .build();

        // copied from ChatMessageContextUtil#createContext
        context.setChatLanguageModel(chatModelProvider.getChatLanguageModel(context));
        int ZERO_SECONDS = 0;
        int SIXTY_SECONDS = 60;
        context.setTimeout(settingsService.getTimeout() == ZERO_SECONDS ? SIXTY_SECONDS : settingsService.getTimeout());
        context.setTimeout(120);
        PromptExecutionService promptExecutionService = PromptExecutionService.getInstance();
        CompletableFuture<Optional<AiMessage>> result = promptExecutionService.executeQuery(context);
        
        result.get().ifPresentOrElse(aiMessage -> {
            // add the aiMessage to the chat memory
            context.setAiMessage(aiMessage);
            broadcast("AI: ", aiMessage.text());
        }, () -> {
            broadcast("AI", "No response");
        });
    }



    private LanguageModel createDefaultLanguageModel(String projectPath) {
        ModelProvider selectedProvider = getSelectedProvider(projectPath);
        if (selectedProvider != null &&
            (selectedProvider.equals(ModelProvider.LMStudio) ||
             selectedProvider.equals(ModelProvider.GPT4All) ||
             selectedProvider.equals(ModelProvider.LLaMA))) {
            return LanguageModel.builder()
                .provider(selectedProvider)
                .apiKeyUsed(false)
                .inputCost(0)
                .outputCost(0)
                .contextWindow(4096)
                .build();
        } else {
            String modelName = DevoxxGenieSettingsService.getInstance().getSelectedLanguageModel(projectPath);
            return LanguageModel.builder()
                .provider(selectedProvider != null ? selectedProvider : ModelProvider.OpenAI)
                .modelName(modelName)
                .apiKeyUsed(false).inputCost(0).outputCost(0).contextWindow(128_000).build();
        }
    }

    private ModelProvider getSelectedProvider(String projectPath) {
        // settings service get selected provider
        DevoxxGenieSettingsService settingsService = DevoxxGenieSettingsService.getInstance();
        String provider = settingsService.getSelectedProvider(projectPath);
        return ModelProvider.valueOf(provider);
    }

    private void broadcast(String user, String message) {
        sessions.forEach(s -> {
            JsonObject jsonResponse = new JsonObject()
                    .put("user", user)
                    .put("content", message);
            String jsonMessage = jsonResponse.encode();
            // log the message
            LOG.info("Sending message to " + s.getId() + ": " + jsonMessage);
            s.getAsyncRemote().sendText(jsonMessage, result -> {
                if (result.getException() != null) {
                    LOG.error("Unable to send message", result.getException());
                }
            });
        });
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOG.error("Error on session " + session.getId(), throwable);
    }


    /** duplicating and adapting from ChatModelProvider */
    // private ChatMessageContext createContext(String message) {
    //     // get settings service
    //     DevoxxGenieSettingsService settingsService = DevoxxGenieSettingsService.getInstance();

    //     LanguageModel languageModel = 

    //     ChatMessageContext context = ChatMessageContext.builder()
    //     .name(String.valueOf(System.currentTimeMillis()))
    //             .userPrompt(message)
    //             .userMessage(UserMessage.userMessage(message))
    //             .chatLanguageModel())
    //             .build();
    // }
}
