package com.tanmedia.mbt.core;

/**
 * Minimal game state for the production demo.
 */
public class GameState {
    private int monthCount = 0;
    private int score = 0;

    public void advanceMonth() {
        monthCount++;
        // simple score growth for demo
        score += 10;
    }

    public int getMonthCount() { return monthCount; }
    public int getScore() { return score; }
    public void addScore(int s) { score += s; }
}