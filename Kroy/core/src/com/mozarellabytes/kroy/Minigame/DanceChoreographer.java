package com.mozarellabytes.kroy.Minigame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class to manege the moves (arrows) that the user
 * must copy on-beat to get right
 */
public class DanceChoreographer {

    /** The number of moves to queue up */
    private static final int MOVE_LIST_LENGTH = 10;

    /** Queue to store upcoming moves */
    private final List<DanceMove> moveList;

    /** Random agent for move selection */
    private final Random random;

    /**
     * Constructor for DanceChoreographer
     */
    public DanceChoreographer() {
        this.moveList = new ArrayList<>();
        this.random = new Random();

        for (int i = 0; i < MOVE_LIST_LENGTH; i++)
            this.moveList.add(DanceMove.NONE);
    }

    /**
     * Consumes and returns the danceMove at the head of the queue
     */
    public void nextMove() {
        int rand = this.random.nextInt(DanceMove.values().length);
        DanceMove randomMove = DanceMove.values()[rand];
        this.moveList.add(randomMove);
        this.moveList.remove(0);
    }

    /**
     * Clears the dance queue
     */
    public void clearQueue() {
        for (int i = 0; i < moveList.size(); i++) {
            moveList.set(i, DanceMove.NONE);
        }
    }

    /**
     * Gets the queue of upcoming moves
     * @return an array of DanceMoves
     */
    public List<DanceMove> getMoveList() { return this.moveList; }

}
