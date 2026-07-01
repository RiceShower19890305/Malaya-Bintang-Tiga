package com.weather.model;

/**
 * Weather alert for temperature, precipitation, or wind extremes.
 */
public class WeatherAlert {
    private String id;
    private String city;
    private String type;
    private String condition;
    private double value;
    private double threshold;
    private String message;
    private long timestamp;
    private boolean acknowledged;

    public WeatherAlert() {}

    public WeatherAlert(String city, String type, String condition, double value, double threshold, String message) {
        this.id = java.util.UUID.randomUUID().toString();
        this.city = city;
        this.type = type;
        this.condition = condition;
        this.value = value;
        this.threshold = threshold;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
        this.acknowledged = false;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public double getThreshold() { return threshold; }
    public void setThreshold(double threshold) { this.threshold = threshold; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public boolean isAcknowledged() { return acknowledged; }
    public void setAcknowledged(boolean acknowledged) { this.acknowledged = acknowledged; }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s (%.1f vs %.1f)", type.toUpperCase(), city, message, value, threshold);
    }
}
