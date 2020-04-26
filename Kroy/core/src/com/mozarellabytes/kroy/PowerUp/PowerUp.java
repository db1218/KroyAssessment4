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
 * Abstract class for powerups which spawn
 * at random locations on roads and give the
 * fire truck advantages
 */
public abstract class PowerUp {

    /** Used to animate the PowerUp to make it spin */
    private final Animation<TextureRegion> animation;

    /** Used to get the multiple textures needed for the PowerUp to make
     * it appear as though the PowerUp is spinning */
    private final TextureAtlas atlas;

    /** The location on the map where the PowerUp should be spawned */
    private Vector2 position;

    /** Used to change the PowerUp's currentFrame to give the impression
     * of it spinning */
    private float elapsedTime;

    /** The PowerUp's texture that should be rendered to the screen */
    private TextureRegion currentFrame;

    /** The time in seconds that the PowerUp has left before it is destroyed */
    private float timeLeftOnScreen;

    /** The total time in seconds that the powerup will be available for */
    private final float timeOnScreen;

    /** Flag for whether the PowerUp can be rendered to the gameScreen */
    boolean canBeRendered;

    /** Flag for whether the PowerUp can be destroyed in gameScreen */
    boolean canBeDestroyed;

    boolean isPowerCurrentlyInvoked;

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
        this.isPowerCurrentlyInvoked = false;
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
        if (timeLeftOnScreen <= 0 && !isPowerCurrentlyInvoked) removePowerUp();
    }

    /**
     * Sets the appropriate boolean flags so that the PowerUp can be
     * removed from currentPowerups in GameScreen.
     */
    void removePowerUp() {
        canBeRendered = false;
        canBeDestroyed = true;
    }

    /**
     * Renders the powerup's current frame
     *
     * @param mapBatch  where to render to
     */
    public void render(Batch mapBatch) {
        mapBatch.draw(currentFrame, position.x, position.y, 1, 1);
    }

    /**
     * Draws a bar above the powerup to indicate how much longer the powerup
     * will be available for
     *
     * @param shapeMapRenderer  the ShapeRenderer used in gameScreen
     */
    public void drawStats(ShapeRenderer shapeMapRenderer) {
        if (this.canBeRendered) {
            shapeMapRenderer.rect(this.getPosition().x - 0.1f, this.getPosition().y + 1.4f, 1.2f, 0.55f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
            shapeMapRenderer.rect(this.getPosition().x, this.getPosition().y + 1.5f, 1f, 0.34f, Color.GOLDENROD, Color.GOLDENROD, Color.GOLDENROD, Color.GOLDENROD);
            shapeMapRenderer.rect(this.getPosition().x, this.getPosition().y + 1.5f, timeLeftOnScreen / timeOnScreen, 0.34f, Color.GOLD, Color.GOLD, Color.GOLD, Color.GOLD);
        }
    }

    /**
     * Generate a list of the available powerups, this is used in
     * GameScreen to randomly choose which powerUp should be generated
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
     * Deal the effect of the powerup to the truck
     * @param truck truck that gets the effect of the fire truck
     */
    public abstract void invokePower(FireTruck truck);

    public abstract String getName();

    public abstract String getDesc();

    public boolean getCanBeRendered() { return this.canBeRendered; }

    public boolean getCanBeDestroyed() { return this.canBeDestroyed; }

    public void setPosition(Vector2 position) { this.position = position; }

    public Vector2 getPosition() { return this.position; }

    public float getTimeOnScreen() { return timeOnScreen; }

    public float getTimeLeftOnScreen() { return timeLeftOnScreen; }

    public TextureRegion getCurrentFrame() {
        return this.currentFrame;
    }

    public float getElapsedTime() {
        return this.elapsedTime;
    }

    public  boolean isPowerCurrentlyInvoked() { return this.isPowerCurrentlyInvoked; };

}
