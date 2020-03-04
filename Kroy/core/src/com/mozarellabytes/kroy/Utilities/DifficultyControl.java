package com.mozarellabytes.kroy.Utilities;

import java.text.DecimalFormat;

public class DifficultyControl {
    /** The value of the current difficulty level */
    private int currentDifficulty;

    /** The amount of time, in seconds, for the difficulty to increment by one */
    private float difficultyChangeInterval;

    /** The amount of time that has passed since the last increment of difficulty */
    private float currentTime;


    /** The constructor for DifficultyControl
     */
    public DifficultyControl(int currentDifficulty, float currentTime) {
        this.currentDifficulty = currentDifficulty;
        difficultyChangeInterval = 60f;
        this.currentTime = currentTime;
    }

    public DifficultyControl() {
        this.currentDifficulty = 0;
        difficultyChangeInterval = 60f;
        this.currentTime = 0;
    }


    /** Updates the amount of time to a change in difficulty
     *
     * @param TimeDelta The amount of time since the last time update
     */
    public void incrementCurrentTime(float TimeDelta){
        currentTime -= TimeDelta;
        changeDifficulty();
    }

    /** Gives the difficulty information as String that can be displayed by the GUI
     *
     * @return A String containing the current difficulty and time to the next difficulty increment
     */
    public float getTimeSinceLastDifficultyIncrease() {
        return this.currentTime;
    }

    /** Checks if enough time has passed for the difficulty to increment
     * Increases difficulty by 1 if needed
     */
    private void changeDifficulty(){
        if (currentTime <= 0){
            currentTime = difficultyChangeInterval;
            currentDifficulty++;
        }
    }

    /** Returns the current difficulty level as a multiplier to be used
     *
     * @return A float equal to one tenth of the current difficulty level
     */
    public float getDifficultyMultiplier(){
        return (currentDifficulty + 10)/10f;
    }
}
