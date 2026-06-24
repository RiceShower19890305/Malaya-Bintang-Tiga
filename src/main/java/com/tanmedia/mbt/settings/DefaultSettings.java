package com.tanmedia.mbt.settings;

import java.util.HashMap;
import java.util.Map;

public class DefaultSettings implements SettingsService {
    private float masterVolume = 0.8f;
    private float sfxVolume = 0.9f;
    private final Map<String, String> store = new HashMap<>();

    @Override
    public float getMasterVolume() { return masterVolume; }
    @Override
    public void setMasterVolume(float v) { masterVolume = Math.max(0f, Math.min(1f, v)); }
    @Override
    public float getSfxVolume() { return sfxVolume; }
    @Override
    public void setSfxVolume(float v) { sfxVolume = Math.max(0f, Math.min(1f, v)); }
    @Override
    public String getString(String key, String def) { return store.getOrDefault(key, def); }
    @Override
    public void setString(String key, String value) { store.put(key, value); }
    @Override
    public void save() { /* TODO: persist to file */ }
    @Override
    public void load() { /* TODO: load from file */ }
}