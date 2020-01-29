package com.mozarellabytes.kroy.Minigame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DanceChoreographer {

    private static final int MOVE_LIST_LENGTH = 10;

    private List<DanceMove> moveList;

    private int clumpLength = 0;

    private Random random = new Random();

    public DanceChoreographer() {
        moveList = new ArrayList<DanceMove>();

        for (int i = 0; i < MOVE_LIST_LENGTH; i++) {
            moveList.add(DanceMove.NONE);
        }
    }

    public DanceMove[] getMoveList() {
        DanceMove[] moves = new DanceMove[MOVE_LIST_LENGTH];
        for (int i = 0; i < MOVE_LIST_LENGTH; i++) {
            moves[i] = moveList.get(i);
        }
        return moves;
    }

    public DanceMove nextMove() {
        if (clumpLength == 0) {
            if (random.nextInt(3) == 0) {
                // Initiate clump
                clumpLength = 3;
                moveList.add(DanceMove.WAIT);
                return moveList.remove(0);
            } else {
                moveList.add(DanceMove.values()[random.nextInt(5)]);
                return moveList.remove(0);
            }
        } else {
            clumpLength--;
            moveList.add(DanceMove.WAIT);
            return moveList.remove(0);
        }
    }

    public void clearQueue() {
        for (int i = 0; i < moveList.size(); i++) {
            moveList.set(i, DanceMove.NONE);
        }
    }
}
