package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.Utilities.Desc;

import java.util.Random;

/**
 * Patrol is an entity that the player controls. It navigates the map
 * going through points defined in the the patrolType *
 */
public class Patrol extends Sprite {

    /** Defines set of pre-defined attributes */
    protected PatrolType type;

    /**
     * path the patrol follows; the fewer item in
     * the path the slower the patrol will go
     */
    protected Queue<Vector2> path;

    /** Health points */
    protected float HP;

    /** Position of patrol in tiles */
    protected Vector2 position;

    /**
     * Unique name of the patrol
     */
    protected String name;

    /**
     * Constructs a new Patrol
     *
     * @param type  used to have predefined attributes
     * @param mapW  width of the map
     * @param mapH  height of the map
     * @param name  name of the patrol
     */
    public Patrol(PatrolType type, int mapW, int mapH, String name) {
        super(type.getTexture());
        this.type = type;
        this.name = name;
        this.HP = type.getMaxHP();
        this.path = generatePath(mapW, mapH);
        this.position = new Vector2(path.get(0).x, path.get(0).y);
    }

    /**
     * Constructs a Patrol from save at a position and of a certain type
     * which have been passed in
     *
     * @param type  used to have predefined attributes
     * @param HP    initial health of patrol
     * @param x     initial x position of patrol
     * @param y     initial y position of patrol
     * @param path  route the patrol will follow
     */
    public Patrol(String type, float HP, float x, float y, Queue<Vector2> path, String name) {
        super(PatrolType.valueOf(type).getTexture());
        this.type = PatrolType.valueOf(type);
        this.HP = HP;
        this.name = name;
        this.position = new Vector2(x, y);
        this.path = path;
    }

    /**
     * Constructs a basic Patrol with just the type
     * (used by {@link BossPatrol}
     *
     * @param type      used to have predefined attributes
     */
    public Patrol(PatrolType type) {
        super(type.getTexture());
        this.type = type;
        this.HP = type.getMaxHP();
    }

    /**
     * Moves the patrol. It checks it's position relative to the next corner
     * of its rectangular route that it follows
     *
     * @param speed units moved each move
     */
    public void move(double speed) {
        Vector2 nextCorner = path.first();
        if (nextCorner.x > Math.round(position.x * 100.0) / 100.0) {
            position.x += speed;
        } else if (nextCorner.x < Math.round(position.x * 100.0) / 100.0) {
            position.x -= speed;
        } else if (nextCorner.y > Math.round(position.y * 100.0) / 100.0) {
            position.y += speed;
        } else if (nextCorner.y < Math.round(position.y * 100.0) / 100.0) {
            position.y -= speed;
        } else if (nextCorner.x == Math.round(position.x * 100.0) / 100.0 && nextCorner.y == Math.round(position.y * 100.0) / 100.0) {
            cycleQueue();
        }
    }

    /**
     * Moves first to last and removes first in queue
     */
    public void cycleQueue(){
        path.addLast(path.first());
        path.removeFirst();
    }

    /**
    * Draws the mini health indicators relative to the patrol
    *
    * @param shapeMapRenderer  Renderer that the stats are being drawn to (map  dependant)
    */
    public void drawStats(ShapeRenderer shapeMapRenderer) {
        shapeMapRenderer.rect(this.getPosition().x + 0.2f, this.getPosition().y + 1.3f, 0.4f, 0.8f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        shapeMapRenderer.rect(this.getPosition().x + 0.3f, this.getPosition().y + 1.4f, 0.2f, 0.6f, Color.OLIVE, Color.OLIVE, Color.OLIVE, Color.OLIVE);
        shapeMapRenderer.rect(this.getPosition().x + 0.3f, this.getPosition().y + 1.4f, 0.2f, this.getHP() / this.type.getMaxHP() * 0.6f, Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN);
    }

    /**
    * Draws the patrol sprite
    *
    * @param mapBatch  Batch that the patrol is being
    *                  drawn to (map dependant)
    */
    public void drawSprite(Batch mapBatch) {
        mapBatch.draw(this, this.position.x, this.position.y, 1, 1);
    }

    /**
     * Generates the random path the patrol will navigate
     * @param mapW  map width
     * @param mapH  map height
     * @return      path
     */
    public Queue<Vector2> generatePath(int mapW, int mapH) {
        path = new Queue<>();
        for (int i=0; i<4; i++) {
            final int X = new Random().nextInt(mapW);
            final int Y = new Random().nextInt(mapH);
            path.addLast(new Vector2(X, Y));
        }
        return path;
    }

    /**
     * Checks whether the patrol has collided with a truck
     * @param truck     truck to check
     * @param station   check if truck is on bay tile
     * @return          <code>true</code>   patrol collides with truck
     *                  <code>false</code>  otherwise
     */
    public boolean collidesWithTruck(FireTruck truck, FireStation station) {
        return getRoundedPosition().equals(truck.getTilePosition()) && !(truck.isOnBayTile(station) && station.isAlive());
    }

    /**
     * Get position vector, but x and y are rounded to nearest integer
     *
     * @return  rounded vector
     */
    public Vector2 getRoundedPosition() {
        return new Vector2(Math.round(position.x), Math.round(position.y));
    }

    /**
     * Generates the description of the patrol to
     * be stored in the save file
     *
     * @return  description of patrol
     */
    public Desc.Patrol getDescriptor() {
        Desc.Patrol desc = new Desc.Patrol();
        desc.type = this.type.name();
        desc.name = this.name;
        desc.health = this.getHP();
        desc.x = (float) Math.floor(this.getPosition().x);
        desc.y = (float) Math.floor(this.getPosition().y);
        desc.path = this.path;
        return desc;
    }

    public float getHP() {
        return this.HP;
    }

    public void setHP(int hp) { this.HP = hp; }

    public PatrolType getType() {
        return this.type;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public Queue<Vector2> getPath() {
        return this.path;
    }

    public String getName() {
        return this.name;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

}
