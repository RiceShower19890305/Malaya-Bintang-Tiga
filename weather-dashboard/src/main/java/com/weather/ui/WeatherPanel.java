package com.weather.ui;

import com.weather.model.Weather;
import javax.swing.*;
import java.awt.*;

/**
 * Panel displaying current weather with detailed metrics.
 */
public class WeatherPanel extends JPanel {
    private Weather weather;
    private ThemeManager themeManager;

    public WeatherPanel(ThemeManager themeManager) {
        this.themeManager = themeManager;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Current Weather"));
        applyTheme();
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
        removeAll();
        
        if (weather == null) {
            add(new JLabel("No weather data"));
        } else {
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setOpaque(false);
            
            JLabel cityLabel = new JLabel(weather.getCity() + ", " + weather.getCountry());
            cityLabel.setFont(new Font("Arial", Font.BOLD, 28));
            cityLabel.setForeground(themeManager.getPrimaryTextColor());
            headerPanel.add(cityLabel, BorderLayout.WEST);
            
            JLabel iconLabel = new JLabel(getWeatherEmoji(weather.getIcon()));
            iconLabel.setFont(new Font("Arial", Font.PLAIN, 36));
            headerPanel.add(iconLabel, BorderLayout.EAST);
            
            add(headerPanel);
            
            JLabel descLabel = new JLabel(weather.getDescription().substring(0, 1).toUpperCase() + 
                                         weather.getDescription().substring(1));
            descLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            descLabel.setForeground(themeManager.getSecondaryTextColor());
            descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(descLabel);
            
            JLabel timeLabel = new JLabel("Updated: " + weather.getTimestamp().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            timeLabel.setForeground(themeManager.getDisabledTextColor());
            add(timeLabel);
            
            add(Box.createVerticalStrut(15));
            
            JPanel detailsPanel = new JPanel(new GridLayout(3, 4, 15, 10));
            detailsPanel.setOpaque(false);
            detailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            addWeatherDetail(detailsPanel, "🌡️ Temperature", String.format("%.1f°", weather.getTemperature()));
            addWeatherDetail(detailsPanel, "👤 Feels Like", String.format("%.1f°", weather.getFeelsLike()));
            addWeatherDetail(detailsPanel, "📊 Min / Max", String.format("%.1f° / %.1f°", weather.getTempMin(), weather.getTempMax()));
            addWeatherDetail(detailsPanel, "💧 Humidity", weather.getHumidity() + "%");
            
            addWeatherDetail(detailsPanel, "🔽 Pressure", weather.getPressure() + " hPa");
            addWeatherDetail(detailsPanel, "💨 Wind Speed", String.format("%.1f m/s", weather.getWindSpeed()));
            addWeatherDetail(detailsPanel, "🧭 Wind Direction", weather.getWindDegree() + "°");
            addWeatherDetail(detailsPanel, "☁️ Cloudiness", weather.getCloudiness() + "%");
            
            addWeatherDetail(detailsPanel, "🌅 Latitude", String.format("%.2f°N", weather.getLatitude()));
            addWeatherDetail(detailsPanel, "🌍 Longitude", String.format("%.2f°E", weather.getLongitude()));
            
            if (weather.getSunrise() != null) {
                addWeatherDetail(detailsPanel, "🌅 Sunrise", weather.getSunrise());
            }
            if (weather.getSunset() != null) {
                addWeatherDetail(detailsPanel, "🌇 Sunset", weather.getSunset());
            }
            
            add(detailsPanel);
        }
        
        revalidate();
        repaint();
    }

    private void addWeatherDetail(JPanel panel, String label, String value) {
        JPanel detailPanel = new JPanel(new BorderLayout(5, 3));
        detailPanel.setOpaque(false);
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Arial", Font.PLAIN, 11));
        labelComp.setForeground(themeManager.getSecondaryTextColor());
        
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Arial", Font.BOLD, 14));
        valueComp.setForeground(themeManager.getPrimaryTextColor());
        
        detailPanel.add(labelComp, BorderLayout.NORTH);
        detailPanel.add(valueComp, BorderLayout.SOUTH);
        
        panel.add(detailPanel);
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
        setForeground(themeManager.getPrimaryTextColor());
    }
}
