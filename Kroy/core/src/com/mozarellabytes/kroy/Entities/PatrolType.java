package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;

import java.util.Random;

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
    Green(new Texture(Gdx.files.internal("sprites/Patrol/greenPatrol.png")), 0.16f, 70),
    Blue(new Texture(Gdx.files.internal("sprites/Patrol/bluePatrol.png")), 0.08f, 120),
    Peach(new Texture(Gdx.files.internal("sprites/Patrol/peachPatrol.png")),0.16f, 70),
    Violet(new Texture(Gdx.files.internal("sprites/Patrol/violetPatrol.png")),0.08f, 120),
    Yellow(new Texture(Gdx.files.internal("sprites/Patrol/yellowPatrol.png")),0.08f, 120),
    Boss(new Texture(Gdx.files.internal("sprites/Patrol/PatrolBoss.png")),0.04f, 150);

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
     * The Attack points the patrol deals when in the minigame
     */
    private final float AP;

    /**
     * Constructs the PatrolType
     *
     * @param texture Texture of this type of patrol
     * @param AP the attack points for this type of patrol
     */
    PatrolType(Texture texture, float AP, float maxHP) {
        this.texture = texture;
        this.maxHP = maxHP;
        this.AP = AP;
    }

    public float getMaxHP() {
        return this.maxHP;
    }

    public Texture getTexture(){
        return this.texture;
    }

    public float getAP() {
        return this.AP;
    }
}
