package com.weather.service;

import com.weather.model.Weather;
import com.weather.model.Forecast;
import java.util.List;

/**
 * Weather service interface.
 */
public interface WeatherService {
    /**
     * Get current weather for a city.
     */
    Weather getWeather(String cityName) throws Exception;

    /**
     * Get forecast for a city.
     */
    List<Forecast> getForecast(String cityName) throws Exception;

    /**
     * Clear all cached weather data.
     */
    void clearCache();
}
