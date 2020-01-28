package com.mozarellabytes.kroy.Minigame;

public class DanceScorer {

    private static int ET_MAX_HEALTH = 10;
    private static int PLAYER_MAX_HEALTH = 10;
    private int etHealth = ET_MAX_HEALTH;
    private int playerHealth = PLAYER_MAX_HEALTH;

    public DanceScorer() {

    }

    public int getEtHealth() {
        return etHealth;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }
}
