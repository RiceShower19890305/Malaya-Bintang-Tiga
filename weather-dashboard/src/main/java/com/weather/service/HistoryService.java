package com.weather.service;

import com.weather.model.HistoricalData;
import java.time.LocalDate;
import java.util.*;
import java.io.*;

/**
 * Service for storing and retrieving historical weather data.
 */
public class HistoryService {
    private final Map<String, List<HistoricalData>> history = new HashMap<>();
    private final String historyDir = System.getProperty("user.home") + "/.weather-dashboard/history";

    public HistoryService() {
        new File(historyDir).mkdirs();
    }

    public void recordData(HistoricalData data) {
        String key = data.getCity().toLowerCase();
        history.computeIfAbsent(key, k -> new ArrayList<>()).add(data);
    }

    public List<HistoricalData> getHistory(String city, LocalDate from, LocalDate to) {
        String key = city.toLowerCase();
        return history.getOrDefault(key, new ArrayList<>()).stream()
                .filter(d -> !d.getDate().isBefore(from) && !d.getDate().isAfter(to))
                .sorted(Comparator.comparing(HistoricalData::getDate))
                .toList();
    }

    public void exportToCSV(String city, LocalDate from, LocalDate to, String filepath) throws IOException {
        List<HistoricalData> data = getHistory(city, from, to);
        try (PrintWriter writer = new PrintWriter(new FileWriter(filepath))) {
            writer.println("Date,TempMin,TempMax,TempAvg,Humidity,Precipitation,WindSpeed,Description,Cloudiness");
            for (HistoricalData d : data) {
                writer.printf("%s,%.1f,%.1f,%.1f,%d,%.1f,%.1f,%s,%d%n",
                        d.getDate(), d.getTempMin(), d.getTempMax(), d.getTempAvg(),
                        d.getHumidity(), d.getPrecipitation(), d.getWindSpeed(),
                        d.getDescription(), d.getCloudiness());
            }
        }
    }

    public double getAverageTemperature(String city, LocalDate from, LocalDate to) {
        List<HistoricalData> data = getHistory(city, from, to);
        return data.stream().mapToDouble(HistoricalData::getTempAvg).average().orElse(0.0);
    }

    public HistoricalData getHottestDay(String city, LocalDate from, LocalDate to) {
        return getHistory(city, from, to).stream()
                .max(Comparator.comparingDouble(HistoricalData::getTempMax))
                .orElse(null);
    }

    public HistoricalData getColdestDay(String city, LocalDate from, LocalDate to) {
        return getHistory(city, from, to).stream()
                .min(Comparator.comparingDouble(HistoricalData::getTempMin))
                .orElse(null);
    }

    public double getTotalPrecipitation(String city, LocalDate from, LocalDate to) {
        return getHistory(city, from, to).stream()
                .mapToDouble(HistoricalData::getPrecipitation)
                .sum();
    }
}
