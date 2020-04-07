package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.Descriptors.Desc;
import com.mozarellabytes.kroy.Screens.GameScreen;
import com.mozarellabytes.kroy.Utilities.SoundFX;

import java.util.*;

/**
 * FireTruck is an entity that the player controls. It navigates the map on the
 * roads defined in the Tiled Map by following a path that the user draws.
 *
 * Having 'A' held when within range of a  Fortress will deal damage to it.
 */
public class FireTruck extends Sprite {

    /** Enables access to functions in GameScreen */
    private GameScreen gameScreen;

    /** Defines set of pre-defined attributes */
    public final FireTruckType type;

    /** Health points */
    private float HP;

    /** Water Reserve */
    private float reserve;

    /** Position of FireTruck in tiles */
    private Vector2 position;

    /** Actual path the truck follows; the fewer item in
     * the path the slower the truck will go */
    public Queue<Vector2> path;

    /**
     * The visual path that users can see when drawing
     * the truck's route, this is the path finding path
     */
    public Queue<Vector2> pathSegment;

    /**
     * Queue of individual path segments
     */
    public Queue<Queue<Vector2>> pathSegments;

    /**
     * Stack of path segments, keeps track of when segments
     * are added, therefore allows for undo and redos
     */
    public Stack<Queue<Vector2>> pathSegmentsStack;

    /** If the truck is currently moving, determines whether the
     * truck's position should be updated
     *
     * <code>true</code> once the player has drawn a
     * path and has let go of the mouse click
     * <code>false</code> once the truck has got to
     * the end of the path */
    private boolean moving;

    /** Whether the truck has an unresolved collision
     * with another truck */
    private boolean inCollision;

    /** Used to check if the truck's image should be
     * changed to match the direction it is facing */
    private Vector2 previousTile;

    /** Time since fortress has attacked the truck */
    private long timeOfLastAttack;

    /** List of particles that the truck uses to attack
     * a Fortress */
    private ArrayList<Particle> spray;

    /** Whether the mouse has been dragged off a road tile */
    private boolean dragOffMap = false;
    /** Whether the path finding algorithm has reached the end*/
    boolean reachedEnd = false;
    /** All possible routes from an end tile to a new tile are stored in this */
    Queue<Vector2> positions;
    /** Current tile */
    Vector2 currentPos;
    /**  Shows all possible movement directions for a firetruck*/
    final int[] directionX = {-1, 1, 0, 0};
    final int[] directionY = {0, 0, 1, -1};

    /** True if a tile has been visited when constructing a path, false otherwise */
    boolean[][] vistited;
    /** Links parents to children in order o find the shortest path */
    Vector2[] prev;
    /** the shortest path between 2 points */
    LinkedList<Vector2> reconstructedPath;

    private boolean inShield;

    private float AP;
    private float range;
    private String rotation;

    /**
     * Constructs a new FireTruck at a position and of a certain type
     * which have been passed in
     *
     * @param gameScreen    used to access functions in GameScreen
     * @param position      initial location of the truck
     * @param type          used to have predefined attributes
     */
    public FireTruck(GameScreen gameScreen, Vector2 position, FireTruckType type) {
        super(type.getLookDown());
        this.type = type;
        this.HP = type.getMaxHP();
        this.reserve = type.getMaxReserve();
        this.position = position;
        this.rotation = "down";
        setup(gameScreen, rotation);
    }

    /**
     * Construct a FireTruck from a saved state
     * @param x             starting x position of fire truck
     * @param y             starting y position of fire truck
     * @param typeString    type of fire truck
     * @param HP            starting hp of fire truck
     * @param reserve       starting reserve of fire truck
     * @param rotation      direction fire truck was facing
     */
    public FireTruck(float x, float y, String typeString, float HP, float reserve, String rotation) {
        super(FireTruckType.valueOf(typeString).getLookDown());
        this.type = FireTruckType.valueOf(typeString);
        this.HP = HP;
        this.reserve = reserve;
        this.position = new Vector2(x, y);
        this.rotation = rotation;
        setup(null, rotation);
    }

    /**
     * Initialise common objects independent on if a new FireTruck
     * is being made, or loaded from a save state
     *
     * @param gameScreen    Game screen
     * @param rotation      the direction the fire truck is looking in
     */
    private void setup(GameScreen gameScreen, String rotation) {
        this.gameScreen = gameScreen;
        this.path = new Queue<>();
        this.pathSegment = new Queue<>();
        this.pathSegments = new Queue<>();
        this.pathSegmentsStack = new Stack<>();
        this.moving = false;
        this.inCollision = false;
        this.spray = new ArrayList<>();
        this.timeOfLastAttack = System.currentTimeMillis();
        this.inShield = false;
        this.AP = this.type.getAP();
        this.range = this.type.getRange();
        this.setTexture(type.getLook(rotation));
    }

    /**
     * Called every tick and updates the paths to simulate the truck moving along the
     * path
     */
    public void move() {
        if (moving) {
            if (this.pathSegments.size > 0) {
                if (this.pathSegments.first().size > 0) {
                    if (!path.isEmpty()) {
                        Vector2 nextTile = path.first();
                        this.position = nextTile;
                        if (position.x == pathSegments.first().first().x && position.y == pathSegments.first().first().y) {
                            pathSegments.first().removeFirst();
                        }
                        if (!this.inCollision) {
                            changeSprite(nextTile);
                        }
                        previousTile = nextTile;
                        path.removeFirst();
                    }
                } else {
                    this.pathSegments.removeFirst();
                }
            } else {
                moving = false;
            }
        } else if (this.path.isEmpty() && inCollision) {
            inCollision = false;
        }
    }

    /**
     * If path segment can be added to the route
     *
     * @return  <code>true</code> if segment can be added
     *          <code>false</code> otherwise
     */
    public boolean canPathSegmentBeAddedToRoute() {
        if (!pathSegment.first().equals(pathSegment.last())) return true;
        pathSegment.clear();
        return false;
    }

    /**
     * Add the segment to a list of segments when the
     * player's mouse click is lifted
     */
    public void addPathSegmentToRoute() {
        this.pathSegments.addLast(cloneQueue(this.pathSegment));
        this.pathSegment.clear();
    }

    /**
     * Clones the queue to add to the list of segments
     *
     * @param oldQueue  queue to clone
     * @return          new cloned queue
     */
    public Queue<Vector2> cloneQueue(Queue<Vector2> oldQueue) {
        Queue<Vector2> newQueue = new Queue<>();
        for (Vector2 vector : oldQueue) newQueue.addLast(vector);
        return newQueue;
    }

    /**
     * Clear queue apart from the first element
     * @param first element to keep
     */
    private void clearQueueSetFirst(Vector2 first) {
        this.pathSegment.clear();
        this.pathSegment.addLast(first);
    }

    /**
     * Increases Health Points of the truck
     *
     * @param HP    increased by this value
     */
    public void repair(float HP) {
        this.HP += HP;
    }

    /**
     * Increases the Reserve of the truck
     *
     * @param reserve increased by this value
     */
    public void refill(float reserve) {
        this.reserve += reserve;
    }

    /**
     * Called when the player drags mouse on GameScreen Coordinate is checked to see
     * whether it is a valid tile to draw to, then adds it to the paths
     *
     * @param coordinate    Position on the screen that the user's mouse is being
     *                      dragged over
     */
    public void addTileToPathSegment(Vector2 coordinate){
        if (isValidDraw(coordinate)) {
            if (!dragOffMap) {
                this.pathSegment.addLast(new Vector2(((int)coordinate.x),((int)coordinate.y)));
                addSuggestedPathSegment(coordinate);
            } else {
                dragOffMap = false;
            }
        }
    }

    /**
     * Show to generated path found by path finding to path segment
     * @param coordinate    destination coordinate
     */
    private void addSuggestedPathSegment(Vector2 coordinate) {
        clearQueueSetFirst(this.pathSegment.first());
        for (Vector2 position : findPath(coordinate, this.pathSegment.first())) this.pathSegment.addLast(position);
    }

    /**
     * Generates the path for when the truck starts to move
     * to simulate the truck moving smoothly, from the last
     * path segment
     */
    public void generatePathFromLastSegments() {
        if (!pathSegments.isEmpty()) {
            for (int i=1; i<pathSegments.last().size; i++) {
                interpolateMove(pathSegments.last().get(i-1), pathSegments.last().get(i), (int)(40/type.getSpeed()));
            }
        }
        moving = true;
    }

    /**
     * Generates the path for when the truck starts to move
     * to simulate the truck moving smoothly, from ALL the
     * path segments
     */
    public void generatePathFromAllSegments() {
        if (!pathSegments.isEmpty()) {
            this.path.clear();
            interpolateMove(this.position, this.pathSegments.first().first(), (int) (40 / type.getSpeed()));
            for (Queue<Vector2> queue : pathSegments) {
                for (int i=1; i<queue.size; i++) {
                    interpolateMove(queue.get(i-1), queue.get(i), (int)(40/type.getSpeed()));
                }
            }
        }
        moving = true;
    }

    /**
     * Interpolation function to generate smooth path between
     * two adjacent tiles
     *
     * @param previousTile  previous tile truck is from
     * @param currentTile   current tile truck is on
     * @param interpolation decides how slow it goes
     */
    private void interpolateMove(Vector2 previousTile, Vector2 currentTile, int interpolation) {
        for (int j = 0; j < interpolation; j++) {
            this.path.addLast(new Vector2((((previousTile.x - currentTile.x) * -1) / interpolation) * j + previousTile.x, (((previousTile.y - currentTile.y) * -1) / interpolation) * j + previousTile.y));
        }
        this.path.addLast(new Vector2(currentTile.x, currentTile.y));
    }

    /**
     * Used when drawing the path to check whether the next tile to be added to the path is
     * valid
     *
     * @param coordinate    Position on the screen that the user's mouse is being dragged over
     * @return              <code>true</code> if the coordinate is a valid tile to be added to
     *                      the paths
     *                      <code>false</code> otherwise
     */
    private boolean isValidDraw(Vector2 coordinate) {
        if (coordinate.y < 28) {
            if (gameScreen.isRoad((Math.round(coordinate.x)), (Math.round(coordinate.y)))) {
                if (pathSegment.isEmpty()) {
                    if (getPosition().equals(coordinate)) {
                        return true;
                    } else if (!pathSegments.isEmpty() && !pathSegments.last().isEmpty()) {
                        return pathSegments.last().last().equals(coordinate);
                    }
                 } else {
                    return !pathSegment.last().equals(coordinate);
                }
            }
        }
        return false;
    }

    /**
     * Finds a path between two points.
     *
     * @param endPos    Position on the screen that the user's mouse is on
     * @param startPos    Position of last tile in the path queue
     *
     * @return An Vector2 array containing all points in the shortest path between the start and end point
     */
    private Vector2[] findPath(Vector2 endPos, Vector2 startPos) {
        positions = new Queue<>();
        vistited = new boolean[48][29];
        prev = new Vector2[1392];

        for(int i=0; i<48; i++) {
            for(int j=0; j<29; j++){
                vistited[i][j] = false;
            }
        }

        positions.addLast(startPos);
        vistited[(int) startPos.x][(int) startPos.y] = true;

        // makes sure the truck always goes down first from fire station
        if (gameScreen.getStation().getBayTiles().contains(new Vector2(startPos))) {
            positions.addLast(new Vector2(startPos.x, startPos.y-1));
            vistited[(int) startPos.x][(int) startPos.y-1] = true;
        }

        while (!positions.isEmpty()) {
            currentPos = positions.removeLast();
            if(currentPos.x == endPos.x && currentPos.y == endPos.y) {
                reachedEnd = true;
                break;
            }
            exploreNeighbours(currentPos);
        }

        return shortestPath(endPos, startPos);
    }

    /**
     * Searches area around a tile and checks if it is a valid place to move
     *
     * @param currentPos    Position of current tile on the positions queue

     */
    private void exploreNeighbours(Vector2 currentPos) {
        for(int i = 0; i < 4; i++) {
            Vector2 newPos = new Vector2();
            newPos.x = currentPos.x + directionX[i];
            newPos.y = currentPos.y + directionY[i];

            if(newPos.x < 0 || newPos.y < 0) {
                continue;
            }
            if(newPos.x > 47 || newPos.y > 28) {
                continue;
            }
            boolean isRoad = gameScreen.isRoad(Math.round(newPos.x), Math.round(newPos.y));
            if(!isRoad) {
                continue;
            }
            if(vistited[(int)newPos.x][(int)newPos.y]) {
                continue;
            }

            positions.addFirst(newPos);
            vistited[(int)newPos.x] [(int)newPos.y] = true;
            prev[convertVector2ToIntPositionInMap(newPos)] = currentPos;
        }
    }

    /**
     * Maps a parent position to it's child (An adjacent tile)
     *
     * @param pos    The current position being queued
     *
     * @return An int reprsenting the Vector2 as a point on the map
     */
    private int convertVector2ToIntPositionInMap(Vector2 pos) {
        return ((int) (pos.x * 29 + pos.y));
    }

    /**
     * Reverses an array
     *
     * @param a The shortest path array, so it goes from start to finish rather then finish to start in order
     */
    private void reverse(Vector2[] a) {
        Collections.reverse(Arrays.asList(a));
    }

    /**
     * Returns the shortest path using the mapped coordinates
     *
     * @param endPos    The shortest path array, so it goes from start to finish rather then finish to start in order
     *
     * @return A array containing the Vector2 positions of the shortest path between two points
     */
    private Vector2[] shortestPath(Vector2 endPos, Vector2 startPos) {
        reconstructedPath = new LinkedList<>();
        for(Vector2 at = endPos; at != null; at = prev[convertVector2ToIntPositionInMap(at)]) {

            if(at == startPos) {
                if(!this.pathSegment.isEmpty())
                    continue;
            }
            reconstructedPath.add(at);
        }

        Object[] objectAarray = reconstructedPath.toArray();
        Vector2[] path = new Vector2[objectAarray.length];

        for(int i=0;i<objectAarray.length;i++) {
            path[i] = (Vector2) objectAarray[i];
        }

        reverse(path);
        return path;
    }

    /**
     * Changes the direction of the truck depending on the previous tile and the next tile
     *
     * @param nextTile  first tile in the queue (next to be followed)
     */
    private void changeSprite(Vector2 nextTile) {
        if (previousTile != null) {
            if (nextTile.x > previousTile.x) {
                setTexture(this.type.getLookRight());
                rotation = "right";
            } else if (nextTile.x < previousTile.x) {
                setTexture(this.type.getLookLeft());
                rotation = "left";
            } else if (nextTile.y > previousTile.y) {
                setTexture(this.type.getLookUp());
                rotation = "up";
            } else if (nextTile.y < previousTile.y) {
                setTexture(this.type.getLookDown());
                rotation = "down";
            }
        }
    }

    /**
     * Clears the two paths
     */
    public void resetPath() {
        this.path.clear();
        this.pathSegment.clear();
    }

    /**
     * When the truck collides with another truck
     */
    public void collided() {
        this.inCollision = true;
        this.path.clear();
        this.pathSegments.clear();
    }

    /**
     * Deals damage to Fortress by generating a WaterParticle and adding
     * it to the spray
     *
     * @param fortress Fortress being attacked
     */
    public void attack(Fortress fortress) {
        if (this.reserve > 0) {
            this.spray.add(new Particle(this.getVisualPosition(), fortress.getPosition(), fortress));
            this.reserve -= Math.min(this.reserve, this.AP);
        }
    }

    /**
     * Called every tick to check if a Fortress is within the range of
     *  the truck
     *
     * @param fortress  Fortress' position being checked
     * @return          <code>true</code> if Fortress is within range
     *                  <code>false </code> otherwise
     */
    public boolean fortressInRange(Vector2 fortress) {
        return this.getVisualPosition().dst(fortress) <= this.getRange();
    }

    /**
     * Updates the position of each WaterParticle
     */
    public void updateSpray() {
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
     * Remove the WaterParticle from the spray when it hits the Fortress
     *
     * @param particle  The particle to be removed from spray
     */
    private void removeParticle(Particle particle) {
        this.spray.remove(particle);
    }

    /**
     * Damages the Fortress depending on the truck's AP
     *
     * @param particle  the particle which damages the fortress
     */
    private void damage(Particle particle) {
        Fortress target = (Fortress)particle.getTarget();
        target.damage(Math.min(this.AP, target.getHP()));
    }

    /**
     * Damage inflicted on truck by a fortress, called when a bomb hits a truck it plays
     * a sound and decreases the fire trucks HP by the amount of the fortresses AP
     *
     * @param HP    amount of HP being taken away dependant
     *              on the AP of the attacking Fortress
     */
    public void fortressDamage(float HP) {
        if (SoundFX.music_enabled) {
            SoundFX.sfx_truck_damage.play();
        }
        this.HP -= Math.min(HP, this.HP);
    }

    /**
     *  Draws the visual, colourful path that the truck will follow
     *
     * @param mapBatch  Batch that the path is being drawn to (map dependant)
     */
    public void drawPath(Batch mapBatch) {
        if (!this.pathSegment.isEmpty()) {
            mapBatch.setColor(this.type.getTrailColour());
            for (Vector2 tile : this.pathSegment) {
                if (tile.equals(this.pathSegment.last())) {
                    mapBatch.draw(this.type.getTrailImageEnd(), tile.x, tile.y, 1, 1);
                }
                mapBatch.draw(this.type.getTrailImage(), tile.x, tile.y, 1, 1);
            }
            mapBatch.setColor(Color.WHITE);
        }
        if (!this.pathSegments.isEmpty()) {
            for (Queue<Vector2> queue : this.pathSegments) {
                mapBatch.setColor(this.type.getTrailColour());
                for (Vector2 tile : queue) {
                    if (tile.equals(pathSegments.last().last())) {
                        mapBatch.draw(this.type.getTrailImageEnd(), tile.x, tile.y, 1, 1);
                    }
                    mapBatch.draw(this.type.getTrailImage(), tile.x, tile.y, 1, 1);
                }
                mapBatch.setColor(Color.WHITE);
            }
        }
    }

    /**
     * Draws the mini health/reserve indicators relative to the truck
     *
     * @param shapeMapRenderer  Renderer that the stats are being drawn to (map  dependant)
     */
    public void drawStats(ShapeRenderer shapeMapRenderer) {
        shapeMapRenderer.rect(this.getPosition().x + 0.2f, this.getPosition().y + 1.3f, 0.6f, 0.8f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        shapeMapRenderer.rect(this.getPosition().x + 0.266f, this.getPosition().y + 1.4f, 0.2f, 0.6f, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
        shapeMapRenderer.rect(this.getPosition().x + 0.266f, this.getPosition().y + 1.4f, 0.2f, this.getReserve() / this.type.getMaxReserve() * 0.6f, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN);
        shapeMapRenderer.rect(this.getPosition().x + 0.533f, this.getPosition().y + 1.4f, 0.2f, 0.6f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        shapeMapRenderer.rect(this.getPosition().x + 0.533f, this.getPosition().y + 1.4f, 0.2f, this.getHP() / this.type.getMaxHP() * 0.6f, Color.RED, Color.RED, Color.RED, Color.RED);
        for (Particle particle : this.getSpray()) {
            shapeMapRenderer.rect(particle.getPosition().x, particle.getPosition().y, particle.getSize(), particle.getSize(), particle.getColour(), particle.getColour(), particle.getColour(), particle.getColour());
        }
    }

    /**
     * "Undo" a segment from the queue of segments that make up the
     * path that the truck follows. This can only happen when the game
     * is frozen and makes sure the truck ends up in a tile
     */
    public void undoSegment() {
        if (this.gameScreen.getState().equals(GameScreen.PlayState.FREEZE)) {

            // can only undo a segment if there is a segment to undo
            if (this.pathSegments.size > 0) {

                /* when there is one last segment in the list and a one tile segment
                must be kept to prevent the truck getting stuck between tiles*/
                if (this.pathSegments.size == 1) {
                    boolean alreadyAdded = false;
                    for (Queue<Vector2> queue : pathSegmentsStack) if (pathSegments.first().first().equals(queue.first())) alreadyAdded = true;
                    if (!alreadyAdded) {
                        // adds little bit onto path to make sure the truck ends up in the next tile
                        pathSegmentsStack.push(cloneQueue(pathSegments.first()));
                        while (this.pathSegments.first().size > 1)  this.pathSegments.first().removeLast();
                        interpolateMove(this.position, this.pathSegments.first().first(), (int) (40 / type.getSpeed()));
                    }
                } else {
                    // pushes the undo to the stack so that it can then be re-added after using the redo method
                    this.path.clear();
                    pathSegmentsStack.push(pathSegments.removeLast());
                }
            }
        }
    }

    /**
     * "Redo" a segment that you just removed
     * with the "undo" method and only works when the
     * game is frozen and there is something to redo
     */
    public void redoSegment() {
        if (this.gameScreen.getState().equals(GameScreen.PlayState.FREEZE)) {
            this.path.clear();
            if (!pathSegmentsStack.empty()) {
                // this is to make sure that the first "safety" segment doesn't mess things up
                if (pathSegments.size == 1 && pathSegments.first().size < 2) {
                    pathSegments.removeFirst();
                }
                pathSegments.addLast(pathSegmentsStack.pop());
            }

        }
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
     * Generates the description of Firetruck which are
     * the values saved in the save file
     * @return  the description of FireTruck
     */
    public Desc.FireTruck getDescriptor() {
        Desc.FireTruck desc = new Desc.FireTruck();
        desc.type = this.type.name();
        desc.health = this.getHP();
        desc.reserve = this.getReserve();
        desc.x = (int) Math.floor(this.getPosition().x);
        desc.y = (int) Math.floor(this.getPosition().y);
        desc.rotation = this.rotation;
        return desc;
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
     * Checks whether the fire truck is on a bay tile
     * of the fire station
     * @param station   contains bay tiles
     * @return          <code>true</code>   if truck is on a bay tile
     *                  <code>false</code>  otherwise
     */
    public boolean isOnBayTile(FireStation station) {
        return station.getBayTiles().contains(getTilePosition());
    }

    /**
     * Gets rounded truck position
     * Used for patrol collision
     *
     * @return Rounded position vector
     */
    public Vector2 getTilePosition() {
        return new Vector2(Math.round(this.position.x), Math.round(this.position.y));
    }

    /**
     * Sets time of last attack to unix timestamp provided
     *
     * @param timestamp  timestamp set as time of last attack
     */
    public void setTimeOfLastAttack(long timestamp) {
        this.timeOfLastAttack = timestamp;
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

    public void setHP(int hp) { this.HP = hp; }

    public float getReserve() {
        return this.reserve;
    }

    public FireTruckType getType() {
        return this.type;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public void setPosition(Vector2 newPosition) {
        this.position = newPosition;
    }

    public Queue<Vector2> getPath() {
        return this.path;
    }

    private ArrayList<Particle> getSpray() {
        return this.spray;
    }

    public boolean getMoving() {
        return this.moving;
    }

    public  float getRange() { return this.range; }

    public void clearPathSegmentsStack() {
        pathSegmentsStack.clear();
    }

    public boolean inShield() {
        return this.inShield;
    }

    public void setShield(boolean b){
        this.inShield = b;
    }

    public float getAP() { return this.AP; }

    public void setRange(float range) { this.range = range;}

    public void makeFreezeAvailable() { gameScreen.setFreezeCooldown(0); }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }
}
