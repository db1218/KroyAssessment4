package com.mozarellabytes.kroy.PowerUp;

import com.mozarellabytes.kroy.Entities.FireTruck;

import java.util.ArrayList;

public class Water extends PowerUp {

    public Water() {
        super("water");
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
