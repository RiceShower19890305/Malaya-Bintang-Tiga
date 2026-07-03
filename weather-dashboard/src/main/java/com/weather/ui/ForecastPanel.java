package com.weather.ui;

import com.weather.model.Forecast;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Panel displaying hourly and daily forecast.
 */
public class ForecastPanel extends JPanel {
    private List<Forecast> forecasts;
    private ThemeManager themeManager;

    public ForecastPanel(ThemeManager themeManager) {
        this.themeManager = themeManager;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("5-Day Forecast (3-Hour Intervals)"));
        applyTheme();
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
        removeAll();
        
        if (forecasts == null || forecasts.isEmpty()) {
            add(new JLabel("No forecast data available"));
        } else {
            LocalDate currentDay = null;
            JPanel dayPanel = null;
            
            for (Forecast forecast : forecasts) {
                LocalDate forecastDay = forecast.getDateTime().toLocalDate();
                
                if (!forecastDay.equals(currentDay)) {
                    if (dayPanel != null) {
                        add(dayPanel);
                        add(Box.createVerticalStrut(8));
                    }
                    
                    currentDay = forecastDay;
                    dayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
                    dayPanel.setBackground(themeManager.getAlternateBackgroundColor());
                    
                    JLabel dayLabel = new JLabel(forecastDay.format(
                        java.time.format.DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy")));
                    dayLabel.setFont(new Font("Arial", Font.BOLD, 13));
                    dayLabel.setForeground(themeManager.getPrimaryTextColor());
                    dayPanel.add(dayLabel);
                    dayPanel.add(Box.createHorizontalStrut(15));
                }
                
                if (dayPanel != null) {
                    JPanel forecastItem = createForecastItem(forecast);
                    dayPanel.add(forecastItem);
                }
            }
            
            if (dayPanel != null) {
                add(dayPanel);
            }
        }
        
        revalidate();
        repaint();
    }

    private JPanel createForecastItem(Forecast forecast) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(themeManager.getBackgroundColor());
        panel.setBorder(BorderFactory.createLineBorder(themeManager.getBorderColor()));
        panel.setPreferredSize(new Dimension(110, 90));
        panel.setMaximumSize(new Dimension(110, 90));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        panel.setToolTipText(String.format(
            "<html>Time: %s<br>Temp: %.1f° (%.1f-%.1f)<br>Humidity: %d%%<br>Wind: %.1f m/s<br>Clouds: %d%%</html>",
            forecast.getDateTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")),
            forecast.getTemperature(), forecast.getTempMin(), forecast.getTempMax(),
            forecast.getHumidity(), forecast.getWindSpeed(), forecast.getCloudiness()
        ));
        
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        
        JLabel timeLabel = new JLabel(forecast.getDateTime().format(
            java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.setFont(new Font("Arial", Font.BOLD, 11));
        timeLabel.setForeground(themeManager.getPrimaryTextColor());
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel iconLabel = new JLabel(getWeatherEmoji(forecast.getIcon()));
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel tempLabel = new JLabel(String.format("%.1f°", forecast.getTemperature()));
        tempLabel.setFont(new Font("Arial", Font.BOLD, 13));
        tempLabel.setForeground(themeManager.getPrimaryTextColor());
        tempLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel = new JLabel("<html><center>" + forecast.getDescription() + "</center></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 9));
        descLabel.setForeground(themeManager.getSecondaryTextColor());
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        center.add(timeLabel);
        center.add(Box.createVerticalStrut(2));
        center.add(iconLabel);
        center.add(Box.createVerticalStrut(2));
        center.add(tempLabel);
        center.add(Box.createVerticalStrut(2));
        center.add(descLabel);
        
        panel.add(center, BorderLayout.CENTER);
        
        return panel;
    }

    private String getWeatherEmoji(String iconId) {
        return switch (iconId) {
            case "01d" -> "☀️";
            case "01n" -> "🌙";
            case "02d", "02n" -> "⛅";
            case "03d", "03n" -> "☁️";
            case "04d", "04n" -> "☁️☁️";
            case "09d", "09n" -> "🌧️";
            case "10d", "10n" -> "🌦️";
            case "11d", "11n" -> "⛈️";
            case "13d", "13n" -> "❄️";
            case "50d", "50n" -> "🌫️";
            default -> "🌤️";
        };
    }

    private void applyTheme() {
        setBackground(themeManager.getBackgroundColor());
    }
}
