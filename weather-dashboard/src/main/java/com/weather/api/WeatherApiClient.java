package com.weather.api;

import com.weather.model.Weather;
import com.weather.model.Forecast;
import com.weather.model.Location;
import java.util.List;

/**
 * Interface for weather API clients.
 */
public interface WeatherApiClient {
    /**
     * Get geocoding info for a city name.
     */
    Location getLocation(String cityName) throws Exception;

    /**
     * Get current weather for a city.
     */
    Weather getCurrentWeather(String cityName) throws Exception;

    /**
     * Get current weather by coordinates.
     */
    Weather getCurrentWeatherByCoords(double latitude, double longitude) throws Exception;

    /**
     * Get 5-day forecast for a city.
     */
    List<Forecast> getForecast(String cityName) throws Exception;

    /**
     * Check if API is available.
     */
    boolean isAvailable();

    /**
     * Set the API key.
     */
    void setApiKey(String apiKey);
}
