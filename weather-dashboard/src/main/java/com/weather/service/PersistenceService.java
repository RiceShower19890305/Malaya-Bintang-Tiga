package com.weather.service;

import java.io.*;
import java.util.Properties;

/**
 * Persistent storage for weather data (file-based).
 */
public class PersistenceService {
    private final String dataDir = System.getProperty("user.home") + "/.weather-dashboard/data";

    public PersistenceService() {
        new File(dataDir).mkdirs();
    }

    public void saveWeather(com.weather.model.Weather weather) throws IOException {
        String filename = dataDir + "/" + weather.getCity().replace(" ", "_") + "_latest.dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(weather);
        }
    }

    public com.weather.model.Weather loadWeather(String city) throws IOException, ClassNotFoundException {
        String filename = dataDir + "/" + city.replace(" ", "_") + "_latest.dat";
        File file = new File(filename);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
                return (com.weather.model.Weather) ois.readObject();
            }
        }
        return null;
    }

    public void clearOldData(int days) {
        long cutoffTime = System.currentTimeMillis() - (days * 24L * 60 * 60 * 1000);
        File dir = new File(dataDir);
        if (dir.exists() && dir.listFiles() != null) {
            for (File file : dir.listFiles()) {
                if (file.lastModified() < cutoffTime) {
                    file.delete();
                }
            }
        }
    }
}
