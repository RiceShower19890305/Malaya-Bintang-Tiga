package com.tanmedia.mbt.settings;

public interface SettingsService {
    float getMasterVolume();
    void setMasterVolume(float v);
    float getSfxVolume();
    void setSfxVolume(float v);
    String getString(String key, String def);
    void setString(String key, String value);
    void save();
    void load();
}