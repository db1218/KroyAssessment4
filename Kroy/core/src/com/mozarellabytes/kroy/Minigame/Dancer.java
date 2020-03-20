package com.mozarellabytes.kroy.Minigame;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * Class for entities that are taking part in the dance-off
 */
public class Dancer {
    
    /** The amount of health the dancer has left*/
    private int health;

    /** The dance move that this dancer is currently executing */
    private DanceMove state;

    /** The time that this dancer has been in this state (see above) */
    private float timeInState;


    /** The number of steps in this dancer's celebratory dance (Jive) */
    private final int stepsInJive;

    /** Whether the dancer is doing a preset dance */
    private boolean jiving;

    /** How far through the jive the dancer is */
    private int jiveStep;

    /** ArrayList containing the list of moves that the dancer
     * executes when it is jiving */
    private final ArrayList<DanceMove> jiveRoutine;

    /**
     * Constructor for Dancer
     *
     * @param maxHealth health dancer starts with
     */
    public Dancer(int maxHealth) {
        this.health = maxHealth;
        this.state = DanceMove.NONE;
        this.timeInState = 0f;
        this.jiving = false;
        this.jiveRoutine = new ArrayList<>();
        this.stepsInJive = 8;

        createRoutine();
    }

    /**
     * Creates the jive routine, by adding moves to
     * jiveRoutine
     */
    private void createRoutine() {
        this.jiveRoutine.add(DanceMove.LEFT);
        this.jiveRoutine.add(DanceMove.NONE);
        this.jiveRoutine.add(DanceMove.RIGHT);
    }

    /**
     * Deals a set amount of damage to the dancer
     * @param amount the amount of damage to deal
     * @return <code> true </code> if the dancer was killed,
     *         <code> false </code> otherwise
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
     * This controls the dancer's jive and stops this dancer jiving
     * when it has danced a certain number of steps.
     */
    public void updateJive() {
        if (this.jiving) jive();
        if (this.jiveStep == this.stepsInJive) stopJiving();
    }

    /**
     * While this dancer is jiving it will loop through its jiveRoutine
     * executing each step
     */
    private void jive(){
        this.jiveStep++;
        int stepIndex = this.jiveStep % 3;
        DanceMove move = jiveRoutine.get(stepIndex);
        this.setState(move);
    }

    /**
     * This stops this dancer from jiving
     */
    private void stopJiving() {
        this.jiving = false;
        this.jiveStep = 0;
        this.state = DanceMove.NONE;
    }

    /**
     * Get texture of entity
     * @param entity    entity to get texture for
     * @return          the texture
     */
    public Texture getTexture(String entity) {
        Texture ETTexture = this.state.getETTexture();
        Texture firefighterTexture = this.state.getFirefighterTexture();
        return entity.equals("ET") ? ETTexture : firefighterTexture;
    }

    public float getTimeInState() { return this.timeInState; }

    public DanceMove getState() {
        return this.state;
    }

    public int getHealth() {
        return this.health;
    }

}
