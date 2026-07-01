package com.weather.service;

import java.io.*;
import java.util.Properties;

/**
 * Configuration service for loading/saving settings.
 */
public class ConfigService {
    private static final String CONFIG_DIR = System.getProperty("user.home") + "/.weather-dashboard";
    private static final String CONFIG_FILE = CONFIG_DIR + "/config.properties";
    
    private final Properties properties = new Properties();

    public ConfigService() {
        load();
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public void set(String key, String value) {
        properties.setProperty(key, value);
    }

    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public void setInt(String key, int value) {
        set(key, String.valueOf(value));
    }

    public void load() {
        try {
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists()) {
                try (FileInputStream fis = new FileInputStream(configFile)) {
                    properties.load(fis);
                }
            } else {
                setDefaults();
                save();
            }
        } catch (IOException e) {
            System.err.println("Failed to load config: " + e.getMessage());
            setDefaults();
        }
    }

    public void save() {
        try {
            File configDir = new File(CONFIG_DIR);
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
            
            File configFile = new File(CONFIG_FILE);
            try (FileOutputStream fos = new FileOutputStream(configFile)) {
                properties.store(fos, "Weather Dashboard Configuration");
            }
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }

    private void setDefaults() {
        properties.setProperty("api.key", "");
        properties.setProperty("api.units", "metric");
        properties.setProperty("refresh.interval", "600");
        properties.setProperty("last.city", "London");
        properties.setProperty("theme", "light");
    }
}
