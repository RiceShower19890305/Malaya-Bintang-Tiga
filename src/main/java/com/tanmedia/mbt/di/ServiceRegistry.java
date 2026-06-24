package com.tanmedia.mbt.di;

import com.tanmedia.mbt.assets.ImageService;
import com.tanmedia.mbt.audio.AudioService;
import com.tanmedia.mbt.achievements.AchievementService;
import com.tanmedia.mbt.settings.SettingsService;
import com.tanmedia.mbt.anim.AnimationService;
import com.tanmedia.mbt.access.AccessibilityService;

/**
 * Tiny service registry used to wire modules together.
 */
public final class ServiceRegistry {
    private static ImageService imageService;
    private static AudioService audioService;
    private static AchievementService achievementService;
    private static SettingsService settingsService;
    private static AnimationService animationService;
    private static AccessibilityService accessibilityService;

    private ServiceRegistry() {}

    public static void register(ImageService s) { imageService = s; }
    public static void register(AudioService s) { audioService = s; }
    public static void register(AchievementService s) { achievementService = s; }
    public static void register(SettingsService s) { settingsService = s; }
    public static void register(AnimationService s) { animationService = s; }
    public static void register(AccessibilityService s) { accessibilityService = s; }

    public static ImageService images() { return imageService; }
    public static AudioService audio() { return audioService; }
    public static AchievementService achievements() { return achievementService; }
    public static SettingsService settings() { return settingsService; }
    public static AnimationService anim() { return animationService; }
    public static AccessibilityService a11y() { return accessibilityService; }
}