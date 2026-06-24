package com.tanmedia.mbt.achievements;

import com.tanmedia.mbt.core.GameState;

public interface AchievementService {
    void initialize();
    void checkAll(GameState state);
    void unlock(String id);
    boolean isUnlocked(String id);
    void reset();
}