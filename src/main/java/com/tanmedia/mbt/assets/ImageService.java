package com.tanmedia.mbt.assets;

import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import java.io.IOException;
import java.util.Set;

public interface ImageService {
    void loadAll(String folderPath) throws IOException;
    BufferedImage get(String id);
    BufferedImage getScaled(String id, int maxWidth, int maxHeight);
    boolean has(String id);
    Set<String> listIds();
    void displayInLabel(String id, JLabel target);
}