package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * FireTruckType is an enum defining the trucks that can be present in the game.
 * Each truck type has a unique reserve, speed, trail colour, range, and attack points.
 * This allows there to be numerous different types of FireTrucks and numerous trucks
 * of each type without having to randomly generate values which may make the game
 * unplayable in some instances and too easy in other instances.
 */


public enum PatrolType {

/** The preset values for the different truck types includes the type's:
     * maximum reserve, speed, trailColour, name, attack range, attack points
     */


    //Magenta(100, 2, "Magenta Patrol", 5, 0.08f, 150),
    Green(250, 1, "Green Patrol", 7, 0.16f, 100, new Vector2(12, 18), new Vector2(15, 18), new Vector2(15, 15), new Vector2(12, 15)),
    Blue(100, 2, "Blue Patrol", 5, 0.08f, 150, new Vector2(30, 17), new Vector2(33, 17), new Vector2(33, 14), new Vector2(30, 14));


    /** The maximum amount of water this type of truck can have,
     * also the value of the truck's reserve when it is spawned */

    private final float maxReserve;

/** The maximum amount of health points this type of truck can have,
     * also the value of the truck's health points when it is spawned */

    private final float maxHP;

/** The speed at which this type of truck moves around the map */

    private final float speed;

/** The name of this type of truck */

    private final String name;

/** The attack range of this type of truck */

    private final float range;

    private Vector2 target;

/** Attack points - the damage this truck can inflict */

    private final float AP;

/** Points the patrol will cycle through */

    private final Vector2 point1;
    private final Vector2 point2;
    private final Vector2 point3;
    private final Vector2 point4;




    /**
     * Constructs the FireTruckType
     *
     * @param maxReserve The maximum reserve for this type of truck;
     * @param speed The speed of this type of truck
     * @param name The name for this type of truck
     * @param range The attack range for this type of truck in tiles
     * @param AP the attack points for this type of truck
     *
     */

    PatrolType(int maxReserve, int speed, String name, float range, float AP, float maxHP, Vector2 point1, Vector2 point2, Vector2 point3, Vector2 point4) {
        this.maxReserve = maxReserve;
        this.maxHP = maxHP;
        this.speed = speed;
        this.name = name;
        this.range = range;
        this.AP = AP;
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
        this.point4 = point4;
        this.target = point2;
    }

    public float getMaxReserve(){ return this.maxReserve; }

    public float getMaxHP(){ return this.maxHP; }

    public float getSpeed(){ return this.speed; }

    public String getName() { return this.name; }

    public float getRange() { return this.range; }

    public float getAP() { return this.AP; }

    public Vector2 getPoint1(){
        return point1;
    }
    public Vector2 getPoint2(){
        return point2;
    }
    public Vector2 getPoint3(){
        return point3;
    }
    public Vector2 getPoint4(){
        return point4;
    }

    public Vector2 getTarget(){ return target; }
    public void setTarget(Vector2 target){ this.target=target;}

}

