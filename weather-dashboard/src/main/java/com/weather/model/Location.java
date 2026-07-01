package com.weather.model;

/**
 * Represents a geographic location.
 */
public class Location {
    private String city;
    private String country;
    private double latitude;
    private double longitude;

    public Location() {}

    public Location(String city, String country, double latitude, double longitude) {
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    @Override
    public String toString() {
        return String.format("%s, %s (%.2f°N, %.2f°E)", city, country, latitude, longitude);
    }
}
