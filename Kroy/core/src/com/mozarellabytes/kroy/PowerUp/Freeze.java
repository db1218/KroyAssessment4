package com.mozarellabytes.kroy.PowerUp;

import com.mozarellabytes.kroy.Entities.FireTruck;

public class Freeze extends PowerUp {

    public Freeze() { super("water"); }

    @Override
    public void invokePower(FireTruck truck) {
        truck.makeFreezeAvailable();
        removePowerUp();
    }




}


