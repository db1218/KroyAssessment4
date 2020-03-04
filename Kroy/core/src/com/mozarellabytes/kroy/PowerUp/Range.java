package com.mozarellabytes.kroy.PowerUp;

import com.badlogic.gdx.Gdx;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Screens.GameScreen;

import java.util.ArrayList;

public class Range extends PowerUp {

    float timeInRange;
    float elapsedTime;
    int rangeIncrease;
    FireTruck truck;


    public Range() {
        super("heart");
        timeInRange = 10;
        elapsedTime = 0;
        rangeIncrease = 3;
    }


    @Override
    public void invokePower(FireTruck truck) {
        this.truck = truck;
        truck.setRange(truck.type.getRange() + rangeIncrease);
    }


    @Override
    public void update() {
        super.update();
        if (truck != null) {
            canBeRendered = false;
            elapsedTime += Gdx.graphics.getRawDeltaTime();
            checkIfFinishedRange();
            Gdx.app.log("time", "time");
        }
    }

    private void checkIfFinishedRange() {
        if (elapsedTime >= timeInRange){
            truck.setRange(truck.type.getRange());
            canBeDestroyed = true;
        }
    }


}
