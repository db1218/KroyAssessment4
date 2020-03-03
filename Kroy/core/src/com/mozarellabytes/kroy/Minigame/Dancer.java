package com.mozarellabytes.kroy.Minigame;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/** Class for entities that are taking part in the dance-off */
public class Dancer {
    /** The amount of health the dancer has left*/
    private int health;
    private DanceMove state;
    private float timeInState;

    private int stepsInJive;

    /** Whether the dancer is doing a preset dance */
    private boolean jiving;

    /** How far through the jive the dancer is */
    private int jiveStep;

    private ArrayList jiveRoutine;

    public Dancer(int maxHealth) {
        this.health = maxHealth;
        this.state = DanceMove.NONE;
        this.timeInState = 0f;
        this.jiving = false;
        this.jiveRoutine = new ArrayList();
        this.stepsInJive = 8;

        createRoutine();
    }

    private void createRoutine() {
        this.jiveRoutine.add(DanceMove.LEFT);
        this.jiveRoutine.add(DanceMove.NONE);
        this.jiveRoutine.add(DanceMove.RIGHT);
    }

    /**
     * Deals a set amount of damage to the dancer
     * @param amount the amount of damage to deal
     * @return true if the dancer was killed, else false
     */
    public boolean damage(int amount) {
        if (amount < 0) amount = 0;

        this.health -= amount;
        return this.health <= 0;
    }

    /**
     * Sets the current state of the dancer
     * @param move the Dancemove the dancer should perform
     */
    public void setState(DanceMove move) {
        this.state = move;
        this.timeInState = 0f;
    }

    /**
     * Update the dancer so that it knows how long it has been in the current position
     * @param delta the amount of time elapsed since the last update in seconds
     */
    public void addTimeInState(float delta) {
        this.timeInState += delta;
    }


    /**
     * Sets the dancer to perform an automatic celebratory dance
     */
    public void startJive() {
        this.jiving = true;
    }

    /**
     * Sends an update to the dancer so that if they are jiving they can change moves in time to the music
     */
    public void updateJive() {
        if (this.jiving) jive();
        if (this.jiveStep >= this.stepsInJive) stopJiving();
    }

    private void jive(){
        this.jiveStep++;
        int stepIndex = this.jiveStep % 3;
        DanceMove move = (DanceMove)jiveRoutine.get(stepIndex);
        this.setState(move);
    }

    private void stopJiving() {
        this.jiving = false;
        this.jiveStep = 0;
        this.state = DanceMove.NONE;
    }

    public Texture getTexture(String entity) {
        Texture ETTexture = this.state.getETTexture();
        Texture firefighterTexture = this.state.getFirefighterTexture();
        return entity.equals("ET") ? ETTexture : firefighterTexture;
    }

    /**
     * Returns the amount of time that the dancer has been in its current state in seconds
     * @return time in state
     */
    public float getTimeInState() { return this.timeInState; }

    /**
     * Gets the current state of the dancer
     * @return a Dancemove
     */
    public DanceMove getState() {
        return this.state;
    }

    /**
     * Returns the current health of the dancer
     * */
    public int getHealth() {
        return this.health;
    }



}
