package com.mozarellabytes.kroy.Utilities;

import java.text.DecimalFormat;

public class DifficultyControl {
    private Integer currentDifficulty;
    private float difficultyChangeInterval;
    private float currentTime;


    public DifficultyControl(){
        currentDifficulty = 0;
        difficultyChangeInterval = 60f;
        currentTime = 60;
    }

    public void incrementCurrentTime(float TimeDelta){
        currentTime -= TimeDelta;
        changeDifficulty();
    }
    public String getDifficultyOutput(){
        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        return ("Difficulty:" + String.valueOf(currentDifficulty) + "\n" +
                "Time To Increase:" + decimalFormat.format(currentTime));
    }
    public void changeDifficulty(){
        if (currentTime <= 0){
            currentTime = difficultyChangeInterval;
            currentDifficulty++;
        }
    }
    public float getDifficultyMultiplier(){
        return (currentDifficulty + 10)/10;
    }
}
