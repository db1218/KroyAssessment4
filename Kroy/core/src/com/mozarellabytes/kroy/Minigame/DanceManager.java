package com.mozarellabytes.kroy.Minigame;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Utilities.CameraShake;

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

    /** The number of successive correct moves the player has performed */
    private int combo = 0;

    /** Technical class for deciding upcoming moves */
    private DanceChoreographer choreographer;



    public DanceManager(float tempo) {

        // Setup tempo
        this.tempo = tempo;
        this.period = 60/tempo;
        // System.out.println("Period: " + this.period);
        this.time = 0;
        this.halfTime = -period/2;

        // Setup dance queue
        this.choreographer = new DanceChoreographer();
        this.beatListeners = new ArrayList<>();
    }

    /** Called once a frame to update the dance manager*/
    public void update(float delta) {


        time += delta;
        halfTime += delta;

        // Trigger every beat
        if (time >= period) {
            //System.out.println("Beat: " + time);
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
     * @return the phase through the next beat
     * */
    public float getPhase() {
        return time / period;
    }

    /**
     * Gets the distance to the nearest beat where .5f is equidistant between two beats and 0f is directly on the beat
     * @return float distance to nearest beat
     */
    public float getBeatProxemity() {
        return 2 * Math.abs(getPhase()-.5f);
    }

    /**
     * Makes the dancer perform a move
     * @param move the DanceMove for the dancer to perform
     * @return the DanceResult for the move, eg 'MISSED' or 'GREAT'
     */
    public DanceResult takeMove(DanceMove move) {

        missedLastTurn = false;

        // The wrong move is input
        if (move != getNearestMove()) {
            wrongMove();
            notifyResult(DanceResult.WRONG);
            return DanceResult.WRONG;
        }

        // This is the first attempted move this beat
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
        // Player attempted two moves this beat, punish them :)
        else
        {
            doneThisBeat = true;
            wrongMove();
            notifyResult(DanceResult.WRONG);
            return DanceResult.WRONG;
        }
    }

    /**
     * gets the queue of upcoming DanceMoves
     * @return array of DanceMoves
     */
    public DanceMove[] getMoveList() {
        return choreographer.getMoveList();
    }

    /**
     * get the move nearest to the current beat
     * eg if phase is .3f returns previous beat. If phase is .8f returns next beat.
     * @return DanceMove that on the nearest beat
     */
    public DanceMove getNearestMove() {
        if (this.getPhase() < .5f) {
            return choreographer.getMoveList()[0];
        } else {
            return choreographer.getMoveList()[1];
        }
    }

    /**
     * Did the player attempt a DanceMove last turn?
     * @return true if move attempted last beat
     */
    public boolean hasMissedLastBeat() {
        return missedLastTurn;
    };

    /**
     * Called if the player makes an incorrect move
     * Incorrect moves are those that are registered as WRONG
     * It does not include missed moves
     */
    public void wrongMove() {
        killCombo();
        choreographer.clearQueue();
    }

    /**
     * Called if the player made a sufficiently correct move
     * Good moves include GREAT moves, GOOD moves and OKAY moves
     */
    public void goodMove() {
        combo++;
    }

    /**
     * Gets the current number of successive correct moves
     * @return combo size as int
     */
    public int getCombo() {
        return this.combo;
    }

    /**
     * Zeroes the combo counter
     * Called when the player makes a WRONG move, MISSES a move or makes a LATE or EARLY move
     */
    public void killCombo() {
        combo = 0;
    }

    /**
     * Register an object to be notified when the beat drops
     * @param listener
     */
    public void subscribeToBeat(BeatListener listener) {
        beatListeners.add(listener);
    }

    /**
     * Notify subscribed beat listeners that an onbeat has occured
     */
    public void notifyOnBeat() {
        for (BeatListener b : beatListeners) {
            b.onBeat();
        }
    }

    /**
     * Notify subscribed beat listeners that an offbeat has occured
     */
    public void notifyOffBeat() {
        for (BeatListener b : beatListeners) {
            b.offBeat();
        }
    }

    /**
     * Notify subscribed beat listeners of the result of the DanceResult recent move
     * @param result
     */
    public void notifyResult(DanceResult result) {
        for (BeatListener b : beatListeners) {
            b.moveResult(result);
        }
    }
}
