package com.weather.service;

import java.util.*;

/**
 * Service for managing favorite cities.
 */
public class FavoritesService {
    private final Set<String> favorites = new LinkedHashSet<>();
    private final ConfigService configService;
    private static final String FAVORITES_KEY = "favorites";

    public FavoritesService(ConfigService configService) {
        this.configService = configService;
        loadFavorites();
    }

    public void addFavorite(String city) {
        favorites.add(city);
        saveFavorites();
    }

    public void removeFavorite(String city) {
        favorites.remove(city);
        saveFavorites();
    }

    public List<String> getFavorites() {
        return new ArrayList<>(favorites);
    }

    public boolean isFavorite(String city) {
        return favorites.contains(city);
    }

    public void clearFavorites() {
        favorites.clear();
        saveFavorites();
    }

    private void loadFavorites() {
        String favStr = configService.get(FAVORITES_KEY, "");
        if (!favStr.isEmpty()) {
            String[] cities = favStr.split(",");
            for (String city : cities) {
                String trimmed = city.trim();
                if (!trimmed.isEmpty()) {
                    favorites.add(trimmed);
                }
            }
        }
    }

    private void saveFavorites() {
        String favStr = String.join(",", favorites);
        configService.set(FAVORITES_KEY, favStr);
        configService.save();
    }
}
