package com.weather.model;

import java.time.LocalDate;

/**
 * Historical weather data point.
 */
public class HistoricalData {
    private String city;
    private LocalDate date;
    private double tempMin;
    private double tempMax;
    private double tempAvg;
    private int humidity;
    private double precipitation;
    private double windSpeed;
    private String description;
    private int cloudiness;

    public HistoricalData() {}

    public HistoricalData(String city, LocalDate date) {
        this.city = city;
        this.date = date;
    }

    // Getters & Setters
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public double getTempMin() { return tempMin; }
    public void setTempMin(double tempMin) { this.tempMin = tempMin; }

    public double getTempMax() { return tempMax; }
    public void setTempMax(double tempMax) { this.tempMax = tempMax; }

    public double getTempAvg() { return tempAvg; }
    public void setTempAvg(double tempAvg) { this.tempAvg = tempAvg; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public double getPrecipitation() { return precipitation; }
    public void setPrecipitation(double precipitation) { this.precipitation = precipitation; }

    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCloudiness() { return cloudiness; }
    public void setCloudiness(int cloudiness) { this.cloudiness = cloudiness; }

    @Override
    public String toString() {
        return String.format("%s (%s): %.1f-%.1f°C, %.1fmm rain", city, date, tempMin, tempMax, precipitation);
    }
}
