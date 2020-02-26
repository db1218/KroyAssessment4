package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;

public class Powerup {

    private int type; //0 = health | 1 = water | 2 = shield
    private TextureAtlas atlas;
    private Animation<TextureRegion> animation;
    private int boost;
    private Vector2 position;
    private float elapsedTime;

    public Powerup(int type, Vector2 position) {
        this.type = type;
        this.position = position;
        this.atlas = new TextureAtlas(Gdx.files.internal("sprites/powerups/powerup.atlas"));
        setType(type);
    }

    private void setType(int type) {
        if (type == 0) {
            this.animation = new Animation<TextureRegion>(0.033f, atlas.findRegions("heart"), Animation.PlayMode.LOOP);
        } else if (type == 1) {
            this.animation = new Animation<TextureRegion>(0.033f, atlas.findRegions("water"), Animation.PlayMode.LOOP);
        } else {
            this.animation = new Animation<TextureRegion>(0.033f, atlas.findRegions("shield"), Animation.PlayMode.LOOP);
        }
    }

    public boolean update(Batch mapBatch){
        // Accumulate amount of time that has passed
        elapsedTime = Gdx.graphics.getDeltaTime();

        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);
        mapBatch.draw(currentFrame, position.x, position.y, 1, 1);
        if(elapsedTime > animation.getAnimationDuration()){
            return true;
        }
        return false;
    }

    public Vector2 getPosition() {
        return this.position;
    }
    public int getType() {
        return this.type;
    }

    public void dispose() {
        this.atlas.dispose();
    }
}