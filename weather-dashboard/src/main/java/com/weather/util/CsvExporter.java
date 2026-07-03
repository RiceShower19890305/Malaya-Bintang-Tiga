package com.weather.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * CSV export utility for weather data.
 */
public class CsvExporter {
    public static void exportWeatherToCsv(java.util.List<?> dataList, String filepath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filepath))) {
            if (dataList.isEmpty()) return;

            Object first = dataList.get(0);
            if (first instanceof com.weather.model.Weather) {
                exportWeatherList((java.util.List<com.weather.model.Weather>) dataList, writer);
            } else if (first instanceof com.weather.model.Forecast) {
                exportForecastList((java.util.List<com.weather.model.Forecast>) dataList, writer);
            }
        }
    }

    private static void exportWeatherList(java.util.List<com.weather.model.Weather> list, PrintWriter writer) {
        writer.println("City,Country,Temperature,Feels Like,Min,Max,Humidity,Pressure,Wind Speed,Cloudiness,Description,Timestamp");
        for (com.weather.model.Weather w : list) {
            writer.printf("%s,%s,%.1f,%.1f,%.1f,%.1f,%d,%d,%.1f,%d,%s,%s%n",
                    w.getCity(), w.getCountry(), w.getTemperature(), w.getFeelsLike(),
                    w.getTempMin(), w.getTempMax(), w.getHumidity(), w.getPressure(),
                    w.getWindSpeed(), w.getCloudiness(), w.getDescription(), w.getTimestamp());
        }
    }

    private static void exportForecastList(java.util.List<com.weather.model.Forecast> list, PrintWriter writer) {
        writer.println("DateTime,Temperature,Min,Max,Humidity,Wind Speed,Cloudiness,Description,Precipitation");
        for (com.weather.model.Forecast f : list) {
            writer.printf("%s,%.1f,%.1f,%.1f,%d,%.1f,%d,%s,%.1f%n",
                    f.getDateTime(), f.getTemperature(), f.getTempMin(), f.getTempMax(),
                    f.getHumidity(), f.getWindSpeed(), f.getCloudiness(),
                    f.getDescription(), f.getPrecipitation());
        }
    }
}
