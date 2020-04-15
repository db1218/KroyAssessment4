package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Descriptors.Desc;
import com.mozarellabytes.kroy.Screens.GameScreen;
import com.mozarellabytes.kroy.Utilities.SavedElement;
import com.mozarellabytes.kroy.Utilities.SoundFX;

import java.util.ArrayList;

/**
 * FireStation is a class created when it is called from GameScreen.
 * This class contains the location and sprite of the FireStation.
 * The FireStation spawns, repairs and refills the firetrucks and
 * ensures that trucks do not collide
 */
public class FireStation {

    /**
     * Game screen
     */
    private GameScreen gameScreen;

    /**
     * Coordinates and dimensions of the FireStation in the game screen
     * in tiles
     */
    private final float x, y;
    private int w;
    private int h;

    /** A tile inside the station where a truck can be repaired and refilled */
    private final ArrayList<Vector2> bayTiles;

    /** The sprite image for the station */
    private final Texture texture;
    private final Texture deadTexture;

    /** List of active fire trucks
     * @link FireTruck */
    private final ArrayList<FireTruck> trucks;

    /**
     * Health of the fortress
     */
    private float HP;
    private float maxHP;

    /**
     * Constructs the Firestation with at a given position with locations
     * for the repair and refill tiles and the spawn tiles.
     *
     * @param x     x coordinate of Station in tiles (lower left point)
     * @param y     y coordinate of Station in tiles (lower left point)
     * @param HP    starting health of the fire station
     */
    public FireStation(float x, float y, float HP) {
        this.x = x;
        this.y = y;
        this.w = 6;
        this.h = 3;
        this.HP = HP;
        this.maxHP = 100;
        this.bayTiles = new ArrayList<>();
        for (int i=0; i<4; i++) bayTiles.add(new Vector2(x + i + 1, y));
        this.texture = new Texture(Gdx.files.internal("sprites/station/extended_station.png"));
        this.deadTexture = new Texture(Gdx.files.internal("sprites/fortress/fortress_revs_dead.png")); // change me pls
        this.trucks = new ArrayList<>();
    }

    /**
     * Creates a fire truck of type specified from FireTruckType. It signals to
     * the game state that a truck has been created and add the truck to the
     * arraylist this.truck so the game screen can iterate through all active trucks
     *
     * @param truck         truck to add to the arrayList of active trucks
     */
    public void spawn(FireTruck truck) {
        this.trucks.add(truck);
    }

    /**
     * Draw the fire station health above the fire station
     *
     * @param shapeMapRenderer  renderer to draw to
     */
    public void drawStats(ShapeRenderer shapeMapRenderer) {
        shapeMapRenderer.rect(this.getPosition().x + 2.76f, this.getPosition().y + 2.9f, 0.55f, 1.2f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        shapeMapRenderer.rect(this.getPosition().x + 2.86f, this.getPosition().y + 3f, 0.34f, 1f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        shapeMapRenderer.rect(this.getPosition().x + 2.86f, this.getPosition().y + 3f, 0.34f, this.getHP() / this.getMaxHP() * 1f, Color.RED, Color.RED, Color.RED, Color.RED);
    }

    /**
     * Calls the repair and refill methods. When a truck is within the station
     * (the trucks position is the same as one of the station's bay tiles) the
     * truck will repair and refill at the same time.
     */
    public void restoreTrucks() {
        for (FireTruck truck : this.trucks) {
            if (this.HP > 0) {
                if (truck.isOnBayTile(this)) {
                    repair(truck);
                    refill(truck);
                }
            }
        }
    }

    /**
     * Increments the truck's HP until the truck's HP equals the truck's maximum
     * HP
     * @param truck truck that is being refilled
     */
    private void refill(FireTruck truck) {
        if (truck.getReserve() < truck.type.getMaxReserve()) {
            truck.refill(Math.min(0.6f, truck.getType().getMaxReserve() - truck.getReserve()));
        }
    }

    /**
     * Increments the truck's reserve until the truck's reserve equals the
     * truck's maximum reserve
     * @param truck truck that is being repaired
     */
    private void repair(FireTruck truck) {
        if (truck.getHP() < truck.type.getMaxHP()) {
            truck.repair(Math.min(0.4f, truck.getType().getMaxHP() - truck.getHP()));
        }
    }

    /**
     * Called when a truck's HP reaches 0, it removes the truck from the
     * array list of active trucks and the game screen.
     *
     * @param truck truck that is being removed from the arrayList of active trucks
     */
    public void destroyTruck(FireTruck truck) {
        this.trucks.remove(truck);
    }

    /**
     * Reduce health of fire station by HP
     * @param HP    amount to reduce health by
     */
    public void damage(float HP){
        this.HP -= HP;
    }

    /**
     * Checks that no more than one truck occupies a tile at a time by checking trucks
     * are not moving towards each other and that a moving truck is not going to go onto
     * the same tile as a stationary truck. If two trucks are going to collide reset
     * trucks is called.
     */
    public void checkForCollisions() {
        for (FireTruck truck1 : trucks) {
            for (FireTruck truck2 : trucks) {
                if (!truck1.equals(truck2) &&
                        !truck1.pathSegments.isEmpty() &&
                        !truck1.pathSegments.first().isEmpty()) {
                    Vector2 truck1tile = new Vector2((float)Math.floor(truck1.getPosition().x),(float)Math.floor(truck1.getPosition().y));
                    Vector2 truck2tile = new Vector2((float)Math.floor(truck2.getPosition().x),(float)Math.floor(truck2.getPosition().y));
                    if (truck1.pathSegments.first().first().equals(truck2tile)) {
                        truck1.setPosition(truck1tile);
                        truck2.setPosition(truck2tile);
                        resetTrucks(truck1, truck2);
                    }
                }
            }
        }
    }

    /** Resets two trucks - is called when both trucks are moving towards each other
     * It removes their paths so they halt on the tile of the collision. It then adds
     * the nearest tile to their path, the trucks move to this tile so that after the
     * collision the trucks are positioned at the centre of adjacent tiles.
     *
     * @param truck1 one truck involved in the collision
     * @param truck2 the second truck involved in the collision
     */
    private void resetTrucks(FireTruck truck1, FireTruck truck2) {
        if (SoundFX.music_enabled) {
            SoundFX.sfx_horn.play();
        }
        truck1.collided();
        truck2.collided();
    }

    /** Draws the firetruck to the gameScreen
     * @param mapBatch batch being used to render to the gameScreen */
    public void draw(Batch mapBatch) {
        mapBatch.draw(this.texture, this.x, this.y, this.w, this.h);
    }

    /** Checks if the user has drawn more than one truck to the same end tile.
     *
     * @return <code> true </code> If more than one truck has the same end tile
     *      * <code> false </code> Otherwise
     */
    public boolean doTrucksHaveSameLastTile() {
        for (FireTruck truck : this.getTrucks()) {
            if (!truck.equals(gameScreen.getSelectedTruck())) {
                if (!truck.pathSegments.isEmpty() && !truck.pathSegments.last().isEmpty()) {
                    return truck.pathSegments.last().last().equals(gameScreen.getSelectedTruck().pathSegment.last());
                }
            }
        }
        return false;
    }

    /**
     * Just as game screen is being unfrozen,
     * the truck's segment stacks are cleared
     * and their new paths are calculated
     */
    public void recalculateTruckPaths() {
        for (FireTruck truck : trucks) {
            truck.clearPathSegmentsStack();
            truck.generatePathFromAllSegments();
        }
    }

    /**
     * @return the destroyed texture object
     */
    public DestroyedEntity getDestroyedStation(){
        return new DestroyedEntity(this.deadTexture, this.x, this.y, 5, 3);
    }

    /**
     * Gets the middle of the fire station
     * @return  Centred vector
     */
    public Vector2 getCentrePosition() {
        return new Vector2(this.x+this.w/2f,this.y+this.h/2f);
    }

    /**
     * Generates the description of the fire trucks
     * to be stored in the save file
     *
     * @return  description of the fire trucks
     */
    public Desc.FireTruck[] getFireTrucksDescriptor() {
        Desc.FireTruck[] fireTrucks = new Desc.FireTruck[this.getTrucks().size()];
        for (int i=0; i<fireTrucks.length; i++) {
            fireTrucks[i] = this.getTruck(i).getDescriptor();
        }
        return fireTrucks;
    }

    /**
     * Generates the description of the fire station to
     * be stored in the save file
     *
     * @return  description of fire station
     */
    public Desc.FireStation getDescriptor() {
        Desc.FireStation desc = new Desc.FireStation();
        desc.x = this.x;
        desc.y = this.y;
        desc.health = this.HP;
        return desc;
    }

    /**
     * Set game screen object when loaded from a save
     * file as dont want to generate screen in
     * {@link SavedElement}
     *
     * @param gameScreen    gameScreen to set
     */
    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        for (FireTruck truck : this.trucks)
            truck.setGameScreen(gameScreen);
    }

    /**
     * if the fire station is alive
     * @return  <code>true</code> if hp is greater than 0
     *          <code>false</code> otherwise
     */
    public boolean isAlive() {
        return HP > 0;
    }

    public float getHP() {
        return this.HP;
    }

    public ArrayList<Vector2> getBayTiles() { return this.bayTiles; }

    public ArrayList<FireTruck> getTrucks() {
        return this.trucks;
    }

    public FireTruck getTruck(int i) {
        return this.trucks.get(i);
    }

    public Vector2 getPosition() {
        return new Vector2(this.x,this.y);
    }

    public Rectangle getArea() {
        return new Rectangle(x, y, w, h);
    }

    public float getMaxHP() {
        return this.maxHP;
    }

    public void setDimensions(int w, int h) {
        this.w = w;
        this.h = h;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }
}