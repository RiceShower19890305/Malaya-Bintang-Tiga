package com.weather.ui;

import java.awt.*;

/**
 * Theme manager for light/dark mode support.
 */
public class ThemeManager {
    public enum Theme {
        LIGHT, DARK
    }
    
    private Theme currentTheme = Theme.LIGHT;
    
    private static final Color LIGHT_BG = new Color(255, 255, 255);
    private static final Color LIGHT_ALT_BG = new Color(240, 245, 250);
    private static final Color LIGHT_PRIMARY_TEXT = new Color(0, 0, 0);
    private static final Color LIGHT_SECONDARY_TEXT = new Color(100, 100, 100);
    private static final Color LIGHT_DISABLED_TEXT = new Color(180, 180, 180);
    private static final Color LIGHT_BORDER = new Color(200, 200, 200);
    
    private static final Color DARK_BG = new Color(30, 30, 30);
    private static final Color DARK_ALT_BG = new Color(50, 50, 50);
    private static final Color DARK_PRIMARY_TEXT = new Color(230, 230, 230);
    private static final Color DARK_SECONDARY_TEXT = new Color(150, 150, 150);
    private static final Color DARK_DISABLED_TEXT = new Color(100, 100, 100);
    private static final Color DARK_BORDER = new Color(80, 80, 80);
    
    public void setTheme(Theme theme) {
        this.currentTheme = theme;
    }
    
    public Theme getTheme() {
        return currentTheme;
    }
    
    public Color getBackgroundColor() {
        return currentTheme == Theme.LIGHT ? LIGHT_BG : DARK_BG;
    }
    
    public Color getAlternateBackgroundColor() {
        return currentTheme == Theme.LIGHT ? LIGHT_ALT_BG : DARK_ALT_BG;
    }
    
    public Color getPrimaryTextColor() {
        return currentTheme == Theme.LIGHT ? LIGHT_PRIMARY_TEXT : DARK_PRIMARY_TEXT;
    }
    
    public Color getSecondaryTextColor() {
        return currentTheme == Theme.LIGHT ? LIGHT_SECONDARY_TEXT : DARK_SECONDARY_TEXT;
    }
    
    public Color getDisabledTextColor() {
        return currentTheme == Theme.LIGHT ? LIGHT_DISABLED_TEXT : DARK_DISABLED_TEXT;
    }
    
    public Color getBorderColor() {
        return currentTheme == Theme.LIGHT ? LIGHT_BORDER : DARK_BORDER;
    }
}
