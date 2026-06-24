package com.tanmedia.mbt.assets;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Simple image manager that loads images from a folder (assets/images).
 * Accepts jpg, jpeg, png.
 */
public class ImageAssetManager implements ImageService {
    private final Map<String, BufferedImage> imageCache = new HashMap<>();
    private boolean loaded = false;

    @Override
    public void loadAll(String folderPath) throws IOException {
        imageCache.clear();
        File dir = new File(folderPath);
        if (!dir.exists() || !dir.isDirectory()) {
            loaded = false;
            System.out.println("ImageAssetManager: folder not found: " + folderPath);
            return;
        }
        File[] files = dir.listFiles((d, n) -> {
            String l = n.toLowerCase();
            return l.endsWith(".png") || l.endsWith(".jpg") || l.endsWith(".jpeg");
        });
        if (files == null) {
            loaded = false;
            return;
        }
        for (File f : files) {
            try {
                BufferedImage img = ImageIO.read(f);
                if (img != null) {
                    String id = stripExt(f.getName());
                    imageCache.put(id.toLowerCase(), img);
                }
            } catch (IOException ex) {
                System.err.println("Failed to load image " + f.getName() + ": " + ex.getMessage());
            }
        }
        loaded = true;
        System.out.println("ImageAssetManager: loaded " + imageCache.size() + " images from " + folderPath);
    }

    private String stripExt(String name) {
        int dot = name.lastIndexOf('.');
        return (dot >= 0) ? name.substring(0, dot) : name;
    }

    @Override
    public BufferedImage get(String id) {
        if (id == null) return null;
        return imageCache.get(id.toLowerCase());
    }

    @Override
    public BufferedImage getScaled(String id, int maxWidth, int maxHeight) {
        BufferedImage orig = get(id);
        if (orig == null) return null;
        if (orig.getWidth() <= maxWidth && orig.getHeight() <= maxHeight) return orig;
        double wr = (double) maxWidth / orig.getWidth();
        double hr = (double) maxHeight / orig.getHeight();
        double r = Math.min(wr, hr);
        int w = Math.max(1, (int) (orig.getWidth() * r));
        int h = Math.max(1, (int) (orig.getHeight() * r));
        Image scaled = orig.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.drawImage(scaled, 0, 0, null);
        g.dispose();
        return out;
    }

    @Override
    public boolean has(String id) {
        return id != null && imageCache.containsKey(id.toLowerCase());
    }

    @Override
    public Set<String> listIds() {
        return new HashSet<>(imageCache.keySet());
    }

    @Override
    public void displayInLabel(String id, JLabel target) {
        if (target == null) return;
        BufferedImage img = getScaled(id, 600, 360);
        if (img != null) {
            target.setIcon(new ImageIcon(img));
            target.setText("");
        } else {
            target.setIcon(null);
            target.setText("[No image: " + id + "]");
        }
    }
}