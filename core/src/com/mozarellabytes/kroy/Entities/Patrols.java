
package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.Screens.GameScreen;
import com.mozarellabytes.kroy.Utilities.SoundFX;

import java.util.ArrayList;


/**
 * FireTruck is an entity that the player controls. It navigates the map on the
 * roads defined in the Tiled Map by following a path that the user draws.
 *
 * Having 'A' held when within range of a  Fortress will deal damage to it.
 */

public class Patrols extends Sprite {


/** Enables access to functions in GameScreen */

    private final GameScreen gameScreen;


/** Defines set of pre-defined attributes */

    public final PatrolType type;


/** Health points */

    private float HP;


/** Position of FireTruck in tiles */

    private Vector2 position;


/** Actual path the truck follows; the fewer item in
     * the path the slower the truck will go */

    public final Queue<Vector2> path;


/** If the truck is currently moving, determines whether the
     * truck's position should be updated
     *
     * <code>true</code> once the player has drawn a
     * path and has let go of the mouse click
     * <code>false</code> once the truck has got to
     * the end of the path */

    private boolean moving;


/** If the truck is attacking a Fortress
     *
     * <code>true</code> 'A' key is pressed
     * <code>false</code> 'A' key is not pressed */

    private boolean attacking;


/** Whether the truck has an unresolved collision
     * with another truck */

    private boolean inCollision;


/** Used to check if the truck's image should be
     * changed to match the direction it is facing */

    private Vector2 previousTile;

    private Vector2 nextTile;


    /** Time since fortress has attacked the truck */

    private long timeOfLastAttack;


    private Vector2 target;

    /** List of particles that the truck uses to attack
     * a Fortress */

    private final ArrayList<WaterParticle> spray;


/** Texture for each direction the
     * truck is facing */

    private final Texture lookLeft;
    private final Texture lookRight;
    private final Texture lookUp;
    private final Texture lookDown;


/**
     * Constructs a new FireTruck at a position and of a certain type
     * which have been passed in
     *
     * @param gameScreen    used to access functions in GameScreen
     * @param position      initial location of the truck
     * @param type          used to have predefined attributes
     */

    public Patrols(GameScreen gameScreen, Vector2 position, PatrolType type) {
        super(new Texture(Gdx.files.internal("sprites/firetruck/down.png")));


        this.gameScreen = gameScreen;
        this.type = type;
        this.HP = type.getMaxHP();
        this.position = position;
        this.path = new Queue<Vector2>();
        this.moving = false;
        this.attacking = false;
        this.inCollision = false;
        this.spray = new ArrayList<WaterParticle>();
        this.timeOfLastAttack = System.currentTimeMillis();
        this.nextTile=position;
        this.previousTile=position;
        this.target=type.getPoint2();
        this.lookLeft = new Texture(Gdx.files.internal("sprites/firetruck/left.png"));
        this.lookRight = new Texture(Gdx.files.internal("sprites/firetruck/right.png"));
        this.lookUp = new Texture(Gdx.files.internal("sprites/firetruck/up.png"));
        this.lookDown = new Texture(Gdx.files.internal("sprites/firetruck/down.png"));
    }


    /**
     * Called every tick and updates the paths to simulate the truck moving along the
     * path
     */

   /* public void move() {
        if (this.path.size > 0) {
            Vector2 nextTile = path.first();
            this.position = nextTile;
            previousTile = nextTile;
            path.removeFirst();
        } else {
            moving = false;
        }
    }*/

    public void cycle(){
        if(this.position==type.getPoint2()){
            target=type.getPoint3();
        }
        else if(this.position==type.getPoint3()){
            target=type.getPoint4();
        }
        else if(this.position==type.getPoint4()){
            target=type.getPoint1();
        }
        else if(this.position==type.getPoint1()){
            target=type.getPoint2();
        }
    }

    public void move(){
            //position.x=this.position.x+1;
            //position.y=this.position.y+1;

            previousTile=position;
            if(target.x>this.position.x){
                position.x=this.position.x+1;
                nextTile.x=position.x;
            }
            else if(target.y>this.position.y){
                position.y=this.position.y+1;
                nextTile.y=position.y;
            }
            else if(target.x<this.position.x){
                position.x=this.position.x-1;
                nextTile.x=position.x;
            }
            else if(target.y<this.position.y){
                position.y=this.position.y-1;
                nextTile.y=position.y;
            }
        cycle();
    }

/**
     * Changes the direction of the truck depending on the previous tile and the next tile
     *
     * @param nextTile  first tile in the queue (next to be followed)
     */

    private void changeSprite(Vector2 nextTile) {
        if (previousTile != null) {
            if (nextTile.x > previousTile.x) {
                setTexture(lookRight);
            } else if (nextTile.x < previousTile.x) {
                setTexture(lookLeft);
            } else if (nextTile.y > previousTile.y) {
                setTexture(lookUp);
            } else if (nextTile.y < previousTile.y) {
                setTexture(lookDown);
            }
        }
    }

/**
     * Deals damage to Fortress by generating a WaterParticle and adding
     * it to the spray
     *
     * @param fireTruck Firetruck being attacked
     */

    public void attack(FireTruck fireTruck) {
        //
    }


/**
     * Called every tick to check if a Fortress is within the range of
     *  the truck
     *
     * @param firetruck  Fortress' position being checked
     * @return          <code>true</code> if Fortress is within range
     *                  <code>false </code> otherwise
     */

    public boolean firetruckInRange(Vector2 firetruck) {
        return this.getVisualPosition().dst(firetruck) <= this.type.getRange();
    }

/**
     * Draws the mini health indicators relative to the truck
     *
     * @param shapeMapRenderer  Renderer that the stats are being drawn to (map  dependant)
     */

    public void drawStats(ShapeRenderer shapeMapRenderer) {
        shapeMapRenderer.rect(this.getPosition().x - 0.26f, this.getPosition().y + 1.4f, 0.6f, 1.2f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        shapeMapRenderer.rect(this.getPosition().x - 0.13f, this.getPosition().y + 1.5f, 0.36f, 1f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        shapeMapRenderer.rect(this.getPosition().x - 0.13f, this.getPosition().y + 1.5f, 0.36f, this.getHP() / this.type.getMaxHP() * 1f, Color.RED, Color.RED, Color.RED, Color.RED);
    }


/**
     * Draws the FireTruck sprite
     *
     * @param mapBatch  Batch that the truck is being
     *                  drawn to (map dependant)
     */

    public void drawSprite(Batch mapBatch) {
        mapBatch.draw(this, this.position.x, this.position.y, 1, 1);
    }


/**
     * Helper method that returns where the truck is visually to the player. This is used when
     * checking the range when attacking the Fortress and getting attacked by the Fortress
     *
     * @return a vector where the truck is visually
     */

    public Vector2 getVisualPosition() {
        return new Vector2(this.position.x + 0.5f, this.position.y + 0.5f);
    }


/**
     * Sets time of last attack to unix timestamp provided
     * @param timestamp  timestamp set as time of last attack
     */

    public void setTimeOfLastAttack(long timestamp) {
        this.timeOfLastAttack = timestamp;
    }

    public void setAttacking(boolean b) {
        this.attacking = b;
    }

    public void setMoving(boolean t) {
        this.moving = t;
    }

    public long getTimeOfLastAttack() {
        return timeOfLastAttack;
    }

    public float getHP() {
        return this.HP;
    }


    public PatrolType getType() {
        return this.type;
    }

    public void setCollision() {
        this.inCollision = true;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public Queue<Vector2> getPath() {
        return this.path;
    }


    public boolean getMoving() {
        return this.moving;
    }
}



