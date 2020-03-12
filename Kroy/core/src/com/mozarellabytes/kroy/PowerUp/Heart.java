package com.mozarellabytes.kroy.PowerUp;

import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;

/**
 * Heart replenishes the Fire Truck's health to full
 */
public class Heart extends PowerUp {

    /**
     * Constructor for Heart
     * @param location  where the power up is
     */
    public Heart(Vector2 location) { super("heart", location); }

    @Override
    public void invokePower(FireTruck truck) {
        restoreHealth(truck);
        removePowerUp();
    }

    /**
     * Replenish health
     * @param truck to heal
     */
    private void restoreHealth(FireTruck truck) {
        int fullHP = (int)truck.type.getMaxHP();
        truck.setHP(fullHP);
    }

}
