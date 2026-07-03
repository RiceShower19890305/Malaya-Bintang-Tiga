package com.weather.util;

import java.io.*;
import java.util.*;

/**
 * Weather data analyzer for statistics and trends.
 */
public class WeatherAnalyzer {
    private List<com.weather.model.Weather> weatherHistory = new ArrayList<>();

    public void addWeatherSnapshot(com.weather.model.Weather weather) {
        weatherHistory.add(weather);
    }

    public double getAverageTemperature() {
        if (weatherHistory.isEmpty()) return 0;
        return weatherHistory.stream()
                .mapToDouble(com.weather.model.Weather::getTemperature)
                .average()
                .orElse(0);
    }

    public double getAverageHumidity() {
        if (weatherHistory.isEmpty()) return 0;
        return weatherHistory.stream()
                .mapToDouble(com.weather.model.Weather::getHumidity)
                .average()
                .orElse(0);
    }

    public double getMaxTemperature() {
        if (weatherHistory.isEmpty()) return 0;
        return weatherHistory.stream()
                .mapToDouble(com.weather.model.Weather::getTemperature)
                .max()
                .orElse(0);
    }

    public double getMinTemperature() {
        if (weatherHistory.isEmpty()) return 0;
        return weatherHistory.stream()
                .mapToDouble(com.weather.model.Weather::getTemperature)
                .min()
                .orElse(0);
    }

    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Weather Analysis Report\n");
        sb.append("======================\n");
        sb.append(String.format("Average Temperature: %.1f°C\n", getAverageTemperature()));
        sb.append(String.format("Average Humidity: %.1f%%\n", getAverageHumidity()));
        sb.append(String.format("Max Temperature: %.1f°C\n", getMaxTemperature()));
        sb.append(String.format("Min Temperature: %.1f°C\n", getMinTemperature()));
        sb.append(String.format("Total Observations: %d\n", weatherHistory.size()));
        return sb.toString();
    }
}
