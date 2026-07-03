package com.weather.util;

import java.io.*;
import java.util.Properties;

/**
 * Localization support for multiple languages.
 */
public class LocalizationManager {
    private Properties messages = new Properties();
    private String currentLanguage = "en";

    public LocalizationManager(String language) {
        this.currentLanguage = language;
        loadLanguage(language);
    }

    private void loadLanguage(String lang) {
        try (InputStream is = getClass().getResourceAsStream("/lang/messages_" + lang + ".properties")) {
            if (is != null) {
                messages.load(is);
            } else {
                loadLanguage("en");
            }
        } catch (IOException e) {
            System.err.println("Failed to load language: " + lang);
        }
    }

    public String get(String key) {
        return messages.getProperty(key, key);
    }

    public String get(String key, String... args) {
        String template = messages.getProperty(key, key);
        for (int i = 0; i < args.length; i++) {
            template = template.replace("{" + i + "}", args[i]);
        }
        return template;
    }

    public void setLanguage(String language) {
        currentLanguage = language;
        loadLanguage(language);
    }
}
