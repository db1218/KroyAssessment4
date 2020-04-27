package com.mozarellabytes.kroy.PowerUp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;

/**
 * Shield makes the Fire Truck immune to damage from
 * fortresses and patrols for a set amount of time
 */
public class Shield extends PowerUp {

    /** The time in seconds that the truck has left in this PowerUp */
    float timeLeftInPowerUp;

    /** The truck that the PowerUp is being used on */
    FireTruck truck;

    /**
     * Constructor for Shield
     *
     * @param location  where the PowerUp spawns on the map
     */
    public Shield(Vector2 location) {
        super("shield", location);
        timeLeftInPowerUp = 15;
    }

    /** This updates the amount of time that the truck has been
     * in the PowerUp, it sets canBeRendered to false so that
     * gameScreen doesn't render the powerUp as the truck has
     * driven over it */
    @Override
    public void update() {
        super.update();
        if (truck != null) {
            canBeRendered = false;
            timeLeftInPowerUp -= Gdx.graphics.getRawDeltaTime();
            if (timeLeftInPowerUp <= 0)revokePowerUp();
        }
    }

    /** This sets the shield giving the truck immunity */
    @Override
    public void invokePower(FireTruck truck) {
        Gdx.app.log("new shield", String.valueOf(this));
        this.isPowerCurrentlyInvoked = true;
        this.truck = truck;
        truck.setShield(true);
    }

    /**
     * Turns the truck's shield off, sets
     * canBeDestroyed to true so that the
     * gameScreen can destroy this powerUp.
     */
    private void revokePowerUp() {
        Gdx.app.log("shield destroyed", String.valueOf(this));
        truck.setShield(false);
        isPowerCurrentlyInvoked = false;
        canBeDestroyed = true;
    }

    @Override
    public String getName() {
        return "Shield";
    }

    @Override
    public String getDesc() {
        return "Makes the fire truck immune to damage for 5 seconds";
    }

    @Override
    public float getTimeLeftInPowerup() {
        return timeLeftInPowerUp;
    }


}
