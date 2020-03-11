package com.mozarellabytes.kroy.PowerUp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;

public class Range extends PowerUp {

    final float timeInRange;
    float elapsedTime;
    final int rangeIncrease;
    FireTruck truck;


    public Range(Vector2 location) {
        super("heart", location);
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
