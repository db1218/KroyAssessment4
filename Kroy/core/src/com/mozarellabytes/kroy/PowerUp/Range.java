package com.mozarellabytes.kroy.PowerUp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;

/**
 * Range extends the range of a truck for a set amount
 * of time
 */
public class Range extends PowerUp {

    /** The time in seconds that the PowerUp lasts for */
    final float powerUpDuration;

    /** The time in seconds that the truck has had in this PowerUp */
    float timeInPowerUp;

    /** The amount that the truck's range should increase */
    final int rangeIncrease;

    /** The truck that the PowerUp is being used on */
    FireTruck truck;

    /**
     * Constructor for Range
     *
     * @param location  where the PowerUp spawns on the map
     */
    public Range(Vector2 location) {
        super("range", location);
        powerUpDuration = 10;
        timeInPowerUp = 0;
        rangeIncrease = 3;
    }

    /** This increases the truck's range */
    @Override
    public void invokePower(FireTruck truck) {
        this.truck = truck;
        truck.setRange(truck.type.getRange() + rangeIncrease);
    }

    @Override
    public String getName() {
        return "Range";
    }

    @Override
    public String getDesc() {
        return "Increase range of the fire truck by 3 tiles for 10 seconds";
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
            timeInPowerUp += Gdx.graphics.getRawDeltaTime();
            if (timeInPowerUp >= powerUpDuration)revokePowerUp();
        }
    }

    /**
     * Returns the truck's range to the value of the truck's range
     * before the powerup, sets canBeDestroyed to true so that the
     * gameScreen can destroy this powerUp.
     */
    private void revokePowerUp() {
        truck.setRange(truck.type.getRange());
        canBeDestroyed = true;
    }


}
