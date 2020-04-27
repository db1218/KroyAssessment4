package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * FireTruckType is an enum defining the trucks that can be present in the game.
 * Each truck type has a unique reserve, speed, trail colour, range, and attack points.
 * This allows there to be numerous different types of FireTrucks and numerous trucks
 * of each type without having to randomly generate values which may make the game
 * unplayable in some instances and too easy in other instances.
 */

public enum FireTruckType {

    /** The preset values for the different truck types includes the type's:
     * maximum reserve, speed, trailColour, name, attack range, attack points
     */
    RubyEasy(160, 4.2f, Color.RED, "Ruby Truck", 5.5f, 0.16f, 180,"red"),
    SapphireEasy(290, 3.3f, Color.CYAN, "Sapphire Truck", 6.5f, 0.25f, 150, "blue"),
    AmethystEasy(320, 2.7f, Color.PURPLE, "Amethyst Truck", 6.7f, 0.27f, 240,"purple"),
    EmeraldEasy(180, 4, Color.GREEN, "Emerald Truck", 4.5f, 0.3f, 150, "green"),

    RubyMedium(140, 4.2f, Color.RED, "Ruby Truck", 5.5f, 0.14f, 160, "red"),
    SapphireMedium(280, 3.2f, Color.CYAN, "Sapphire Truck", 6.2f, 0.20f, 140, "blue"),
    AmethystMedium(300, 2.6f, Color.PURPLE, "Amethyst Truck", 6.5f, 0.24f, 230, "purple"),
    EmeraldMedium(160, 4, Color.GREEN, "Emerald Truck", 4.5f, 0.3f, 140, "green"),

    RubyHard(160, 4.2f, Color.RED, "Ruby Truck", 5, 0.14f, 150, "red"),
    SapphireHard(300, 3, Color.CYAN, "Sapphire Truck", 6, 0.16f, 130, "blue"),
    AmethystHard(320, 2.4f, Color.PURPLE, "Amethyst Truck", 6.5f, 0.20f, 180, "purple"),
    EmeraldHard(180, 4, Color.GREEN, "Emerald Truck", 4.5f, 0.24f, 110, "green");


    /** The maximum amount of water this type of truck can have,
     * also the value of the truck's reserve when it is spawned */
    private final float maxReserve;

    /** The maximum amount of health points this type of truck can have,
     * also the value of the truck's health points when it is spawned */
    private final float maxHP;

    /** The speed at which this type of truck moves around the map */
    private final float speed;

    /** The colour of the trail that is visible once a truck's path has
     * been drawn */
    private final Color trailColour;

    /** The name of this type of truck */
    private final String name;

    /** The attack range of this type of truck */
    private final float range;

    /** Attack points - the damage this truck can inflict */
    private final float AP;

    /** The tile sprite that is drawn to GameScreen once a truck's
     * path has been drawn */
    private final Texture trailImage;

    /** The tile sprite that marks the end of the truck's path so the
     * user knows which tile they can continue drawing the truck's
     * path from */
    private final Texture trailImageEnd;

    /** The sprites for each of the truck's directions*/
    private final Texture lookLeft;
    private final Texture lookRight;
    private final Texture lookUp;
    private final Texture lookDown;

    /**
     * Constructs the FireTruckType
     *  @param maxReserve The maximum reserve for this type of truck;
     * @param speed The speed of this type of truck
     * @param trailColour The colour of the truck's path when it has been drawn
     * @param name The name for this type of truck
     * @param range The attack range for this type of truck in tiles
     * @param AP the attack points for this type of truck
     * @param spriteFileName the file name that contains the sprites for each of the truck's directions
     *
     */
    FireTruckType(int maxReserve, float speed, Color trailColour, String name, float range, float AP,  float maxHP, String spriteFileName) {
        this.maxReserve = maxReserve;
        this.maxHP = maxHP;
        this.speed = speed;
        this.trailColour = trailColour;
        this.name = name;
        this.range = range;
        this.AP = AP;

        this.lookLeft = new Texture(Gdx.files.internal("sprites/firetruck/"+spriteFileName+"/left.png"));
        this.lookRight = new Texture(Gdx.files.internal("sprites/firetruck/"+spriteFileName+"/right.png"));
        this.lookUp = new Texture(Gdx.files.internal("sprites/firetruck/"+spriteFileName+"/up.png"));
        this.lookDown = new Texture(Gdx.files.internal("sprites/firetruck/"+spriteFileName+"/down.png"));

        this.trailImage = new Texture(Gdx.files.internal("sprites/firetruck/trail.png"));
        this.trailImageEnd = new Texture(Gdx.files.internal("sprites/firetruck/trailEnd.png"));
    }

    public float getMaxReserve(){ return this.maxReserve; }

    public float getMaxHP(){ return this.maxHP; }

    public float getSpeed(){ return this.speed; }

    public Color getTrailColour() { return this.trailColour; }

    public String getName() { return this.name; }

    public float getRange() { return this.range; }

    public float getAP() { return this.AP; }

    public Texture getTrailImage(){ return this.trailImage; }

    public Texture getTrailImageEnd() { return this.trailImageEnd; }

    public Texture getLook(String rotation) {
        if (rotation == null) return getLookDown();
        switch (rotation) {
            case "right":
                return getLookRight();
            case "left":
                return getLookLeft();
            case "up":
                return getLookUp();
            default:
                return getLookDown();
        }
    }

    public Texture getLookLeft() { return this.lookLeft; }

    public Texture getLookRight() { return this.lookRight; }

    public Texture getLookUp() { return this.lookUp; }

    public Texture getLookDown() { return this.lookDown; }

}
