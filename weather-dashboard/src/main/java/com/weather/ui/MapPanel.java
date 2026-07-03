package com.weather.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Simple visualization of weather data on a map-like grid.
 */
public class MapPanel extends JPanel {
    private java.util.List<MapWeatherPoint> weatherPoints = new java.util.ArrayList<>();
    private ThemeManager themeManager;

    public MapPanel(ThemeManager themeManager) {
        this.themeManager = themeManager;
        setBackground(themeManager.getBackgroundColor());
        setBorder(BorderFactory.createTitledBorder("Map View (Beta)"));
    }

    public void addWeatherPoint(String city, double lat, double lon, double temp, String icon) {
        weatherPoints.add(new MapWeatherPoint(city, lat, lon, temp, icon));
    }

    public void clearPoints() {
        weatherPoints.clear();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2d.setColor(themeManager.getBorderColor());
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0; i <= 18; i++) {
            int x = (int) (w / 18.0 * i);
            g2d.drawLine(x, 0, x, h);
        }
        for (int i = 0; i <= 9; i++) {
            int y = (int) (h / 9.0 * i);
            g2d.drawLine(0, y, w, y);
        }

        for (MapWeatherPoint point : weatherPoints) {
            int x = (int) ((point.lon + 180) / 360.0 * w);
            int y = (int) ((90 - point.lat) / 180.0 * h);

            Color tempColor = getTempColor(point.temp);
            g2d.setColor(tempColor);
            g2d.fillOval(x - 8, y - 8, 16, 16);

            g2d.setColor(themeManager.getPrimaryTextColor());
            g2d.drawString(point.city, x + 12, y);
        }
    }

    private Color getTempColor(double temp) {
        if (temp < 0) return new Color(0, 150, 255);
        if (temp < 10) return new Color(100, 200, 255);
        if (temp < 20) return new Color(100, 255, 150);
        if (temp < 30) return new Color(255, 200, 0);
        return new Color(255, 100, 0);
    }

    private static class MapWeatherPoint {
        String city;
        double lat, lon, temp;
        String icon;

        MapWeatherPoint(String city, double lat, double lon, double temp, String icon) {
            this.city = city;
            this.lat = lat;
            this.lon = lon;
            this.temp = temp;
            this.icon = icon;
        }
    }
}
