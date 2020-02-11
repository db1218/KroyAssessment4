package com.mozarellabytes.kroy.Minigame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DanceChoreographer {

    /** The number of moves to queue up */
    private static final int MOVE_LIST_LENGTH = 10;

    /** Queue to store upcoming moves */
    private List<DanceMove> moveList;

    /** Random agent for move selection */
    private Random random = new Random();


    public DanceChoreographer() {
        moveList = new ArrayList<DanceMove>();

        for (int i = 0; i < MOVE_LIST_LENGTH; i++) {
            moveList.add(DanceMove.NONE);
        }
    }

    /**
     * Gets the queue of upcoming moves
     * @return an array of DanceMoves
     */
    public DanceMove[] getMoveList() {
        DanceMove[] moves = new DanceMove[MOVE_LIST_LENGTH];
        for (int i = 0; i < MOVE_LIST_LENGTH; i++) {
            moves[i] = moveList.get(i);
        }
        return moves;
    }

    /**
     * Consumes and returns the danceMove at the head of the queue
     * @return the next dance move
     */
    public DanceMove nextMove() {
        moveList.add(DanceMove.values()[random.nextInt(5)]);
        return moveList.remove(0);
    }

    /**
     * Clears the dance queue
     */
    public void clearQueue() {
        for (int i = 0; i < moveList.size(); i++) {
            moveList.set(i, DanceMove.NONE);
        }
    }
}
