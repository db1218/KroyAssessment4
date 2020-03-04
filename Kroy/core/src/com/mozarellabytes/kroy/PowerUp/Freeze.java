package com.mozarellabytes.kroy.PowerUp;

import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;

public class Freeze extends PowerUp {

    public Freeze(Vector2 location) { super("water", location); }

    @Override
    public void invokePower(FireTruck truck) {
        truck.makeFreezeAvailable();
        removePowerUp();
    }

}


