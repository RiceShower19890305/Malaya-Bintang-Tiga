package com.tanmedia.mbt.achievements;

import com.tanmedia.mbt.core.GameState;
import java.util.*;

/**
 * Very small achievement tracker for demonstration.
 */
public class AchievementTracker implements AchievementService {
    private final Set<String> unlocked = new HashSet<>();
    private final Map<String, String> definitions = new LinkedHashMap<>();

    public AchievementTracker() {
        // simple sample achievements
        definitions.put("survive_12_months", "Survive for 12 months");
        definitions.put("reach_score_100", "Reach score 100");
    }

    @Override
    public void initialize() {
        // nothing for now
    }

    @Override
    public void checkAll(GameState state) {
        if (state == null) return;
        if (!isUnlocked("survive_12_months") && state.getMonthCount() >= 12) {
            unlock("survive_12_months");
            System.out.println("Achievement unlocked: survive_12_months");
        }
        if (!isUnlocked("reach_score_100") && state.getScore() >= 100) {
            unlock("reach_score_100");
            System.out.println("Achievement unlocked: reach_score_100");
        }
    }

    @Override
    public void unlock(String id) {
        if (id == null) return;
        unlocked.add(id);
    }

    @Override
    public boolean isUnlocked(String id) {
        return id != null && unlocked.contains(id);
    }

    @Override
    public void reset() {
        unlocked.clear();
    }

    public Set<String> getUnlocked() { return Collections.unmodifiableSet(unlocked); }
    public Map<String, String> getDefinitions() { return Collections.unmodifiableMap(definitions); }
}