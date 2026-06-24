package com.tanmedia.mbt.core;

import com.tanmedia.mbt.di.ServiceRegistry;
import com.tanmedia.mbt.assets.ImageService;
import com.tanmedia.mbt.audio.AudioService;
import com.tanmedia.mbt.achievements.AchievementService;
import com.tanmedia.mbt.settings.SettingsService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Small engine that wires services and provides a simple UI for the production demo.
 */
public class GameEngine {
    private final GameState state = new GameState();
    private final ImageService images = ServiceRegistry.images();
    private final AudioService audio = ServiceRegistry.audio();
    private final AchievementService achievements = ServiceRegistry.achievements();
    private final SettingsService settings = ServiceRegistry.settings();

    private JFrame frame;
    private JLabel monthLabel;
    private JLabel scoreLabel;
    private JLabel imageLabel;
    private JTextArea achievementsArea;

    public GameEngine() {
        // safe init: services may be null if not registered
        try {
            if (images != null) images.loadAll("assets/images");
        } catch (Exception e) {
            System.err.println("Image load failed: " + e.getMessage());
        }
        try {
            if (audio != null) audio.init();
            if (audio != null && settings != null) audio.setVolume(settings.getMasterVolume());
        } catch (Exception e) {
            System.err.println("Audio init failed: " + e.getMessage());
        }
        if (achievements != null) achievements.initialize();
    }

    public void startUI() {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Malaya Bintang Tiga - Production Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLayout(new BorderLayout());

            JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
            monthLabel = new JLabel("Months: 0");
            scoreLabel = new JLabel("Score: 0");
            top.add(monthLabel);
            top.add(new JLabel("   "));
            top.add(scoreLabel);

            JPanel center = new JPanel(new BorderLayout());
            imageLabel = new JLabel("[No image loaded]", SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(600, 360));
            center.add(imageLabel, BorderLayout.CENTER);

            JPanel right = new JPanel(new BorderLayout());
            achievementsArea = new JTextArea(10, 20);
            achievementsArea.setEditable(false);
            right.add(new JLabel("Achievements"), BorderLayout.NORTH);
            right.add(new JScrollPane(achievementsArea), BorderLayout.CENTER);

            JPanel controls = new JPanel(new FlowLayout());
            JButton advance = new JButton("Advance Month");
            advance.addActionListener((ActionEvent e) -> {
                state.advanceMonth();
                if (achievements != null) achievements.checkAll(state);
                updateUI();
            });
            JButton showImage = new JButton("Show sample image (malan_command_post)");
            showImage.addActionListener(ae -> {
                if (images != null) images.displayInLabel("malan_command_post", imageLabel);
                else imageLabel.setText("[No ImageService]");
            });
            JButton playSfx = new JButton("Play Click SFX");
            playSfx.addActionListener(ae -> {
                try {
                    if (audio != null) audio.playSfx("assets/audio/click.wav");
                } catch (Exception ex) { System.err.println("SFX play error: " + ex.getMessage()); }
            });
            controls.add(advance);
            controls.add(showImage);
            controls.add(playSfx);

            frame.add(top, BorderLayout.NORTH);
            frame.add(center, BorderLayout.CENTER);
            frame.add(right, BorderLayout.EAST);
            frame.add(controls, BorderLayout.SOUTH);

            updateUI();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private void updateUI() {
        monthLabel.setText("Months: " + state.getMonthCount());
        scoreLabel.setText("Score: " + state.getScore());
        // show unlocked achievements
        StringBuilder sb = new StringBuilder();
        if (achievements instanceof com.tanmedia.mbt.achievements.AchievementTracker) {
            com.tanmedia.mbt.achievements.AchievementTracker at =
                    (com.tanmedia.mbt.achievements.AchievementTracker) achievements;
            for (String id : at.getUnlocked()) {
                sb.append(id).append(" - ").append(at.getDefinitions().getOrDefault(id, "")).append("\n");
            }
        }
        achievementsArea.setText(sb.toString());
    }

    public GameState getState() { return state; }
}