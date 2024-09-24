package com.devoxx.genie;

import com.devoxx.genie.service.DevoxxGenieSettingsService;
import com.devoxx.genie.service.Logger;
import com.devoxx.genie.service.MessageCreationService;
import com.devoxx.genie.service.ChatMemoryService;
import com.devoxx.genie.service.DaemonChatMemoryService;
import com.devoxx.genie.service.DaemonSettingsService;
import com.devoxx.genie.service.DaemonMessageCreationService;
import com.devoxx.genie.service.NotificationService;
import com.devoxx.genie.service.ProjectManager;
import com.devoxx.genie.service.DevoxxGenieServiceProvider;

public class DevoxxGenieDaemonServiceProvider implements DevoxxGenieServiceProvider {

    @Override
    public DevoxxGenieSettingsService getDevoxxGenieSettingsService() {
        return DaemonSettingsService.getInstance();
    }

    @Override
    public NotificationService getNotificationService() {
        return new NotificationService() {

            @Override
            public void sendNotification(ProjectHandler project, String content) {
                System.out.println("Notification: " + content);
            }

        };
    }

    @Override
    public ProjectManager getProjectManager() {
        throw new UnsupportedOperationException("Unimplemented method 'getProjectManager'");
    }

    @Override
    public Logger getLogger(Class<?> clazz) {
        return new Logger() {
            org.jboss.logging.Logger logger = org.jboss.logging.Logger.getLogger(clazz.getName());

            @Override
            public void info(String message) {
                logger.info(message);
            }

            @Override
            public void warn(String message) {
                logger.warn(message);
            }

            @Override
            public void debug(String message) {
                logger.debug(message);
            }

            @Override
            public void error(String message) {
                logger.error(message);
            }

            @Override
            public void trace(String message) {
                logger.trace(message);
            }

            @Override
            public void info(String message, Throwable t) {
                logger.info(message, t);
            }

            @Override
            public void warn(String message, Throwable t) {
                logger.warn(message, t);
            }

            @Override
            public void debug(String message, Throwable t) {
                logger.debug(message, t);
            }

            @Override
            public void error(String message, Throwable t) {
                logger.error(message, t);
            }

        };
    }

    @Override
    public MessageCreationService getMessageCreationService() {
        return DaemonMessageCreationService.getInstance();
    }

    @Override
    public ChatMemoryService getChatMemoryService() {
        return DaemonChatMemoryService.getInstance();
    }

}
