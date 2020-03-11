package com.mozarellabytes.kroy.Minigame;

import java.util.ArrayList;
import java.util.List;

/**
 * DanceManager controls all aspects of the minigame
 */
public class DanceManager {

    /** The time in seconds between beats */
    private float beatDuration;

    /** The time since the last beat in seconds */
    private float timeSinceBeat;

    /** The time since the last half-beat in seconds */
    private float halfBeat;

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

    /**
     * Constructor for Dance Manager with a given bpm
     *
     * @param beatsPerMinute    bpm to match the song
     */
    public DanceManager(float beatsPerMinute) {

        // All measurements are in seconds
        this.beatDuration = 60/beatsPerMinute;
        this.timeSinceBeat = 0;
        this.halfBeat = -beatDuration/2;

        // Setup dance queue
        this.choreographer = new DanceChoreographer();
        this.beatListeners = new ArrayList<>();

        this.notDanceMoves = new ArrayList<>();
        this.notDanceMoves.add(DanceMove.WAIT);
        this.notDanceMoves.add(DanceMove.NONE);
    }

    /**
     * Called once a frame to update the dance manager
     *
     * @param delta time since last frame
     */
    public void update(float delta) {
        this.timeSinceBeat += delta;
        this.halfBeat += delta;

        if (this.timeSinceBeat >= this.beatDuration) onBeat();
        if (this.halfBeat >= this.beatDuration) offBeat();
    }

    /**
     * When the player gets the timing correct
     */
    private void onBeat() {
        this.choreographer.nextMove();
        this.timeSinceBeat = 0f;
        notifyOnBeat();
    }

    /**
     * When the player gets the timing incorrect
     */
    private void offBeat() {
        this.halfBeat = 0f;
        if (!doneThisBeat && !notDanceMoves.contains(getNearestMove()) ){
            killCombo();
            missedLastTurn = true;
        }
        doneThisBeat = false;
        notifyOffBeat();
    }

    /**
     * Returns the phase difference of the beat at the current time where 0 is directly on the beat
     * and lim x = 1 is directly on the next beat.
     * @return the phase through the next beat
     * */
    public float getPhase() {
        return timeSinceBeat/beatDuration;
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
        if (!doneThisBeat) {
            float proximityToBeat = getBeatProximity();
            float phase = getPhase();

            DanceResult result;

            doneThisBeat = true;

            if (proximityToBeat > .91f) {
                goodMove();
                result = DanceResult.GREAT;
            } else if (proximityToBeat > .86f) {
                goodMove();
                result = DanceResult.GOOD;
            } else if (proximityToBeat > .75f) {
                goodMove();
                result = DanceResult.OKAY;
            } else if (proximityToBeat > .5 && phase > .5f) {
                killCombo();
                result = DanceResult.EARLY;
            } else if (proximityToBeat > .5 && phase < .5f) {
                killCombo();
                result = DanceResult.LATE;
            } else {
                wrongMove();
                result = DanceResult.WRONG;
            }
            notifyResult(result);
            return result;
        } else {
            // Player attempted two moves this beat, punish them :)
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
        return this.choreographer.getMoveList();
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
     * @param listener  beat listener
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
     * @param result    result of move
     */
    public void notifyResult(DanceResult result) {
        for (BeatListener listener : beatListeners) {
            listener.moveResult(result);
        }
    }

}
