package com.weather.service;

import com.weather.model.WeatherAlert;
import java.util.*;

/**
 * Service for managing weather alerts.
 */
public class AlertService {
    private final List<WeatherAlert> alerts = new ArrayList<>();
    private final ConfigService configService;
    private double tempMinThreshold = -40;
    private double tempMaxThreshold = 50;
    private double precipitationThreshold = 25;
    private double windSpeedThreshold = 10;
    private boolean enabled = true;

    public AlertService(ConfigService configService) {
        this.configService = configService;
        loadThresholds();
    }

    public void addAlert(WeatherAlert alert) {
        alerts.add(alert);
        System.out.println("Weather Alert: " + alert.getMessage());
    }

    public void checkTemperature(String city, double temp) {
        if (!enabled) return;
        
        if (temp < tempMinThreshold) {
            addAlert(new WeatherAlert(city, "temperature", "low", temp, tempMinThreshold,
                    String.format("Temperature critically low: %.1f°C", temp)));
        } else if (temp > tempMaxThreshold) {
            addAlert(new WeatherAlert(city, "temperature", "high", temp, tempMaxThreshold,
                    String.format("Temperature extremely high: %.1f°C", temp)));
        }
    }

    public void checkPrecipitation(String city, double precipitation) {
        if (!enabled || precipitation == 0) return;
        
        if (precipitation > precipitationThreshold) {
            addAlert(new WeatherAlert(city, "precipitation", "high", precipitation, precipitationThreshold,
                    String.format("Heavy rain alert: %.1fmm expected", precipitation)));
        }
    }

    public void checkWindSpeed(String city, double windSpeed) {
        if (!enabled) return;
        
        if (windSpeed > windSpeedThreshold) {
            addAlert(new WeatherAlert(city, "wind", "high", windSpeed, windSpeedThreshold,
                    String.format("Strong wind alert: %.1f m/s", windSpeed)));
        }
    }

    public List<WeatherAlert> getUnacknowledgedAlerts() {
        return alerts.stream()
                .filter(a -> !a.isAcknowledged())
                .toList();
    }

    public List<WeatherAlert> getAllAlerts() {
        return new ArrayList<>(alerts);
    }

    public void acknowledgeAlert(String alertId) {
        alerts.stream()
                .filter(a -> a.getId().equals(alertId))
                .forEach(a -> a.setAcknowledged(true));
    }

    public void clearAlerts() {
        alerts.clear();
    }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isEnabled() { return enabled; }

    public void setTempMinThreshold(double temp) { this.tempMinThreshold = temp; }
    public void setTempMaxThreshold(double temp) { this.tempMaxThreshold = temp; }
    public void setPrecipitationThreshold(double mm) { this.precipitationThreshold = mm; }
    public void setWindSpeedThreshold(double ms) { this.windSpeedThreshold = ms; }

    private void loadThresholds() {
        tempMinThreshold = Integer.parseInt(configService.get("alerts.temperature_min", "-40"));
        tempMaxThreshold = Integer.parseInt(configService.get("alerts.temperature_max", "50"));
        precipitationThreshold = Double.parseDouble(configService.get("alerts.precipitation_threshold", "25"));
        enabled = Boolean.parseBoolean(configService.get("alerts.enabled", "true"));
    }

    public void saveThresholds() {
        configService.set("alerts.temperature_min", String.valueOf((int) tempMinThreshold));
        configService.set("alerts.temperature_max", String.valueOf((int) tempMaxThreshold));
        configService.set("alerts.precipitation_threshold", String.valueOf(precipitationThreshold));
        configService.set("alerts.enabled", String.valueOf(enabled));
        configService.save();
    }
}
