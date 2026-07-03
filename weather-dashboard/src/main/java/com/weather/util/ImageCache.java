package com.weather.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Image cache for weather icons and graphics.
 */
public class ImageCache {
    private static final Map<String, BufferedImage> cache = new HashMap<>();
    private static final String CACHE_DIR = System.getProperty("user.home") + "/.weather-dashboard/images";

    static {
        new File(CACHE_DIR).mkdirs();
    }

    public static BufferedImage getImage(String url) throws IOException {
        if (cache.containsKey(url)) {
            return cache.get(url);
        }

        String filename = url.hashCode() + ".png";
        File cachedFile = new File(CACHE_DIR, filename);

        BufferedImage image;
        if (cachedFile.exists()) {
            image = ImageIO.read(cachedFile);
        } else {
            image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            ImageIO.write(image, "png", cachedFile);
        }

        cache.put(url, image);
        return image;
    }

    public static void clearCache() {
        cache.clear();
    }
}
