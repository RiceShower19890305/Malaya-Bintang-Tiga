package com.weather.service;

import com.weather.api.WeatherApiClient;
import com.weather.model.Weather;
import com.weather.model.Forecast;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Weather service with caching to reduce API calls.
 */
public class CachedWeatherService implements WeatherService {
    private final WeatherApiClient apiClient;
    private final Map<String, CachedWeather> weatherCache = new HashMap<>();
    private final Map<String, CachedForecast> forecastCache = new HashMap<>();
    private static final long CACHE_VALIDITY_MS = 10 * 60 * 1000;

    public CachedWeatherService(WeatherApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public Weather getWeather(String cityName) throws Exception {
        String key = cityName.toLowerCase();
        
        if (weatherCache.containsKey(key)) {
            CachedWeather cached = weatherCache.get(key);
            if (!cached.isExpired()) {
                return cached.weather;
            }
        }
        
        Weather weather = apiClient.getCurrentWeather(cityName);
        weatherCache.put(key, new CachedWeather(weather));
        
        return weather;
    }

    @Override
    public List<Forecast> getForecast(String cityName) throws Exception {
        String key = cityName.toLowerCase();
        
        if (forecastCache.containsKey(key)) {
            CachedForecast cached = forecastCache.get(key);
            if (!cached.isExpired()) {
                return cached.forecasts;
            }
        }
        
        List<Forecast> forecasts = apiClient.getForecast(cityName);
        forecastCache.put(key, new CachedForecast(forecasts));
        
        return forecasts;
    }

    @Override
    public void clearCache() {
        weatherCache.clear();
        forecastCache.clear();
    }

    private static class CachedWeather {
        private final Weather weather;
        private final long timestamp;

        CachedWeather(Weather weather) {
            this.weather = weather;
            this.timestamp = System.currentTimeMillis();
        }

        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_VALIDITY_MS;
        }
    }

    private static class CachedForecast {
        private final List<Forecast> forecasts;
        private final long timestamp;

        CachedForecast(List<Forecast> forecasts) {
            this.forecasts = forecasts;
            this.timestamp = System.currentTimeMillis();
        }

        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_VALIDITY_MS;
        }
    }
}
