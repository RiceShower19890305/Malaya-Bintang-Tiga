package com.tanmedia.mbt.access;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * Accessibility helpers.
 */
public interface AccessibilityService {
    void applyHighContrast();
    void applyColorblindPalette();
    void setLargeText();
    void makeButtonAccessible(JButton b);
    void makeLabelAccessible(JLabel l);
}