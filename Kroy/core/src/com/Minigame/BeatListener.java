package com.mozarellabytes.kroy.Minigame;

/**
 * Interface for classes that subscribe to that phat beat
 */
public interface BeatListener {
    void onBeat();
    void offBeat();
    void moveResult(DanceResult result);
}
