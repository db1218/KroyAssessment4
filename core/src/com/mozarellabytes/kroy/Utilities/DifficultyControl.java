package com.mozarellabytes.kroy.Utilities;

import com.mozarellabytes.kroy.Kroy;

public class DifficultyControl {
    private Integer CurrentDifficulty;
    private float DifficultyChangeInterval;
    private float CurrentTime;


    public DifficultyControl(){
        CurrentDifficulty = 0;
        DifficultyChangeInterval = 60f;
        CurrentTime = 60;
    }

    public Integer getCurrentDifficulty() {
        return CurrentDifficulty;
    }
    public float getCurrentTime(){
        return CurrentTime;
    }
    public void setCurrentTime(float newTime){
        CurrentTime = newTime;
    }
    public String getDifficultyOutput(){
        return ("Difficulty:" + String.valueOf(CurrentDifficulty) + "\n" +
                "Time To Increase:" + String.valueOf(CurrentTime));
    }
    public void changeDifficulty(){
        if (CurrentTime == 0){
            CurrentTime = DifficultyChangeInterval;
            CurrentDifficulty++;
        }
    }
}
