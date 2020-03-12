package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.Descriptors.Desc;
import com.mozarellabytes.kroy.Utilities.Constants;

import java.lang.invoke.VolatileCallSite;
import java.util.ArrayList;
import java.util.Random;

/**
 * Patrol is an entity that the player controls. It navigates the map
 * going through points defined in the the patrolType
 *
 *
 */
public class Patrol extends Sprite {

    /** Defines set of pre-defined attributes */
    public final PatrolType type;

    /**
     * path the patrol follows; the fewer item in
     * the path the slower the patrol will go
     */
    private Queue<Vector2> path;

    /** Health points */
    private float HP;

    /** Position of patrol in tiles */
    public final Vector2 position;

    /**
     * List of particles that the patrol uses to attack
     * a Fortress
     */
    private final ArrayList<Particle> spray;

    private Vector2 shootingPosition;

    /**
     * Unique name of the patrol
     */
    private final String name;

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
        this.spray = new ArrayList<>();
    }

    /**
     * Constructs a new special Boss Patrol
     *
     * @param type      used to have predefined attributes
     * @param source    where the boss starts
     * @param target    where the boss is heading towards
     */
    public Patrol(PatrolType type, Vector2 source, Vector2 target) {
        super(type.getTexture());
        this.type = type;
        this.name = "Boss";
        this.HP = type.getMaxHP();
        this.shootingPosition = new Vector2();
        this.path = generateBossPath(source, target);
        this.position = new Vector2(path.get(0).x, path.get(0).y);
        this.spray = new ArrayList<>();
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
        this.spray = new ArrayList<>();
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
    private void cycleQueue(){
        path.addLast(path.first());
        path.removeFirst();
    }

    /**
    * Deals damage to Firestation by generating a BlasterParticle and adding
    * it to the spray
    *
    * @param station FireStation being attacked
    */
    public void attack(FireStation station) {
        this.spray.add(new Particle(this.getSprayHole(), station.getCentrePosition(), station));
    }

    /**
     * Updates the spray of the Boss patrol
     */
    public void updateBossSpray() {
        if (this.spray != null) {
            for (int i=0; i < this.spray.size(); i++) {
                Particle particle = this.spray.get(i);
                particle.updatePosition();
                if (particle.isHit()) {
                    this.damage(particle);
                    this.removeParticle(particle);
                }
            }
        }
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
        for (Particle particle : this.getSpray()) {
            shapeMapRenderer.rect(particle.getPosition().x, particle.getPosition().y, particle.getSize(), particle.getSize(), particle.getColour(), particle.getColour(), particle.getColour(), particle.getColour());
        }
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

    private void removeParticle(Particle particle) {
        this.spray.remove(particle);
    }

    /**
     * Damages the Station depending on the patrol's AP
     *
     * @param particle  the particle which damages the station
     */
    private void damage(Particle particle) {
        FireStation station = (FireStation) particle.getTarget();
        station.damage(0.15f);
    }

    /**
     * Generates the random path the patrol will navigate
     * @param mapW  map width
     * @param mapH  map height
     * @return      path
     */
    private Queue<Vector2> generatePath(int mapW, int mapH) {
        path = new Queue<>();
        for (int i=0; i<4; i++) {
            int x = new Random().nextInt(mapW);
            int y = new Random().nextInt(mapH);
            path.addLast(new Vector2(x, y));
        }
        return path;
    }

    /**
     * Generates a special path for the boss
     * @param source    where the boss starts
     * @param target    where the boss is heading towards
     * @return          path the boss will take
     */
    private Queue<Vector2> generateBossPath(Vector2 source, Vector2 target) {
        shootingPosition = new Vector2();
        if (source.x > target.x) shootingPosition.x = Math.round(target.x) + 2;
        else shootingPosition.x = Math.round(target.x) - 2;

        if (source.y > target.y) shootingPosition.y = Math.round(target.y) + 2;
        else shootingPosition.y = Math.round(target.y) - 2;

        path = new Queue<>();
        path.addLast(new Vector2(Math.round(source.x), Math.round(source.y)));
        path.addLast(shootingPosition);
        return path;
    }

    /**
     * Gets the position where the spray should appear like it
     * is coming out of the patrol. Need to divide by tile width
     * as width is in pixels not tiles
     *
     * @return  where spray comes out
     */
    private Vector2 getSprayHole() {
        return new Vector2(this.getDoublePosition().x + this.getWidth()/(Constants.TILE_WxH * 2f), this.getDoublePosition().y);
    }

    /**
     * Get vector, but x and y are rounded to doubles instead of floats
     * @return  new Vector
     */
    public Vector2 getDoublePosition() {
        return new Vector2((float) (Math.round(position.x * 100.0) / 100.0), (float) (Math.round(position.y * 100.0) / 100.0));
    }

    /**
     * Whether the shooting pos and current pos are equal
     * @return  <code>true</code> patrol is at shooting pos
     *          <code>false</code> otherwise
     */
    public boolean inShootingPosition() {
        return getDoublePosition().equals(shootingPosition);
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

    private ArrayList<Particle> getSpray() {
        return this.spray;
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
}
