package com.mozarellabytes.kroy.Entities;

/**********************************************************************************
                            Edited for assessment 4
 **********************************************************************************/


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.mozarellabytes.kroy.Utilities.Desc;
import com.mozarellabytes.kroy.Utilities.SoundFX;

import java.util.ArrayList;

/** Edited for assessment 4 to allow save functionality
 *
 * Fortresses can attack fire trucks when within range
 * and can regenerate health over time
 */
public class Fortress {

    /*** Fortress health, destroyed on zero */
    private float HP;

    /*** Position of the Fortress */
    private final Vector2 position;

    /*** Where the Fortress lies on the map */
    private Rectangle area;

    /*** List of bombs that are active */
    private ArrayList<Bomb> bombs;

    /*** Gives Fortress certain stats */
    private final FortressType fortressType;

    /**
     * Constructs Fortress at certain position and
     * of a certain type
     *
     * @param x     x coordinate of Fortress (lower left point)
     * @param y     y coordinate of Fortress (lower left point)
     * @param type  Type of Fortress to give certain stats
     */
    public Fortress(float x, float y, FortressType type) {
        this.position = new Vector2(x, y);
        this.fortressType = type;
        this.HP = type.getMaxHP();
        setup();
    }

    /**
     * Constructs a Fortress from a save file
     * @param x             x location of fortress
     * @param y             y location of fortress
     * @param typeString    type of fortress as a string
     * @param HP            hp of fortresss
     */
    public Fortress(float x, float y, String typeString, float HP) {
        this.position = new Vector2(x, y);
        this.fortressType = FortressType.valueOf(typeString);
        this.HP = HP;
        setup();
    }

    /**
     * Initialise common objects independent on if a new Fortress
     * is being made, or loaded from a save state
     */
    public void setup() {
        this.bombs = new ArrayList<>();
        this.area = new Rectangle(this.position.x - (float) this.fortressType.getW()/2, this.position.y - (float) this.fortressType.getH()/2,
                this.fortressType.getW(), this.fortressType.getH());
    }

    /**
     * Checks if the truck's position is within the attack range of the fortress
     *
     * @param targetPos the truck position being checked
     * @return          <code>true</code> if truck within range of fortress
     *                  <code>false</code> otherwise
     */
    public boolean withinRange(Vector2 targetPos) {
        return targetPos.dst(this.position) <= fortressType.getRange();
    }

    /**
     * Generates bombs to attack the FireTruck with
     * @param target                FireTruck being attacked
     * @param randomTarget          whether the bomb hits every time or
     *                              there is a chance it misses
     * @param difficultyMultiplier  damage multiplier the bomb deals
     */
    public void attack(FireTruck target, boolean randomTarget, float difficultyMultiplier) {
        if (target.getTimeOfLastAttack() + fortressType.getDelay() < System.currentTimeMillis()) {
            this.bombs.add(new Bomb(this, target, randomTarget, difficultyMultiplier));
            target.setTimeOfLastAttack(System.currentTimeMillis());
            if (SoundFX.music_enabled) {
                SoundFX.sfx_fortress_attack.play();
            }
        }
    }

    /**
     * Updates the position of all the bombs and checks whether
     * they have hit their target. If they have, it should deal
     * damage to the truck, remove the bomb and shake the screen
     * @return  <code>true</code> if bomb hits a truck
     *          <code>false</code> if bomb does nt hit a true
     */
    public boolean updateBombs() {
        for (int i = 0; i < this.getBombs().size(); i++) {
            Bomb bomb = this.getBombs().get(i);
            bomb.updatePosition();
            if (bomb.checkHit()) {
                bomb.damageTruck();
                this.removeBomb(bomb);
                return true;
            } else if (bomb.hasReachedTargetTile()) {
                this.removeBomb(bomb);
            }
        }
        return false;
    }

    /**
     * Removes Bomb from bomb list. This
     * occurs when the bomb hits or misses
     *
     * @param bomb bomb being removed
     */
    private void removeBomb(Bomb bomb) {
        this.bombs.remove(bomb);
    }

    /**
     * Draws the health bars above the Fortress
     *
     * @param shapeMapRenderer  The renderer to be drawn to
     */
    public void drawStats(ShapeRenderer shapeMapRenderer) {
        shapeMapRenderer.rect(this.getPosition().x - 0.26f, this.getPosition().y + 1.4f, 0.6f, 1.2f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        shapeMapRenderer.rect(this.getPosition().x - 0.13f, this.getPosition().y + 1.5f, 0.36f, 1f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        shapeMapRenderer.rect(this.getPosition().x - 0.13f, this.getPosition().y + 1.5f, 0.36f, this.getHP() / this.fortressType.getMaxHP() * 1f, Color.RED, Color.RED, Color.RED, Color.RED);
    }

    /**
     * Draws the Fortress on the map
     *
     * @param mapBatch  the renderer in line with the map
     */
    public void draw(Batch mapBatch) {
        mapBatch.draw(this.getType().getTexture(this.getHP()), this.getArea().x, this.getArea().y, this.getArea().width, this.getArea().height);
    }

    /**
     * Creates a instance of DestroyedEntity with the destroyed Fortress's texture in the same location.
     *
     * @return DestroyedEntity with Area and destroyed texture from this fortress.
     */
    public DestroyedEntity createDestroyedFortress(){
        return new DestroyedEntity(this.getType().getTexture(0), this.area);
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public float getHP() {
        return this.HP;
    }

    public void damage(float HP){
        this.HP -= HP;
    }

    public Rectangle getArea() {
        return this.area;
    }

    public FortressType getType() {
        return this.fortressType;
    }

    public ArrayList<Bomb> getBombs() {
        return this.bombs;
    }

    public float getRange(){
        return this.fortressType.getRange();
    }

    public void setBombs(ArrayList<Bomb> bombs) { this.bombs = bombs; }

    /** Added for assessment 4
     *
     * Generates the description of the fortress to
     * be stored in the save file
     *
     * @return  description of fortress
     */
    public Desc.Fortress getSave() {
        Desc.Fortress desc = new Desc.Fortress();
        desc.type = this.fortressType.name();
        desc.health = this.getHP();
        desc.x = this.getPosition().x;
        desc.y = this.getPosition().y;
        return desc;
    }

}
