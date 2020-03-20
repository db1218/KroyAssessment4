package com.mozarellabytes.kroy.Minigame;

/**
 * Interface for classes that subscribe to the beat used in the minigame
 */
public interface BeatListener {
    void onBeat();
    void offBeat();
    void moveResult(DanceResult result);
}
