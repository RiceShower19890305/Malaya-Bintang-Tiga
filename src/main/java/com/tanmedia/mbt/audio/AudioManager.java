package com.tanmedia.mbt.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Minimal WAV-based audio manager. Supports background loop and SFX (WAV).
 * Note: MP3 is not supported by default; add external libraries if needed.
 */
public class AudioManager implements AudioService {
    private volatile Clip bgClip;
    private float volume = 0.8f;
    private boolean initialized = false;
    private final ExecutorService sfxPool = Executors.newCachedThreadPool();

    @Override
    public void init() throws Exception {
        initialized = true;
    }

    @Override
    public void playBackground(String file) throws Exception {
        stopBackground();
        File f = new File(file);
        if (!f.exists()) {
            System.out.println("AudioManager: background file not found: " + file);
            return;
        }
        AudioInputStream ais = AudioSystem.getAudioInputStream(f);
        bgClip = AudioSystem.getClip();
        bgClip.open(ais);
        setClipVolume(bgClip, volume);
        bgClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Override
    public void stopBackground() {
        try {
            if (bgClip != null && bgClip.isRunning()) {
                bgClip.stop();
                bgClip.close();
                bgClip = null;
            }
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public void playSfx(String file) throws Exception {
        File f = new File(file);
        if (!f.exists()) {
            System.out.println("AudioManager: sfx file not found: " + file);
            return;
        }
        sfxPool.submit(() -> {
            try (AudioInputStream ais = AudioSystem.getAudioInputStream(f)) {
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                setClipVolume(clip, volume);
                clip.start();
                Thread.sleep( Math.max(50, clip.getMicrosecondLength() / 1000) );
                clip.drain();
                clip.close();
            } catch (Exception ex) {
                System.err.println("SFX playback failed: " + ex.getMessage());
            }
        });
    }

    private void setClipVolume(Clip clip, float vol) {
        try {
            if (clip == null) return;
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (20.0 * Math.log10(Math.max(0.0001, vol)));
            gainControl.setValue(dB);
        } catch (Exception e) {
            // Some mixers may not support volume control
        }
    }

    @Override
    public void setVolume(float master) {
        this.volume = Math.max(0f, Math.min(1f, master));
        setClipVolume(bgClip, this.volume);
    }

    @Override
    public float getVolume() { return volume; }

    @Override
    public boolean isInitialized() { return initialized; }
}