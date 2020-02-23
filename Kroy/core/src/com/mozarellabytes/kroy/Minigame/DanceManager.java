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

    private List<DanceMove> notDanceMoves;

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

        this.notDanceMoves = new ArrayList<>();
        this.notDanceMoves.add(DanceMove.WAIT);
        this.notDanceMoves.add(DanceMove.NONE);
    }

    /** Called once a frame to update the dance manager*/
    public void update(float delta) {
        time += delta;
        halfTime += delta;

        if (time >= period) onBeat();
        if (halfTime >= period) offBeat();
    }

    private void onBeat() {
        choreographer.nextMove();
        time = 0f;
        notifyOnBeat();
    }

    private void offBeat() {
        halfTime = 0f;
        if (!doneThisBeat && !notDanceMoves.contains(getNearestMove()) ){
            killCombo();
            missedLastTurn = true;
        }
        doneThisBeat = false;
        notifyOffBeat();
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
    public float getBeatProximity() {
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
            float proximity = getBeatProximity();
            float phase = getPhase();

            DanceResult result;

            doneThisBeat = true;

            if (proximity > .95f) {
                goodMove();
                result = DanceResult.GREAT;
            }
            else if (proximity > .9f) {
                goodMove();
                result = DanceResult.GOOD;
            }
            else if (proximity > .8) {
                goodMove();
                result = DanceResult.OKAY;
            }
            else if (proximity > .5 && phase > .5f) {
                killCombo();
                result = DanceResult.EARLY;
            }
            else if (proximity > .5 && phase < .5f) {
                killCombo();
                result = DanceResult.LATE;
            }
            else {
                wrongMove();
                result = DanceResult.WRONG;
            }
            notifyResult(result);
            return result;
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
    public List<DanceMove> getMoveList() {
        return choreographer.getMoveList();
    }

    /**
     * get the move nearest to the current beat
     * eg if phase is .3f returns previous beat. If phase is .8f returns next beat.
     * @return DanceMove that on the nearest beat
     */
    public DanceMove getNearestMove() {
        DanceMove previousBeat = choreographer.getMoveList().get(0);
        DanceMove nextBeat = choreographer.getMoveList().get(1);
        return this.getPhase() < .5f ? previousBeat : nextBeat;
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
    public void goodMove() { combo++; }

    /**
     * Gets the current number of successive correct moves
     * @return combo size as int
     */
    public int getCombo() { return this.combo; }

    /**
     * Zeroes the combo counter
     * Called when the player makes a WRONG move, MISSES a move or makes a LATE or EARLY move
     */
    public void killCombo() { combo = 0; }

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
        for (BeatListener listener : beatListeners) {
            listener.onBeat();
        }
    }

    /**
     * Notify subscribed beat listeners that an offbeat has occured
     */
    public void notifyOffBeat() {
        for (BeatListener listener : beatListeners) {
            listener.offBeat();
        }
    }

    /**
     * Notify subscribed beat listeners of the result of the DanceResult recent move
     * @param result
     */
    public void notifyResult(DanceResult result) {
        for (BeatListener listener : beatListeners) {
            listener.moveResult(result);
        }
    }
}
