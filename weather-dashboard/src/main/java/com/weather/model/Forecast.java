package com.weather.model;

import java.time.LocalDateTime;

/**
 * Represents a forecast entry (3-hour or daily interval).
 */
public class Forecast {
    private LocalDateTime dateTime;
    private double temperature;
    private double tempMin;
    private double tempMax;
    private int humidity;
    private double windSpeed;
    private int cloudiness;
    private String description;
    private String icon;
    private double precipitation;
    private int pressure;

    public Forecast() {}

    public Forecast(LocalDateTime dateTime, double temperature, String description, String icon) {
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.description = description;
        this.icon = icon;
    }

    // Getters & Setters
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public double getTempMin() { return tempMin; }
    public void setTempMin(double tempMin) { this.tempMin = tempMin; }

    public double getTempMax() { return tempMax; }
    public void setTempMax(double tempMax) { this.tempMax = tempMax; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }

    public int getCloudiness() { return cloudiness; }
    public void setCloudiness(int cloudiness) { this.cloudiness = cloudiness; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public double getPrecipitation() { return precipitation; }
    public void setPrecipitation(double precipitation) { this.precipitation = precipitation; }

    public int getPressure() { return pressure; }
    public void setPressure(int pressure) { this.pressure = pressure; }

    @Override
    public String toString() {
        return String.format("%s: %.1f°C, %s", dateTime, temperature, description);
    }
}
