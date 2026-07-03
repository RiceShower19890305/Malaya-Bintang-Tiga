package com.weather.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps OpenWeatherMap icon IDs to emoji and descriptions.
 */
public class WeatherIconMapper {
    private static final Map<String, String> EMOJI_MAP = new HashMap<>();
    private static final Map<String, String> DESCRIPTION_MAP = new HashMap<>();

    static {
        EMOJI_MAP.put("01d", "☀️");
        DESCRIPTION_MAP.put("01d", "Clear Sky (Day)");

        EMOJI_MAP.put("01n", "🌙");
        DESCRIPTION_MAP.put("01n", "Clear Sky (Night)");

        EMOJI_MAP.put("02d", "⛅");
        DESCRIPTION_MAP.put("02d", "Few Clouds (Day)");
        EMOJI_MAP.put("02n", "⛅");
        DESCRIPTION_MAP.put("02n", "Few Clouds (Night)");

        EMOJI_MAP.put("03d", "☁️");
        DESCRIPTION_MAP.put("03d", "Scattered Clouds (Day)");
        EMOJI_MAP.put("03n", "☁️");
        DESCRIPTION_MAP.put("03n", "Scattered Clouds (Night)");

        EMOJI_MAP.put("04d", "☁️☁️");
        DESCRIPTION_MAP.put("04d", "Broken Clouds (Day)");
        EMOJI_MAP.put("04n", "☁️☁️");
        DESCRIPTION_MAP.put("04n", "Broken Clouds (Night)");

        EMOJI_MAP.put("09d", "🌧️");
        DESCRIPTION_MAP.put("09d", "Shower Rain (Day)");
        EMOJI_MAP.put("09n", "🌧️");
        DESCRIPTION_MAP.put("09n", "Shower Rain (Night)");

        EMOJI_MAP.put("10d", "🌦️");
        DESCRIPTION_MAP.put("10d", "Rain (Day)");
        EMOJI_MAP.put("10n", "🌦️");
        DESCRIPTION_MAP.put("10n", "Rain (Night)");

        EMOJI_MAP.put("11d", "⛈️");
        DESCRIPTION_MAP.put("11d", "Thunderstorm (Day)");
        EMOJI_MAP.put("11n", "⛈️");
        DESCRIPTION_MAP.put("11n", "Thunderstorm (Night)");

        EMOJI_MAP.put("13d", "❄️");
        DESCRIPTION_MAP.put("13d", "Snow (Day)");
        EMOJI_MAP.put("13n", "❄️");
        DESCRIPTION_MAP.put("13n", "Snow (Night)");

        EMOJI_MAP.put("50d", "🌫️");
        DESCRIPTION_MAP.put("50d", "Mist (Day)");
        EMOJI_MAP.put("50n", "🌫️");
        DESCRIPTION_MAP.put("50n", "Mist (Night)");
    }

    public static String getEmoji(String iconId) {
        return EMOJI_MAP.getOrDefault(iconId, "🌤️");
    }

    public static String getDescription(String iconId) {
        return DESCRIPTION_MAP.getOrDefault(iconId, "Unknown");
    }
}
