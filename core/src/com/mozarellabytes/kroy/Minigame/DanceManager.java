package com.mozarellabytes.kroy.Minigame;

import java.util.List;

public class DanceManager {

    /** The tempo of the music in Beats Per Minute */
    private float tempo;

    /** The time in seconds between beats */
    private float period;

    /** The time since the last beat in seconds */
    private float time;

    /** The time since the last half-beat in seconds */
    private float halfTime;

    /** Whether an input has already been given this beat */
    private boolean doneThisBeat;

    private DanceChoreographer choreographer;


    public DanceManager(float tempo) {
        // Setup tempo
        this.tempo = tempo;
        this.period = 60/tempo;
//        System.out.println("Period: " + this.period);
        this.time = 0;
        this.halfTime = -period/2;

        // Setup dance queue
        this.choreographer = new DanceChoreographer();
    }

    /** Called once a frame to update the dance manager*/
    public void update(float delta) {
        time += delta;
        halfTime += delta;

        // Trigger every beat
        if (time >= period) {
            System.out.println("Beat: " + time);
            choreographer.nextMove();
            time = 0f;
        }

        // Trigger every off-beat
        if (halfTime >= period)
        {
            halfTime = 0f;
            doneThisBeat = false;
        }
    }

    /**
     * Returns the phase difference of the beat at the current time where 0 is directly on the beat
     * and lim x-> 1 is directly on the next beat.
     * */
    public float getPhase() {
        return time / period;
    }

    public float getBeatProxemity() {
        return 2 * Math.abs(getPhase()-.5f);
    }

    public DanceResult takeMove(DanceMove move) {
        if (move != getNearestMove()) {
            System.out.println("Wrong");
            return DanceResult.WRONG;
        }

        if (!doneThisBeat)
        {
            float proxemity = getBeatProxemity();
            float phase = getPhase();
            if (proxemity > .9f) {
                System.out.println("Great");
                doneThisBeat = true;
                return DanceResult.GREAT;
            }
            else if (proxemity > .8f) {
                System.out.println("Good");
                doneThisBeat = true;
                return DanceResult.GOOD;
            }
            else if (proxemity > .7) {
                System.out.println("Okay");
                doneThisBeat = true;
                return DanceResult.OKAY;
            }
            else if (proxemity > .5 && phase > .5f) {
                System.out.println("Early");
                doneThisBeat = true;
                return DanceResult.EARLY;
            }
            else if (proxemity > .5 && phase < .5f) {
                System.out.println("Late");
                doneThisBeat = true;
                return DanceResult.LATE;
            }
            else {
                System.out.println("Wrong");
                doneThisBeat = true;
                return DanceResult.WRONG;
            }
        }
        else
        {
            // Player doubletook a move, punish them
            System.out.println("Wrong");
            doneThisBeat = true;
            return DanceResult.WRONG;
        }
    }

    public DanceMove[] getMoveList() {
        return choreographer.getMoveList();
    }

    public DanceMove getNearestMove() {
        if (this.getPhase() < .5f) {
            return choreographer.getMoveList()[0];
        } else {
            return choreographer.getMoveList()[1];
        }
    }
}
