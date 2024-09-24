package com.devoxx.genie.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.devoxx.genie.model.Constant.*;
import com.devoxx.genie.model.CustomPrompt;
import com.devoxx.genie.model.LanguageModel;
import com.devoxx.genie.model.enumarations.ModelProvider;


/**
 * This class is a service that provides settings for the DevoxxGenieDaemon. It is a in memory service settings that
 * could be populated from a configuration file from the VSCode extension.
 */
public class DaemonSettingsService implements DevoxxGenieSettingsService {

    private static DaemonSettingsService instance;

    // quite a copy of IntelliJ IDEA's settings service
    private List<CustomPrompt> customPrompts = new ArrayList<>();

    private List<LanguageModel> languageModels = new ArrayList<>();

    private String ollamaModelUrl = OLLAMA_MODEL_URL;
    private String lmstudioModelUrl = LMSTUDIO_MODEL_URL;
    private String gpt4allModelUrl = GPT4ALL_MODEL_URL;
    private String janModelUrl = JAN_MODEL_URL;
    private String exoModelUrl = EXO_MODEL_URL;
    private String llamaCPPUrl = LLAMA_CPP_MODEL_URL;

    // LLM API Keys
    private String openAIKey = "";
    private String mistralKey = "";
    private String anthropicKey = "";
    private String groqKey = "";
    private String deepInfraKey = "";
    private String geminiKey = "";

    // Search API Keys
    private Boolean hideSearchButtonsFlag = HIDE_SEARCH_BUTTONS;
    private String googleSearchKey = "";
    private String googleCSIKey = "";
    private String tavilySearchKey = "";
    private Integer maxSearchResults = MAX_SEARCH_RESULTS;

    // Last selected language model
    private Map<String, String> lastSelectedProvider;
    private Map<String, String> lastSelectedLanguageModel;

    // Enable stream mode
    private Boolean streamMode = STREAM_MODE;

    // LLM settings
    private Double temperature = TEMPERATURE;
    private Double topP = TOP_P;

    private Integer timeout = TIMEOUT;
    private Integer maxRetries = MAX_RETRIES;
    private Integer chatMemorySize = MAX_MEMORY;
    private Integer maxOutputTokens = MAX_OUTPUT_TOKENS;

    // Enable AST mode
    private Boolean astMode = AST_MODE;
    private Boolean astParentClass = AST_PARENT_CLASS;
    private Boolean astClassReference = AST_CLASS_REFERENCE;
    private Boolean astFieldReference = AST_FIELD_REFERENCE;

    private String systemPrompt = SYSTEM_PROMPT;
    private String testPrompt = TEST_PROMPT;
    private String reviewPrompt = REVIEW_PROMPT;
    private String explainPrompt = EXPLAIN_PROMPT;

    private Boolean excludeJavaDoc = false;

    private List<String> excludedDirectories = new ArrayList<>(Arrays.asList(
        "build", ".git", "bin", "out", "target", "node_modules", ".idea"
    ));

    private List<String> includedFileExtensions = new ArrayList<>(Arrays.asList(
        "java", "kt", "groovy", "scala", "xml", "json", "yaml", "yml", "properties", "txt", "md"
    ));

    private Map<String, Double> modelInputCosts = new HashMap<>();
    private Map<String, Double> modelOutputCosts = new HashMap<>();

    private Map<String, Integer> modelWindowContexts = new HashMap<>();
    private Integer defaultWindowContext = 8000;

    private DaemonSettingsService() {
        initializeDefaultPrompts();
    }

    private void initializeDefaultPrompts() {
        if (customPrompts.isEmpty()) {
            customPrompts.add(new CustomPrompt("test", TEST_PROMPT));
            customPrompts.add(new CustomPrompt("explain", EXPLAIN_PROMPT));
            customPrompts.add(new CustomPrompt("review", REVIEW_PROMPT));
        }
    }

    public static DaemonSettingsService getInstance() {
        if (instance == null) {
            instance = new DaemonSettingsService();
        }
        return instance;
    }

    @Override
    public List<CustomPrompt> getCustomPrompts() {
        return customPrompts;
    }

    @Override
    public void setCustomPrompts(List<CustomPrompt> customPrompts) {
        this.customPrompts = customPrompts;
    }

    @Override
    public List<LanguageModel> getLanguageModels() {
        return languageModels;
    }

    @Override
    public void setLanguageModels(List<LanguageModel> languageModels) {
        this.languageModels = languageModels;
    }

    @Override
    public String getOllamaModelUrl() {
        return ollamaModelUrl;
    }

    @Override
    public void setOllamaModelUrl(String url) {
        this.ollamaModelUrl = url;
    }

    @Override
    public String getLmstudioModelUrl() {
        return lmstudioModelUrl;
    }

    @Override
    public void setLmstudioModelUrl(String url) {
        this.lmstudioModelUrl = url;
    }

    @Override
    public String getGpt4allModelUrl() {
        return gpt4allModelUrl;
    }

    @Override
    public void setGpt4allModelUrl(String url) {
        this.gpt4allModelUrl = url;
    }

    @Override
    public String getJanModelUrl() {
        return janModelUrl;
    }

    @Override
    public void setJanModelUrl(String url) {
        this.janModelUrl = url;
    }

    @Override
    public String getExoModelUrl() {
        return exoModelUrl;
    }

    @Override
    public void setExoModelUrl(String url) {
        this.exoModelUrl = url;
    }

    @Override
    public String getOpenAIKey() {
        return openAIKey;
    }

    @Override
    public void setOpenAIKey(String key) {
        this.openAIKey = key;
    }

    @Override
    public String getMistralKey() {
        return mistralKey;
    }

    @Override
    public void setMistralKey(String key) {
        this.mistralKey = key;
    }

    @Override
    public String getAnthropicKey() {
        return anthropicKey;
    }

    @Override
    public void setAnthropicKey(String key) {
        this.anthropicKey = key;
    }

    @Override
    public String getGroqKey() {
        return groqKey;
    }

    @Override
    public void setGroqKey(String key) {
        this.groqKey = key;
    }

    @Override
    public String getDeepInfraKey() {
        return deepInfraKey;
    }

    @Override
    public void setDeepInfraKey(String key) {
        this.deepInfraKey = key;
    }

    @Override
    public String getGeminiKey() {
        return geminiKey;
    }

    @Override
    public void setGeminiKey(String key) {
        this.geminiKey = key;
    }

    @Override
    public Boolean getHideSearchButtonsFlag() {
        return hideSearchButtonsFlag;
    }

    @Override
    public void setHideSearchButtonsFlag(Boolean flag) {
        this.hideSearchButtonsFlag = flag;
    }

    @Override
    public String getGoogleSearchKey() {
        return googleSearchKey;
    }

    @Override
    public void setGoogleSearchKey(String key) {
        this.googleSearchKey = key;
    }

    @Override
    public String getGoogleCSIKey() {
        return googleCSIKey;
    }

    @Override
    public void setGoogleCSIKey(String key) {
        this.googleCSIKey = key;
    }

    @Override
    public String getTavilySearchKey() {
        return tavilySearchKey;
    }

    @Override
    public void setTavilySearchKey(String key) {
        this.tavilySearchKey = key;
    }

    @Override
    public Integer getMaxSearchResults() {
        return maxSearchResults;
    }

    @Override
    public void setMaxSearchResults(Integer results) {
        this.maxSearchResults = results;
    }

    public void setSelectedLanguageModel(String projectLocation, String selectedLanguageModel) {
        if (lastSelectedLanguageModel == null) {
            lastSelectedLanguageModel = new HashMap<>();
        }
        lastSelectedLanguageModel.put(projectLocation, selectedLanguageModel);
    }

    public String getSelectedLanguageModel(String projectLocation) {
        if (lastSelectedLanguageModel != null) {
            return lastSelectedLanguageModel.getOrDefault(projectLocation, "");
        } else {
            return "";
        }
    }

    public void setSelectedProvider(String projectLocation, String selectedProvider) {
        if (lastSelectedProvider == null) {
            lastSelectedProvider = new HashMap<>();
        }
        lastSelectedProvider.put(projectLocation, selectedProvider);
    }

    public String getSelectedProvider(String projectLocation) {
        if (lastSelectedProvider != null) {
            return lastSelectedProvider.getOrDefault(projectLocation, ModelProvider.Ollama.getName());
        } else {
            return ModelProvider.Ollama.getName();
        }
    }

    @Override
    public Boolean getStreamMode() {
        return streamMode;
    }

    @Override
    public void setStreamMode(Boolean mode) {
        this.streamMode = mode;
    }

    @Override
    public Double getTemperature() {
        return temperature;
    }

    @Override
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    @Override
    public Double getTopP() {
        return topP;
    }

    @Override
    public void setTopP(Double topP) {
        this.topP = topP;
    }

    @Override
    public Integer getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    @Override
    public Integer getMaxRetries() {
        return maxRetries;
    }

    @Override
    public void setMaxRetries(Integer retries) {
        this.maxRetries = retries;
    }

    @Override
    public Integer getChatMemorySize() {
        return chatMemorySize;
    }

    @Override
    public void setChatMemorySize(Integer size) {
        this.chatMemorySize = size;
    }

    @Override
    public Integer getMaxOutputTokens() {
        return maxOutputTokens;
    }

    @Override
    public void setMaxOutputTokens(Integer tokens) {
        this.maxOutputTokens = tokens;
    }

    @Override
    public Boolean getAstMode() {
        return astMode;
    }

    @Override
    public void setAstMode(Boolean mode) {
        this.astMode = mode;
    }

    @Override
    public Boolean getAstParentClass() {
        return astParentClass;
    }

    @Override
    public void setAstParentClass(Boolean parentClass) {
        this.astParentClass = parentClass;
    }

    @Override
    public Boolean getAstClassReference() {
        return astClassReference;
    }

    @Override
    public void setAstClassReference(Boolean classReference) {
        this.astClassReference = classReference;
    }

    @Override
    public Boolean getAstFieldReference() {
        return astFieldReference;
    }

    @Override
    public void setAstFieldReference(Boolean fieldReference) {
        this.astFieldReference = fieldReference;
    }

    @Override
    public String getSystemPrompt() {
        return systemPrompt;
    }

    @Override
    public void setSystemPrompt(String prompt) {
        this.systemPrompt = prompt;
    }

    @Override
    public String getTestPrompt() {
        return testPrompt;
    }

    @Override
    public void setTestPrompt(String prompt) {
        this.testPrompt = prompt;
    }

    @Override
    public String getReviewPrompt() {
        return reviewPrompt;
    }

    @Override
    public void setReviewPrompt(String prompt) {
        this.reviewPrompt = prompt;
    }

    @Override
    public String getExplainPrompt() {
        return explainPrompt;
    }

    @Override
    public void setExplainPrompt(String prompt) {
        this.explainPrompt = prompt;
    }

    @Override
    public Boolean getExcludeJavaDoc() {
        return excludeJavaDoc;
    }

    @Override
    public void setExcludeJavaDoc(Boolean exclude) {
        this.excludeJavaDoc = exclude;
    }

    @Override
    public List<String> getExcludedDirectories() {
        return excludedDirectories;
    }

    @Override
    public void setExcludedDirectories(List<String> directories) {
        this.excludedDirectories = directories;
    }

    @Override
    public List<String> getIncludedFileExtensions() {
        return includedFileExtensions;
    }

    @Override
    public void setIncludedFileExtensions(List<String> extensions) {
        this.includedFileExtensions = extensions;
    }

    @Override
    public Integer getDefaultWindowContext() {
        return defaultWindowContext;
    }

    @Override
    public void setDefaultWindowContext(Integer context) {
        this.defaultWindowContext = context;
    }

    @Override
    public void setModelCost(ModelProvider provider, String modelName, double inputCost, double outputCost) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setModelCost'");
    }

    @Override
    public void setModelWindowContext(ModelProvider provider, String modelName, int windowContext) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setModelWindowContext'");
    }

    @Override
    public double getModelInputCost(ModelProvider provider, String modelName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getModelInputCost'");
    }

    @Override
    public String getLlamaCPPUrl() {
        return llamaCPPUrl;
    }

    @Override
    public void setLlamaCPPUrl(String text) {
        this.llamaCPPUrl = text;
    }
}