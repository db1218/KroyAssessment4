package com.mozarellabytes.kroy.Entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.Entities.FireStation;
import com.mozarellabytes.kroy.Screens.GameScreen;
import com.mozarellabytes.kroy.Utilities.Node;

import com.mozarellabytes.kroy.Utilities.SoundFX;

import java.util.ArrayList;


/**
 * Patrol is an entity that the player controls. It navigates the map
 * going through points defined in the the patrolType
 *
 *
 */

public class Patrol extends Sprite {


    /** Enables access to functions in GameScreen */
    private final GameScreen gameScreen;

    /** Defines set of pre-defined attributes */
    public final PatrolType type;

    /**
     * path the patrol follows; the fewer item in
     * the path the slower the patrol will go
     */
    private final Queue<Vector2> path;

    /** Health points */
    private float HP;

    /** Position of patrol in tiles */
    public Vector2 position;

    /**
     * Used to check if the patrol's image should be
     * changed to match the direction it is facing
     */
    private Vector2 previousTile;

    private Vector2 nextTile;


    /**
     * List of particles that the patrol uses to attack
     * a Fortress
     */
    private final ArrayList<BlasterParticle> spray;

    /**
     * Constructs a new Patrol at a position and of a certain type
     * which have been passed in
     *
     * @param gameScreen    used to access functions in GameScreen
     * @param type          used to have predefined attributes
     */
    public Patrol(GameScreen gameScreen, PatrolType type) {
        super(type.getTexture());
        this.gameScreen = gameScreen;
        this.type = type;
        this.HP = type.getMaxHP();
        this.position = new Vector2(type.getPoint1().x + 1, type.getPoint1().y);
        this.path = new Queue<>();
        this.spray = new ArrayList<BlasterParticle>();
        this.nextTile = position;
        this.previousTile = position;
        definePath();
    }


    /**
     * Called every tick and updates the paths to simulate the patrol moving along the
     * path
     */
    public void definePath() {
        addTileToPath(this.position,type.getPoint1());

        boolean fullCycle = false;
        int counter = 0;

        while (!fullCycle){
            if (this.type.getTarget().x>this.position.x) nextTile.x = this.position.x+1;
            else if (this.type.getTarget().y>this.position.y) nextTile.y = this.position.y+1;
            else if (this.type.getTarget().x<this.position.x) nextTile.x = this.position.x-1;
            else if (this.type.getTarget().y<this.position.y) nextTile.y = this.position.y-1;
            else {
                if (this.position.equals(type.getPoint2())) type.setTarget(type.getPoint3());
                else if (this.position.equals(type.getPoint3())) type.setTarget(type.getPoint4());
                else if (this.position.equals(type.getPoint4())) type.setTarget(type.getPoint1());
                else {
                    type.setTarget(type.getPoint2());
                    counter++;
                    if (counter==2) fullCycle = true;
                }
            }
            addTileToPath(this.position, previousTile);
        }
    }

    public void addTileToPath(Vector2 coordinate, Vector2 previous) {
        int interpolation = (int) (90/type.getSpeed());
        for (int i=1; i<interpolation; i++) {
            this.path.addLast(new Vector2((((previous.x - coordinate.x)*-1)/interpolation)*i + previous.x, (((previous.y - coordinate.y)*-1)/interpolation)*i + previous.y));
        }
        previousTile = new Vector2(((int) coordinate.x), ((int) coordinate.y));
        this.path.addLast(previousTile);
    }

    public void move(){
        this.position = getFirstInQueue();
        updateQueue();
    }

    private void updateQueue(){
        this.path.addLast(getFirstInQueue());
        this.path.removeFirst();
    }

    private Vector2 getFirstInQueue(){
        return this.path.first();
    }
    
    /**
    * Deals damage to Firestation by generating a BlasterParticle and adding
    * it to the spray
    *
    * @param station FireStation being attacked
    */
    public void attack(FireStation station) {
        this.spray.add(new BlasterParticle(this, station));
    }

    public void updateSpray() {
        if (this.spray != null) {
            for (int i=0; i < this.spray.size(); i++) {
                BlasterParticle particle = this.spray.get(i);
                particle.updatePosition();
                if (particle.isHit()) {
                    this.damage(particle);
                    this.removeParticle(particle);
                }
            }
        }
    }

    private ArrayList<BlasterParticle> getSpray() {
        return this.spray;
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
        for (BlasterParticle particle : this.getSpray()) {
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

    private void removeParticle(BlasterParticle particle) {
        this.spray.remove(particle);
    }

    /**
     * Damages the Station depending on the patrol's AP
     *
     * @param particle  the particle which damages the station
     */
    private void damage(BlasterParticle particle) {
        particle.getTarget().damage(0.15f);
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
}
