package com.weather.ui;

import com.weather.model.WeatherAlert;
import com.weather.service.AlertService;
import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

/**
 * Panel displaying weather alerts.
 */
public class AlertsPanel extends JPanel {
    private final AlertService alertService;
    private final ThemeManager themeManager;
    private DefaultListModel<String> alertsModel;
    private JList<String> alertsList;

    public AlertsPanel(AlertService alertService, ThemeManager themeManager) {
        this.alertService = alertService;
        this.themeManager = themeManager;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Weather Alerts"));
        applyTheme();
        
        alertsModel = new DefaultListModel<>();
        alertsList = new JList<>(alertsModel);
        alertsList.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(alertsList);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttonPanel.setOpaque(false);
        
        JButton acknowledgeButton = new JButton("Acknowledge");
        acknowledgeButton.addActionListener(e -> {
            int idx = alertsList.getSelectedIndex();
            if (idx >= 0) {
                String alertId = alertService.getAllAlerts().get(idx).getId();
                alertService.acknowledgeAlert(alertId);
                refreshAlerts();
            }
        });
        buttonPanel.add(acknowledgeButton);
        
        JButton clearButton = new JButton("Clear All");
        clearButton.addActionListener(e -> {
            alertService.clearAlerts();
            refreshAlerts();
        });
        buttonPanel.add(clearButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        refreshAlerts();
    }

    public void refreshAlerts() {
        alertsModel.clear();
        List<WeatherAlert> alerts = alertService.getAllAlerts();
        for (WeatherAlert alert : alerts) {
            String status = alert.isAcknowledged() ? "[ACK]" : "[NEW]";
            String timestamp = Instant.ofEpochMilli(alert.getTimestamp())
                    .atZone(ZoneId.systemDefault())
                    .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
            alertsModel.addElement(String.format("%s %s %s", status, timestamp, alert.getMessage()));
        }
    }

    private void applyTheme() {
        setBackground(themeManager.getBackgroundColor());
    }
}
