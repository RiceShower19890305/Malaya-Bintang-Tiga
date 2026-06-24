package com.tanmedia.mbt.audio;

public interface AudioService {
    void init() throws Exception;
    void playBackground(String file) throws Exception;
    void stopBackground();
    void playSfx(String file) throws Exception;
    void setVolume(float master); // 0.0 - 1.0
    float getVolume();
    boolean isInitialized();
}