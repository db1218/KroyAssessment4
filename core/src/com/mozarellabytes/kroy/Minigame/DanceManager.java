package com.mozarellabytes.kroy.Minigame;

import java.util.ArrayList;
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
    /** Whether the player missed the last move */
    private boolean missedLastTurn = false;

    /** List of classes to notify about the beat */
    private List<BeatListener> beatListeners;

    private int combo = 0;

    private DanceChoreographer choreographer;

    public DanceScorer scorer;


    public DanceManager(float tempo) {
        // Setup tempo
        this.tempo = tempo;
        this.period = 60/tempo;
//        System.out.println("Period: " + this.period);
        this.time = 0;
        this.halfTime = -period/2;

        // Setup dance queue
        this.choreographer = new DanceChoreographer();
        this.scorer = new DanceScorer();
        this.beatListeners = new ArrayList<>();
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
            notifyOnBeat();
        }

        // Trigger every off-beat
        if (halfTime >= period)
        {
            halfTime = 0f;
            if (!doneThisBeat && getNearestMove() != DanceMove.NONE && getNearestMove() != DanceMove.WAIT) {
                // Player missed a turn
                killCombo();
                missedLastTurn = true;
            }
            doneThisBeat = false;
            notifyOffBeat();
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
        missedLastTurn = false;

        if (move != getNearestMove()) {
            wrongMove();
            notifyResult(DanceResult.WRONG);
            return DanceResult.WRONG;
        }

        if (!doneThisBeat)
        {
            float proxemity = getBeatProxemity();
            float phase = getPhase();
            if (proxemity > .95f) {
                doneThisBeat = true;
                goodMove();
                notifyResult(DanceResult.GREAT);
                return DanceResult.GREAT;
            }
            else if (proxemity > .9f) {
                doneThisBeat = true;
                goodMove();
                notifyResult(DanceResult.GOOD);
                return DanceResult.GOOD;
            }
            else if (proxemity > .8) {
                doneThisBeat = true;
                goodMove();
                notifyResult(DanceResult.OKAY);
                return DanceResult.OKAY;
            }
            else if (proxemity > .5 && phase > .5f) {
                doneThisBeat = true;
                killCombo();
                notifyResult(DanceResult.EARLY);
                return DanceResult.EARLY;
            }
            else if (proxemity > .5 && phase < .5f) {
                doneThisBeat = true;
                killCombo();
                notifyResult(DanceResult.LATE);
                return DanceResult.LATE;
            }
            else {
                doneThisBeat = true;
                wrongMove();
                notifyResult(DanceResult.WRONG);
                return DanceResult.WRONG;
            }
        }
        else
        {
            // Player doubletook a move, punish them
            doneThisBeat = true;
            wrongMove();
            notifyResult(DanceResult.WRONG);
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

    public boolean hasMissedLastBeat() {
        return missedLastTurn;
    };

    public void wrongMove() {
        killCombo();
        choreographer.clearQueue();
    }

    public void goodMove() {
        combo++;
    }

    public int getCombo() {
        return this.combo;
    }

    public void killCombo() {
        combo = 0;
    }

    public void subscribeToBeat(BeatListener listener) {
        beatListeners.add(listener);
    }

    public void notifyOnBeat() {
        for (BeatListener b : beatListeners) {
            b.onBeat();
        }
    }

    public void notifyOffBeat() {
        for (BeatListener b : beatListeners) {
            b.offBeat();
        }
    }

    public void notifyResult(DanceResult result) {
        for (BeatListener b : beatListeners) {
            b.moveResult(result);
        }
    }
}
