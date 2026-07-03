package com.weather.ui;

import com.weather.model.HistoricalData;
import com.weather.service.HistoryService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Panel for viewing and exporting historical weather data.
 */
public class HistoryPanel extends JPanel {
    private final HistoryService historyService;
    private final ThemeManager themeManager;
    private JLabel statsLabel;
    private JTextArea dataArea;

    public HistoryPanel(HistoryService historyService, ThemeManager themeManager) {
        this.historyService = historyService;
        this.themeManager = themeManager;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Historical Data & Trends"));
        applyTheme();
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        controlPanel.setOpaque(false);
        
        JLabel cityLabel = new JLabel("City:");
        JTextField cityField = new JTextField(15);
        
        JButton queryButton = new JButton("Query");
        JButton exportButton = new JButton("Export CSV");
        
        controlPanel.add(cityLabel);
        controlPanel.add(cityField);
        controlPanel.add(queryButton);
        controlPanel.add(exportButton);
        
        add(controlPanel, BorderLayout.NORTH);
        
        statsLabel = new JLabel("No data selected");
        statsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        add(new JScrollPane(statsLabel), BorderLayout.WEST);
        
        dataArea = new JTextArea();
        dataArea.setEditable(false);
        dataArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        add(new JScrollPane(dataArea), BorderLayout.CENTER);
        
        queryButton.addActionListener(e -> {
            String city = cityField.getText();
            if (!city.isEmpty()) {
                LocalDate from = LocalDate.now().minusDays(30);
                LocalDate to = LocalDate.now();
                displayHistory(city, from, to);
            }
        });
        
        exportButton.addActionListener(e -> {
            String city = cityField.getText();
            if (!city.isEmpty()) {
                JFileChooser fc = new JFileChooser();
                if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        LocalDate from = LocalDate.now().minusDays(30);
                        LocalDate to = LocalDate.now();
                        historyService.exportToCSV(city, from, to, fc.getSelectedFile().getAbsolutePath());
                        JOptionPane.showMessageDialog(this, "Data exported successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private void displayHistory(String city, LocalDate from, LocalDate to) {
        List<HistoricalData> data = historyService.getHistory(city, from, to);
        
        if (data.isEmpty()) {
            statsLabel.setText("No historical data available");
            dataArea.setText("");
            return;
        }
        
        double avgTemp = historyService.getAverageTemperature(city, from, to);
        HistoricalData hottestDay = historyService.getHottestDay(city, from, to);
        HistoricalData coldestDay = historyService.getColdestDay(city, from, to);
        double totalPrecip = historyService.getTotalPrecipitation(city, from, to);
        
        statsLabel.setText(String.format(
            "<html>Avg Temp: %.1f° | Hottest: %.1f° (%s) | Coldest: %.1f° (%s) | Total Rain: %.1fmm</html>",
            avgTemp,
            hottestDay != null ? hottestDay.getTempMax() : 0,
            hottestDay != null ? hottestDay.getDate() : "N/A",
            coldestDay != null ? coldestDay.getTempMin() : 0,
            coldestDay != null ? coldestDay.getDate() : "N/A",
            totalPrecip
        ));
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-12s %8s %8s %8s %8s %10s %10s\n", "Date", "Min", "Max", "Avg", "Humid", "Rain(mm)", "Wind(m/s)"));
        sb.append("=============================================================================\n");
        
        for (HistoricalData d : data) {
            sb.append(String.format("%-12s %8.1f %8.1f %8.1f %8d %10.1f %10.1f\n",
                    d.getDate(), d.getTempMin(), d.getTempMax(), d.getTempAvg(),
                    d.getHumidity(), d.getPrecipitation(), d.getWindSpeed()));
        }
        
        dataArea.setText(sb.toString());
    }

    private void applyTheme() {
        setBackground(themeManager.getBackgroundColor());
    }
}
