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
    private Random random;

    public DanceChoreographer() {
        moveList = new ArrayList<DanceMove>();
        random = new Random();

        for (int i = 0; i < MOVE_LIST_LENGTH; i++) {
            moveList.add(DanceMove.NONE);
        }
    }


    /**
     * Consumes and returns the danceMove at the head of the queue
     * @return the next dance move
     */
    public DanceMove nextMove() {
        int rand = random.nextInt(DanceMove.values().length);
        moveList.add(DanceMove.values()[rand]);
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

    /**
     * Gets the queue of upcoming moves
     * @return an array of DanceMoves
     */
    public List<DanceMove> getMoveList() { return moveList; }

}
