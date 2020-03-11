package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.Descriptors.Desc;

import java.util.ArrayList;

/**
 * Patrol is an entity that the player controls. It navigates the map
 * going through points defined in the the patrolType
 *
 *
 */
public class Patrol extends Sprite {

    /** Defines set of pre-defined attributes */
    public PatrolType type;

    /**
     * path the patrol follows; the fewer item in
     * the path the slower the patrol will go
     */
    private Queue<Vector2> path;

    /** Health points */
    private float HP;

    /** Position of patrol in tiles */
    public Vector2 position;

    /**
     * List of particles that the patrol uses to attack
     * a Fortress
     */
    private ArrayList<Particle> spray;

    /**
     * Constructs a Patrol from save at a position and of a certain type
     * which have been passed in
     *
     * @param type          used to have predefined attributes
     */
    public Patrol(PatrolType type) {
        super(type.getTexture());
        this.type = type;
        this.HP = type.getMaxHP();
        this.position = new Vector2(type.getPoints().get(0).x, type.getPoints().get(0).y);
        this.path = type.getPoints();
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
    public Patrol(String type, float HP, float x, float y, Queue<Vector2> path) {
        super(PatrolType.valueOf(type).getTexture());
        this.type = PatrolType.valueOf(type);
        this.HP = HP;
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
        this.spray.add(new Particle(this.getPosition(), station.getCentrePosition(), station));
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
     * Get vector, but x and y are rounded to doubles instead of floats
     * @return  new Vector
     */
    public Vector2 getDoublePosition() {
        return new Vector2((float) (Math.round(position.x * 100.0) / 100.0), (float) (Math.round(position.y * 100.0) / 100.0));
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

}
