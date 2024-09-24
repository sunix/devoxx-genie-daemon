package com.devoxx.genie.model.dto;

import java.text.DecimalFormat;

import com.devoxx.genie.model.LanguageModel;
import com.devoxx.genie.util.WindowContextFormatterUtil;

public class LanguageModelAdapter {

    private LanguageModel languageModel;

    public LanguageModelAdapter(LanguageModel languageModel) {
        this.languageModel = languageModel;
    }

    public String getId() {
        return languageModel.getModelName();
    }

    public String getName() {
        return languageModel.getDisplayName();
    }

    public String getDescription() {
        String windowContext = WindowContextFormatterUtil.format(languageModel.getContextWindow(), "tokens");
        if (languageModel.getInputCost() > 0.0) {
            double cost = (languageModel.getInputCost() / 1_000_000) * languageModel.getContextWindow();
            return String.format("%s @ %s USD", windowContext, new DecimalFormat("#.##").format(cost));
        } else {
            return windowContext;
        }

    }

}