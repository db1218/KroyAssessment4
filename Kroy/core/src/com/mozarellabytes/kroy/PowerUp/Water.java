package com.mozarellabytes.kroy.PowerUp;

import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;

import java.util.ArrayList;

public class Water extends PowerUp {

    public Water(Vector2 location) {
        super("water", location);
    }

    @Override
    public void invokePower(FireTruck truck) {
        refillReserve(truck);
        removePowerUp();
    }

    private void refillReserve(FireTruck truck) {
        int maxReserve = (int)truck.type.getMaxReserve();
        int currentReserve = (int) truck.getReserve();
        truck.refill(maxReserve - currentReserve);
    }


}
