package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.Descriptors.Desc;
import com.mozarellabytes.kroy.Utilities.Constants;

import java.util.ArrayList;

/**
 * Special Patrol which is the Boss. This spawns
 * when a certain number (depending on difficulty)
 * of fortresses have been destroyed and will head
 * towards your Fortress.
 */
public class BossPatrol extends Patrol {

    private Vector2 shootingPosition;

    /**
     * List of particles that the patrol uses to attack
     * a Fortress
     */
    private ArrayList<Particle> spray;

    /**
     * Constructs a new special Boss Patrol
     *
     * @param type   used to have predefined attributes
     * @param source where the boss starts
     * @param target where the boss is heading towards
     */
    public BossPatrol(PatrolType type, Vector2 source, Vector2 target) {
        super(type);
        this.name = "Boss";
        this.shootingPosition = new Vector2();
        this.path = generatePath(source, target);
        this.position = new Vector2(path.get(0).x, path.get(0).y);
        this.spray = new ArrayList<>();
    }

    /**
     * Constructs a special Boss Patrol from a save file
     *
     * @param type  used to have predefined attributes
     * @param HP    initial health of patrol
     * @param x     initial x position of patrol
     * @param y     initial y position of patrol
     * @param path  route the patrol will follow
     */
    public BossPatrol(String type, float HP, float x, float y, Queue<Vector2> path, String name) {
        super(type, HP, x, y, path, name);
        this.spray = new ArrayList<>();
        this.shootingPosition = path.get(0);
    }

    /**
     * Generates a special path for the boss
     * @param source    where the boss starts
     * @param target    where the boss is heading towards
     * @return          path the boss will take
     */
    private Queue<Vector2> generatePath(Vector2 source, Vector2 target) {
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
     * Draws the spray when attacking the fire station
     *
     * @param shapeMapRenderer  render to
     */
    public void drawSpray(ShapeRenderer shapeMapRenderer) {
        for (Particle particle : this.getSpray()) {
            shapeMapRenderer.rect(particle.getPosition().x, particle.getPosition().y, particle.getSize(), particle.getSize(), particle.getColour(), particle.getColour(), particle.getColour(), particle.getColour());
        }
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
     * Gets the position where the spray should appear like it
     * is coming out of the patrol. Need to divide by tile width
     * as width is in pixels not tiles
     *
     * @return  where spray comes out
     */
    private Vector2 getSprayHole() {
        return new Vector2(this.getDoublePosition().x + this.getWidth()/(Constants.TILE_WxH * 2f), this.getDoublePosition().y);
    }

    private void removeParticle(Particle particle) {
        this.spray.remove(particle);
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
     * Deals damage to Firestation by generating a BlasterParticle and adding
     * it to the spray
     *
     * @param station FireStation being attacked
     */
    public void attack(FireStation station) {
        this.spray.add(new Particle(this.getSprayHole(), station.getCentrePosition(), station));
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

    private ArrayList<Particle> getSpray() {
        return this.spray;
    }

}
