package com.mozarellabytes.kroy.PowerUp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;

/**
 * Freeze makes the freeze feature available,
 * bypassing the cooldown
 */
public class Freeze extends PowerUp {

    /**
     * Constructor with given location
     * @param location  where the PowerUp spawns on the map
     */
    public Freeze(Vector2 location) { super("freeze", location); }

    /** This makes a Freeze instantly available in
     * gameScreen and then sets the appropriate
     * flags so that the PowerUp can be removed
     * from gameScreen */
    @Override
    public void invokePower(FireTruck truck) {
        Gdx.app.log("new freeze", String.valueOf(this));
        this.isPowerCurrentlyInvoked = true;
        truck.makeFreezeAvailable();
        removePowerUp();
    }

    @Override
    public String getName() {
        return "Freeze";
    }

    @Override
    public String getDesc() {
        return "Makes the freeze ability available, bypassing the cooldown ";
    }

    @Override
    public float getTimeLeftInPowerup() {
        return 0;
    }


}


