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
import com.mozarellabytes.kroy.Utilities.CircularLinkedList;
import com.mozarellabytes.kroy.Utilities.Node;

import com.mozarellabytes.kroy.Utilities.SoundFX;

import java.util.ArrayList;
import java.util.LinkedList;


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

    public Vector2 position;


/** Actual path the truck follows; the fewer item in
     * the path the slower the truck will go */

    public final CircularLinkedList path;


    public final Queue<Vector2> trailPath;


    public Node current;

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

    private final Texture texture;
    //private final Texture lookRight;
    //private final Texture lookUp;
    //private final Texture lookDown;


/**
     * Constructs a new FireTruck at a position and of a certain type
     * which have been passed in
     *
     * @param gameScreen    used to access functions in GameScreen
     * @param type          used to have predefined attributes
     */

    public Patrols(GameScreen gameScreen, PatrolType type) {
        super(type.getTexture());


        this.gameScreen = gameScreen;
        this.type = type;
        this.HP = type.getMaxHP();
        this.position = new Vector2(type.getPoint1().x + 1,type.getPoint1().y);
        this.path = new CircularLinkedList();
        this.trailPath = new Queue<Vector2>();
        this.moving = true;
        this.attacking = false;
        this.inCollision = false;
        this.spray = new ArrayList<WaterParticle>();
        this.timeOfLastAttack = System.currentTimeMillis();
        this.nextTile=position;
        this.previousTile=position;
        this.texture = type.getTexture();
        //this.lookRight = new Texture(Gdx.files.internal("sprites/Patrol/patrol.png"));
        //this.lookUp = new Texture(Gdx.files.internal("sprites/Patrol/patrol.png"));
        //this.lookDown = new Texture(Gdx.files.internal("sprites/Patrol/patrol.png"));

        definePath();
    }


    /**
     * Called every tick and updates the paths to simulate the truck moving along the
     * path
     */


    public void definePath(){
        addTileToPath(this.position,type.getPoint1());

        boolean fullCycle=false;
        int counter=0;

        while(!fullCycle){
            if(this.type.getTarget().x>this.position.x){
                nextTile.x=this.position.x+1;
            }
            else if(this.type.getTarget().y>this.position.y){
                nextTile.y=this.position.y+1;
            }
            else if(this.type.getTarget().x<this.position.x){
                nextTile.x=this.position.x-1;
            }
            else if(this.type.getTarget().y<this.position.y) {
                nextTile.y=this.position.y-1;
            }
            else{
                if(this.position.equals(type.getPoint2())){
                    type.setTarget(type.getPoint3());
                    System.out.println("got point2");
                }
                else if(this.position.equals(type.getPoint3())){
                    type.setTarget(type.getPoint4());
                    System.out.println("got point3");
                }
                else if(this.position.equals(type.getPoint4())){
                    type.setTarget(type.getPoint1());
                    System.out.println("got point4");

                }
                else{
                    type.setTarget(type.getPoint2());
                    System.out.println("got point1");
                    counter++;
                    if(counter==2){
                        fullCycle=true;
                    }
                }
            }
            addTileToPath(this.position, previousTile);
        }
        //addTileToPath(this.target, this.position);
        current=path.getHead();
    }

    public void addTileToPath(Vector2 coordinate, Vector2 previous) {
            //Vector2 previous = this.path.last();
            int interpolation = (int) (20/type. getSpeed());
            for (int i=1; i<interpolation; i++) {
                this.path.addNode(new Vector2((((previous.x - coordinate.x)*-1)/interpolation)*i + previous.x, (((previous.y - coordinate.y)*-1)/interpolation)*i + previous.y));
            }
        previousTile=new Vector2(((int) coordinate.x), ((int) coordinate.y));
        this.path.addNode(previousTile);
    }

    public void move() {
        //path.traverseList();
        if (moving) {
            Node next=path.getNext(current);
            Vector2 nextTile = path.getData(next);

            this.position = nextTile;

            //changeSprite(nextTile);

            /*if (!this.inCollision) {
                changeSprite(nextTile);
            }
            else{
                moving=false;
            }*/
            current=next;
            previousTile = nextTile;

        }
    }

/**
     * Changes the direction of the truck depending on the previous tile and the next tile
     *
     * @param nextTile  first tile in the queue (next to be followed)
     */

    /*private void changeSprite(Vector2 nextTile) {
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
*/
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
        shapeMapRenderer.rect(this.getPosition().x + 0.2f, this.getPosition().y + 1.3f, 0.4f, 0.8f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        shapeMapRenderer.rect(this.getPosition().x + 0.3f, this.getPosition().y + 1.4f, 0.2f, 0.6f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        shapeMapRenderer.rect(this.getPosition().x + 0.3f, this.getPosition().y + 1.4f, 0.2f, this.getHP() / this.type.getMaxHP() * 0.6f, Color.RED, Color.RED, Color.RED, Color.RED);
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

    public boolean getAttacking(){
        return this.attacking;
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

    public CircularLinkedList getPath() {
        return this.path;
    }


    public boolean getMoving() {
        return this.moving;
    }

    public boolean withinRange(Vector2 targetPos) {
        return targetPos==this.position;
    }
}
