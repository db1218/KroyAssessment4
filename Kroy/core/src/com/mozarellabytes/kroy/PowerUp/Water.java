package com.mozarellabytes.kroy.PowerUp;

import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;

/**
 * Water replenishes the water/reserve level to full
 */
public class Water extends PowerUp {

    /**
     * Contructor for Water
     * @param location  where the power up is
     */
    public Water(Vector2 location) {
        super("water", location);
    }

    @Override
    public void invokePower(FireTruck truck) {
        refillReserve(truck);
        removePowerUp();
    }

    /**
     * Refills the truck to full reserve capacity
     *
     * @param truck to get refilled
     */
    private void refillReserve(FireTruck truck) {
        int maxReserve = (int) truck.type.getMaxReserve();
        int currentReserve = (int) truck.getReserve();
        truck.refill(maxReserve - currentReserve);
    }


}
