/*
package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

*/
/**
 * FireTruckType is an enum defining the trucks that can be present in the game.
 * Each truck type has a unique reserve, speed, trail colour, range, and attack points.
 * This allows there to be numerous different types of FireTrucks and numerous trucks
 * of each type without having to randomly generate values which may make the game
 * unplayable in some instances and too easy in other instances.
 *//*


public enum PatrolType {

    */
/** The preset values for the different truck types includes the type's:
     * maximum reserve, speed, trailColour, name, attack range, attack points
     *//*

    //Magenta(100, 2, "Magenta Patrol", 5, 0.08f, 150),
    Green(250, 1, "Green Patrol", 7, 0.16f, 100),
    Blue(100, 2, "Blue Patrol", 5, 0.08f, 150);



    */
/** The maximum amount of water this type of truck can have,
     * also the value of the truck's reserve when it is spawned *//*

    private final float maxReserve;

    */
/** The maximum amount of health points this type of truck can have,
     * also the value of the truck's health points when it is spawned *//*

    private final float maxHP;

    */
/** The speed at which this type of truck moves around the map *//*

    private final float speed;

    */
/** The name of this type of truck *//*

    private final String name;

    */
/** The attack range of this type of truck *//*

    private final float range;

    */
/** Attack points - the damage this truck can inflict *//*

    private final float AP;

    */
/**
     * Constructs the FireTruckType
     *
     * @param maxReserve The maximum reserve for this type of truck;
     * @param speed The speed of this type of truck
     * @param name The name for this type of truck
     * @param range The attack range for this type of truck in tiles
     * @param AP the attack points for this type of truck
     *
     *//*

    PatrolType(int maxReserve, int speed, String name, float range, float AP, float maxHP) {
        this.maxReserve = maxReserve;
        this.maxHP = maxHP;
        this.speed = speed;
        this.name = name;
        this.range = range;
        this.AP = AP;
    }

    public float getMaxReserve(){ return this.maxReserve; }

    public float getMaxHP(){ return this.maxHP; }

    public float getSpeed(){ return this.speed; }

    public String getName() { return this.name; }

    public float getRange() { return this.range; }

    public float getAP() { return this.AP; }

}
*/
