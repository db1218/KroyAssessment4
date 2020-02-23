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
        this.moveList = new ArrayList<DanceMove>();
        this.random = new Random();

        for (int i = 0; i < MOVE_LIST_LENGTH; i++) {
            this.moveList.add(DanceMove.NONE);
        }
    }


    /**
     * Consumes and returns the danceMove at the head of the queue
     * @return the next dance move
     */
    public DanceMove nextMove() {
        int rand = this.random.nextInt(DanceMove.values().length);
        DanceMove randomMove = DanceMove.values()[rand];
        this.moveList.add(randomMove);
        return this.moveList.remove(0);
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
