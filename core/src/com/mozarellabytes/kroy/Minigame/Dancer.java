package com.mozarellabytes.kroy.Minigame;

/** Class for entities that are taking part in the dance-off */
public class Dancer {
    /** The amount of health the dancer has left*/
    private int health;
    private DanceMove state;
    private float timeInState;

    /** Whether the dancer is doing a preset dance */
    private boolean jiving;

    /** How far through the jive the dancer is */
    private int jiveStep;


    public Dancer(int maxHealth) {
        this.health = maxHealth;
        this.state = DanceMove.NONE;
        this.timeInState = 0f;
        this.jiving = false;
    }

    /**
     * Returns the current health of the dancer
     * */
    public int getHealth() {
        return health;
    }

    /**
     * Deals a set amount of damage to the dancer
     * @param amount the amount of damage to deal
     * @return true if the dancer was killed, else false
     */
    public boolean damage(int amount) {
        this.health -= amount;
        return this.health <= 0;
    }

    /**
     * Gets the current state of the dancer
     * @return a Dancemove
     */
    public DanceMove getState() {
        return this.state;
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
     * Returns the amount of time that the dancer has been in its current state in seconds
     * @return time in state
     */
    public float getTimeInState() { return this.timeInState; }

    /**
     * A dancer is jiving when it is performing an automatic celebratory dance
     * @return true if the dancer is jiving, else false
     */
    public boolean isJiving() { return this.jiving; }

    /**
     * Sets the dancer to perform an automatic celebratory dance
     * @param state true if the dancer is jiving, false if they are not
     */
    public void setJiving(boolean state) {
        System.out.println("A jive has started");
        this.jiving = state;
        switch (jiveStep % 4) {
            case 0:
                this.setState(DanceMove.LEFT);
                break;
            case 1:
                this.setState(DanceMove.NONE);
                break;
            case 2:
                this.setState(DanceMove.RIGHT);
                break;
            case 3:
                this.setState(DanceMove.NONE);
                break;
        }
    }

    /**
     * Sends an update to the dancer so that if they are jiving they can change moves in time to the music
     */
    public void updateJive() {
        if (this.isJiving()) {
            jiveStep++;
            switch (jiveStep % 4) {
                case 0:
                    this.setState(DanceMove.LEFT);
                    break;
                case 1:
                    this.setState(DanceMove.NONE);
                    break;
                case 2:
                    this.setState(DanceMove.RIGHT);
                    break;
                case 3:
                    this.setState(DanceMove.NONE);
                    break;
            }
            if (jiveStep >= 8) {
                this.jiving = false;
                this.jiveStep = 0;
                this.state = DanceMove.NONE;
            }
        }
    }
}
