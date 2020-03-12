package com.mozarellabytes.kroy.PowerUp;

import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;

/**
 * Freeze makes the freeze feature available,
 * bypassing the cooldown
 */
public class Freeze extends PowerUp {

    /**
     * Constructor with given location
     * @param location  of power up
     */
    public Freeze(Vector2 location) { super("water", location); }

    @Override
    public void invokePower(FireTruck truck) {
        truck.makeFreezeAvailable();
        removePowerUp();
    }

}


