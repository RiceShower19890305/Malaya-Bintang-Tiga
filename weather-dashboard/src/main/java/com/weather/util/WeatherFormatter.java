package com.weather.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Utility for formatting weather data displays.
 */
public class WeatherFormatter {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy");

    public static String formatTemperature(double temp, String unit) {
        if (unit.equals("metric")) {
            return String.format("%.1f°C", temp);
        } else {
            return String.format("%.1f°F", temp);
        }
    }

    public static String formatWindSpeed(double speed, String unit) {
        switch (unit) {
            case "mph":
                return String.format("%.1f mph", speed * 2.237);
            case "kmh":
                return String.format("%.1f km/h", speed * 3.6);
            case "knots":
                return String.format("%.1f kts", speed * 1.944);
            default:
                return String.format("%.1f m/s", speed);
        }
    }

    public static String formatPrecipitation(double mm, String unit) {
        if (unit.equals("inches")) {
            return String.format("%.2f in", mm / 25.4);
        }
        return String.format("%.1f mm", mm);
    }

    public static String formatDateTime(LocalDateTime dt) {
        return dt.format(DATE_TIME_FORMATTER);
    }

    public static String formatTime(LocalDateTime dt) {
        return dt.format(TIME_FORMATTER);
    }

    public static String formatDate(LocalDateTime dt) {
        return dt.format(DATE_FORMATTER);
    }

    public static String formatPressure(int pressure) {
        return String.format("%d hPa (%.2f inHg)", pressure, pressure * 0.02953);
    }

    public static String getWindDirection(int degree) {
        String[] directions = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
                "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
        return directions[(int) Math.round(degree / 22.5) % 16] + " (" + degree + "°)";
    }
}
