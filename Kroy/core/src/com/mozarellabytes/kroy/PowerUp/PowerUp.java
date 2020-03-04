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
import java.util.Random;

public abstract class PowerUp {

    private Animation<TextureRegion> animation;
    private TextureAtlas atlas;
    private Vector2 position;
    private float elapsedTime;
    private TextureRegion currentFrame;

    public ArrayList<PowerUp> currentPowerups;

    private float timeLeftOnScreen;
    private float timeOnScreen;

    private ArrayList<Vector2> locations;

    boolean canBeRendered;
    boolean canBeDestroyed;

    public PowerUp(String animationType){
        this.atlas = new TextureAtlas(Gdx.files.internal("sprites/powerups/powerup.atlas"));
        this.currentFrame = new TextureRegion();
        this.animation = new Animation<TextureRegion>(.032f, atlas.findRegions(animationType), Animation.PlayMode.LOOP);
        this.canBeRendered = true;
        this.canBeDestroyed = false;
        this.locations = new ArrayList<>();
        populateLocations();
        this.position = generateRandomLocation();
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

    public static ArrayList<PowerUp> createNewPowers(){
        ArrayList<PowerUp> possiblePowerups = new ArrayList<PowerUp>();
        possiblePowerups.add(new Heart());
        possiblePowerups.add(new Shield());
        possiblePowerups.add(new Water());
        possiblePowerups.add(new Range());
        return possiblePowerups;
    }

    // need to work out how to stop them from spawning in the same location
    private Vector2 generateRandomLocation() {
        Random rand = new Random();
        int index = rand.nextInt(locations.size());
        return locations.get(index);
    }

    private boolean checkNotSameLocation(Vector2 vector2){
        if(!(this.currentPowerups == null)) {
            for (PowerUp power : this.currentPowerups) {
                if (power.position == vector2) {
                    return true;
                }
            }
        }
        return false;
    }


    protected void populateLocations(){
        locations.add(new Vector2(30,3));
        locations.add(new Vector2(17,10));
        locations.add(new Vector2(13,12));
        locations.add(new Vector2(8,27));
        locations.add(new Vector2(1,10));
        locations.add(new Vector2(17,20));
        locations.add(new Vector2(26,12));
        locations.add(new Vector2(25,20));
        locations.add(new Vector2(34,17));
        locations.add(new Vector2(26,27));
        locations.add(new Vector2(37,22));
        locations.add(new Vector2(44,15));
        locations.add(new Vector2(41,7));
        locations.add(new Vector2(30,3));
    }


    public void dispose() { this.atlas.dispose(); }

    public abstract void invokePower(FireTruck truck);

    public boolean getCanBeRendered() { return this.canBeRendered; }

    public boolean getCanBeDestroyed(){
        return this.canBeDestroyed;
    }

    public void setPosition(Vector2 position){
        this.position = position;
    }

    public Vector2 getPosition() { return this.position; }

}
