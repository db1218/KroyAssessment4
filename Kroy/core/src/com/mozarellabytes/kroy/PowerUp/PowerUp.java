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

    public void update() {
        updateAnimation();
        updateTimeOnScreen();
    }

    private void updateAnimation(){
        // Accumulate amount of time that has passed
        elapsedTime += Gdx.graphics.getDeltaTime();
        // Get current frame of animation for the current stateTime
        currentFrame = animation.getKeyFrame(elapsedTime, true);
    }

    private void updateTimeOnScreen(){
        timeLeftOnScreen -= Gdx.graphics.getDeltaTime();
        if (timeLeftOnScreen <= 0) removePowerUp();
    }

    void removePowerUp() {
        canBeRendered = false;
        canBeDestroyed = true;
    }

    public void render(Batch mapBatch) {
        mapBatch.draw(currentFrame, position.x, position.y, 1, 1);
    }

    public void drawStats(ShapeRenderer shapeMapRenderer) {
        if (this.canBeRendered) {
            shapeMapRenderer.rect(this.getPosition().x - 0.1f, this.getPosition().y + 1.4f, 1.2f, 0.55f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
            shapeMapRenderer.rect(this.getPosition().x, this.getPosition().y + 1.5f, 1f, 0.34f, Color.GOLDENROD, Color.GOLDENROD, Color.GOLDENROD, Color.GOLDENROD);
            shapeMapRenderer.rect(this.getPosition().x, this.getPosition().y + 1.5f, timeLeftOnScreen / timeOnScreen, 0.34f, Color.GOLD, Color.GOLD, Color.GOLD, Color.GOLD);
        }
    }

    public static ArrayList<PowerUp> createNewPowers(Vector2 location){
        ArrayList<PowerUp> possiblePowerups = new ArrayList<>();
        possiblePowerups.add(new Heart(location));
        possiblePowerups.add(new Shield(location));
        possiblePowerups.add(new Water(location));
        possiblePowerups.add(new Range(location));
        possiblePowerups.add(new Freeze(location));
        return possiblePowerups;
    }

    public void dispose() { this.atlas.dispose(); }

    public abstract void invokePower(FireTruck truck);

    public boolean getCanBeRendered() { return this.canBeRendered; }

    public boolean getCanBeDestroyed(){ return this.canBeDestroyed; }

    public void setPosition(Vector2 position){ this.position = position; }

    public Vector2 getPosition() { return this.position; }

}
