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

    public int getHealth() {
        return health;
    }

    public boolean damage(int amount) {
        this.health -= amount;
        return this.health <= 0;
    }

    public DanceMove getState() {
        return this.state;
    }

    public void setState(DanceMove move) {
        this.state = move;
        this.timeInState = 0f;
    }

    public void addTimeInState(float delta) {
        this.timeInState += delta;
    }

    public float getTimeInState() { return this.timeInState; }

    public boolean isJiving() { return this.jiving; }

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
