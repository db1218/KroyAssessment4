package com.mozarellabytes.kroy;

import com.mozarellabytes.kroy.Screens.GameOverScreen;

/** This class is used to keep track of the player's progress within
 * the game. It keeps track of how many active fire trucks the user
 * has and how many fortresses have been destroyed and causes the game
 * to end declaring the player has having won or lost
 */

public class GameState {

    /** Number of fire trucks there are on screen */
    private int activeFireTrucks;

    private int activePatrols;

    private boolean hasShownDanceTutorial;

    /** The number of fortresses the player has destroyed */
    private int fortressesDestroyed;

    /** The number of trucks that have a fortress within their attack range */
    private int trucksInAttackRange;

    private boolean stationDestoyed;

    /** Constructor for GameState */
    public GameState() {
        this.activeFireTrucks = 0;
        this.fortressesDestroyed = 0;
        this.activePatrols = 0;
        this.stationDestoyed = false;
        this.hasShownDanceTutorial = false;
    }

    /** Adds one to activeFireTrucks, called when a firetruck is spawned */
    public void addFireTruck() {
        this.activeFireTrucks++;
    }

    public void setStationDestoyed() {
        this.stationDestoyed = true;
    }

    public boolean hasStationDestoyed() {
        return this.stationDestoyed;
    }

    /** Removes one from activeFireTrucks, called when a firetruck
     * is destroyed */
    public void removeFireTruck() {
        this.activeFireTrucks--;
    }

    /** Adds one to fortressesDestroyed when a user has destroyed a
     * fortress */
    public void addFortress() {
        this.fortressesDestroyed++;
    }

    /** Determines whether the game has ended either when a certain
     * number of fortresses have been destroyed or when there are no
     * fire trucks left
     * @param game LibGDX game
     */
    public void hasGameEnded(Kroy game) {
        if (fortressesDestroyed == 6) {
            endGame(true, game);
        } else if (this.activeFireTrucks == 0) {
            endGame(false, game);
        }
    }

    public boolean firstFortressDestroyed() {
        return (fortressesDestroyed >= 1) && (!stationDestoyed);
    }

    /** Triggers the appropriate game over screen depending
     * on if the user has won or lost
     * @param playerWon <code> true </code> if player has won
     *                  <code> false </code> if player has lost
     * @param game LibGDX game
     */
    private void endGame(Boolean playerWon, Kroy game) {
        if (playerWon) {
            game.setScreen(new GameOverScreen(game, true));
        } else {
            game.setScreen(new GameOverScreen(game, false));
        }
    }

    public void setTrucksInAttackRange(int number){
        trucksInAttackRange = number;
    }

    public void incrementTrucksInAttackRange(){
        trucksInAttackRange++;
    }

    public int getTrucksInAttackRange(){
        return trucksInAttackRange;
    }

    public void setDanceTutorialShown() { this.hasShownDanceTutorial = true; }

    public boolean hasDanceTutorialShown() { return this.hasShownDanceTutorial; }


}
