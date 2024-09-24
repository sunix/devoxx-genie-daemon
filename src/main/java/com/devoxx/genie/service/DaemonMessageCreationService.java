package com.devoxx.genie.service;

import com.devoxx.genie.model.request.ChatMessageContext;
import org.jetbrains.annotations.NotNull;


/**
 * The message creation service for user and system messages.
 * Here's where also the basic prompt "engineering" is happening, including calling the AST magic.
 */
public class DaemonMessageCreationService extends AbstractMessageCreationService {
    private static final DaemonMessageCreationService INSTANCE = new DaemonMessageCreationService();

    public static DaemonMessageCreationService getInstance() {
        return INSTANCE;
    }



    /**
     * Create user prompt with context.
     * @param project    the project
     * @param userPrompt the user prompt
     * @param files      the files
     * @return the user prompt with context
     */
    // public @NotNull CompletableFuture<String> createUserPromptWithContextAsync(Project project,
    //                                                                            String userPrompt,
    //                                                                            @NotNull List<VirtualFile> files) {
    //     return CompletableFuture.supplyAsync(() -> {
    //         StringBuilder userPromptContext = new StringBuilder();
    //         FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();

    //         for (VirtualFile file : files) {
    //             ApplicationManager.getApplication().runReadAction(() -> {
    //                 if (file.getFileType().getName().equals("UNKNOWN")) {
    //                     userPromptContext.append("Filename: ").append(file.getName()).append("\n");
    //                     userPromptContext.append("Code Snippet: ").append(file.getUserData(SELECTED_TEXT_KEY)).append("\n");
    //                 } else {
    //                     Document document = fileDocumentManager.getDocument(file);
    //                     if (document != null) {
    //                         userPromptContext.append("Filename: ").append(file.getName()).append("\n");
    //                         String content = document.getText();
    //                         userPromptContext.append(content).append("\n");
    //                     } else {
    //                         NotificationUtil.sendNotification(project, "Error reading file: " + file.getName());
    //                     }
    //                 }
    //             });
    //         }

    //         userPromptContext.append(userPrompt);
    //         return userPromptContext.toString();
    //     });
    // }

    /**
     * Add AST prompt context (selected code snippet) to the chat message.
     * @param chatMessageContext the chat message context
     * @param sb                 the string builder
     */
    void addASTContext(@NotNull ChatMessageContext chatMessageContext,
                                      @NotNull StringBuilder sb) {
    //     sb.append("\n\nRelated classes:\n\n");
    //     List<VirtualFile> tempFiles = new ArrayList<>();

    //     chatMessageContext.getEditorInfo().getSelectedFiles().forEach(file ->
    //         PSIAnalyzerService.getInstance().analyze(chatMessageContext.getProject().getAdaptedInstance(Project.class), file.getAdaptedInstance(VirtualFile.class))
    //             .ifPresent(psiClasses ->
    //                 psiClasses.forEach(psiClass -> {
    //                     tempFiles.add(psiClass.getContainingFile().getVirtualFile());
    //                     sb.append(psiClass.getText()).append("\n");
    //                 })));
    //    ;
    //     chatMessageContext.getEditorInfo().getSelectedFiles().addAll( tempFiles.stream().map((file) -> new IntellijVirtualFileHandler(file)).toList());
     }



    @Override
    protected void runReadAction(Runnable runnable) {
        runnable.run();
    }

}
