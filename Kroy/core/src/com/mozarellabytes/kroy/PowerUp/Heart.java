package com.mozarellabytes.kroy.PowerUp;

import com.mozarellabytes.kroy.Entities.FireTruck;

public class Heart extends PowerUp {

    public Heart() { super("heart"); }

    @Override
    public void invokePower(FireTruck truck) {
        restoreHealth(truck);
        removePowerUp();
    }

    private void restoreHealth(FireTruck truck) {
        int fullHP = (int)truck.type.getMaxHP();
        truck.setHP(fullHP);
    }

}
