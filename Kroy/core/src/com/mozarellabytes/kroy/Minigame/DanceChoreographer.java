package com.mozarellabytes.kroy.Minigame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class to manage the moves (arrows) that the user
 * must copy on-beat to get right
 */
public class DanceChoreographer {

    /** The number of moves to queue up */
    private static final int MOVE_LIST_LENGTH = 10;

    /** List to store upcoming moves */
    private final List<DanceMove> moveList;

    /** Random agent for move selection */
    private final Random random;

    /**
     * Constructor for DanceChoreographer, creates a random object which is
     * used frequently in nextMove() and sets up the list of moves of length
     * MOVE_LIST_LENGTH with DanceMove.NONE, this gives the player some time
     * before the moves that the player has to press the corresponding key
     * for begins
     */
    public DanceChoreographer() {
        this.moveList = new ArrayList<>();
        this.random = new Random();

        for (int i = 0; i < MOVE_LIST_LENGTH; i++)
            this.moveList.add(DanceMove.NONE);
    }

    /**
     * This creates a random move, adds it to the end of moveList and
     * removes the first (i.e move that has just been) from moveList,
     * this ensures moveList remains at a constant size.
     */
    public void createMove() {
        int rand = this.random.nextInt(DanceMove.values().length);
        DanceMove randomMove = DanceMove.values()[rand];
        this.moveList.add(randomMove);
        this.moveList.remove(0);
    }

    /**
     * Clears the dance list by setting each move in moveList to
     * NONE; if .clear() is used then there is a null pointer
     * exception error in DanceScreen as there is no arrow to render
     * in an empty list
     */
    public void clearQueue() {
        for (int i = 0; i < moveList.size(); i++) {
            moveList.set(i, DanceMove.NONE);
        }
    }

    public List<DanceMove> getMoveList() { return this.moveList; }

}
