package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;

/**
 * FireTruckType is an enum defining the patrols that can be present in the game.
 * Each patrol type has a unique reserve, speed, trail colour, range, and attack points.
 * This allows there to be numerous different types of FireTrucks and numerous patrols
 * of each type without having to randomly generate values which may make the game
 * unplayable in some instances and too easy in other instances.
 */
public enum PatrolType {

    /**
     * The preset values for the different patrol types includes the type's:
     * texture, speed, name, name, attack range, attack points
     */
    Green(new Texture(Gdx.files.internal("sprites/Patrol/greenPatrol.png")),"Green Patrol", 0.16f, 100, new Vector2(18, 2), new Vector2(48, 2), new Vector2(48, 20), new Vector2(18, 20)),
    Blue(new Texture(Gdx.files.internal("sprites/Patrol/bluePatrol.png")),"Blue Patrol", 0.08f, 150, new Vector2(3, 27), new Vector2(3, 19), new Vector2(19,19), new Vector2(19, 27)),
    Peach(new Texture(Gdx.files.internal("sprites/Patrol/peachPatrol.png")),"Peach Patrol",  0.16f, 100, new Vector2(3, 4), new Vector2(10, 16), new Vector2(32, 21), new Vector2(27, 9)),
    Violet(new Texture(Gdx.files.internal("sprites/Patrol/violetPatrol.png")),"Violet Patrol",  0.08f, 150, new Vector2(49, 18), new Vector2(39, 28), new Vector2(29, 18), new Vector2(39, 8)),
    Yellow(new Texture(Gdx.files.internal("sprites/Patrol/yellowPatrol.png")),"Yellow Patrol", 0.08f, 150, new Vector2(47, 13), new Vector2(47, 21), new Vector2(28, 21), new Vector2(28, 13)),
    Boss(new Texture(Gdx.files.internal("sprites/Patrol/PatrolBoss.png")),"Boss Patrol",  0.04f, 150, new Vector2(45, 11), new Vector2(44, 11), new Vector2(7, 11), new Vector2(7, 10));

    /**
     * Texture for this type of patrol
     */
    private final Texture texture;

    /**
     * The maximum amount of health points this type of patrol can have,
     * also the value of the patrol's health points when it is spawned
     */
    private final float maxHP;

    /**
     * The speed at which this type of patrol moves around the map
     */
    private final String name;

    /**
     * The Attack points the patrol deals when in the minigame
     */
    private final float AP;

    /**
     * Points the patrol will cycle through
     */
    private final Queue<Vector2> points;

    /**
     * Constructs the PatrolType
     *
     * @param texture Texture of this type of patrol
     * @param name The name for this type of patrol
     * @param AP the attack points for this type of patrol
     */
    PatrolType(Texture texture, String name, float AP, float maxHP, Vector2 point1, Vector2 point2, Vector2 point3, Vector2 point4) {
        this.texture = texture;
        this.maxHP = maxHP;
        this.name = name;
        this.AP = AP;
        this.points = new Queue<>();
        this.points.addLast(point1);
        this.points.addLast(point2);
        this.points.addLast(point3);
        this.points.addLast(point4);
    }

    public float getMaxHP() {
        return this.maxHP;
    }

    public Texture getTexture(){
        return this.texture;
    }

    public Queue<Vector2> getPoints() {
        return this.points;
    }
}
