package com.mozarellabytes.kroy.PowerUp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;

import java.util.ArrayList;

/**
 * Abstract class for powerups which can
 * spawn randomly aon roads and give the
 * fire truck advantages
 */
public abstract class PowerUp {

    private final Animation<TextureRegion> animation;
    private final TextureAtlas atlas;
    private Vector2 position;
    private float elapsedTime;
    private TextureRegion currentFrame;

    private float timeLeftOnScreen;
    private final float timeOnScreen;

    boolean canBeRendered;
    boolean canBeDestroyed;

    /**
     * Constructor for PowerUp
     * @param animationType type of animation
     * @param position      where it should spawn
     */
    public PowerUp(String animationType, Vector2 position){
        this.atlas = new TextureAtlas(Gdx.files.internal("sprites/powerups/powerup.atlas"));
        this.currentFrame = new TextureRegion();
        this.animation = new Animation<>(.032f, atlas.findRegions(animationType), Animation.PlayMode.LOOP);
        this.canBeRendered = true;
        this.canBeDestroyed = false;
        this.position = position;
        this.timeOnScreen = 15;
        this.timeLeftOnScreen = timeOnScreen;
    }

    /**
     * Updates the animation and time left
     */
    public void update() {
        updateAnimation();
        updateTimeOnScreen();
    }

    /**
     * Updates the animation
     */
    private void updateAnimation(){
        // Accumulate amount of time that has passed
        elapsedTime += Gdx.graphics.getDeltaTime();
        // Get current frame of animation for the current stateTime
        currentFrame = animation.getKeyFrame(elapsedTime, true);
    }

    /**
     * Updates the time left on screen bar
     */
    private void updateTimeOnScreen(){
        timeLeftOnScreen -= Gdx.graphics.getDeltaTime();
        if (timeLeftOnScreen <= 0) removePowerUp();
    }

    /**
     * Remove the power up
     */
    void removePowerUp() {
        canBeRendered = false;
        canBeDestroyed = true;
    }

    /**
     * Render the powerup at that frame
     *
     * @param mapBatch  where to render to
     */
    public void render(Batch mapBatch) {
        mapBatch.draw(currentFrame, position.x, position.y, 1, 1);
    }

    /**
     * Draw the time left bar of the powerup
     *
     * @param shapeMapRenderer  where to render to
     */
    public void drawStats(ShapeRenderer shapeMapRenderer) {
        if (this.canBeRendered) {
            shapeMapRenderer.rect(this.getPosition().x - 0.1f, this.getPosition().y + 1.4f, 1.2f, 0.55f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
            shapeMapRenderer.rect(this.getPosition().x, this.getPosition().y + 1.5f, 1f, 0.34f, Color.GOLDENROD, Color.GOLDENROD, Color.GOLDENROD, Color.GOLDENROD);
            shapeMapRenderer.rect(this.getPosition().x, this.getPosition().y + 1.5f, timeLeftOnScreen / timeOnScreen, 0.34f, Color.GOLD, Color.GOLD, Color.GOLD, Color.GOLD);
        }
    }

    /**
     * Generate list of powerups to then select a random one
     * from GameScreen
     *
     * @param location  where it should spawn
     * @return          list of powerups
     */
    public static ArrayList<PowerUp> createNewPowers(Vector2 location) {
        ArrayList<PowerUp> possiblePowerups = new ArrayList<>();
        possiblePowerups.add(new Heart(location));
        possiblePowerups.add(new Shield(location));
        possiblePowerups.add(new Water(location));
        possiblePowerups.add(new Range(location));
        possiblePowerups.add(new Freeze(location));
        return possiblePowerups;
    }

    public void dispose() {
        this.atlas.dispose();
    }

    /**
     * Deal the effect if the powerup to the truck
     * @param truck truck that gets the effect of the fire truck
     */
    public abstract void invokePower(FireTruck truck);

    public boolean getCanBeRendered() { return this.canBeRendered; }

    public boolean getCanBeDestroyed() { return this.canBeDestroyed; }

    public void setPosition(Vector2 position) { this.position = position; }

    public Vector2 getPosition() { return this.position; }

}
