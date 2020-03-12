package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Visual Effects within the game. Such as when
 * Fortresses are destroyed, the explosion.
 */
public class VFX {
    private TextureAtlas atlas;
    private Vector2 position;
    private float elapsedTime;
    private int width, height;
    private Animation<TextureRegion> animation;
    private boolean isPowerup;

    /**
     * Constructor for VFX
     * @param type      type of visual effect
     * @param position  where the visual effect will be shown
     */
    public VFX(int type, Vector2 position) {
        setType(type);
        this.position = position;
    }

    /**
     * Sets the attributes depending on the type
     * of visual effect
     *
     * @param type  of visual effect
     */
    private void setType(int type) {
        if (type == 0) {
            this.atlas = new TextureAtlas(Gdx.files.internal("sprites/vfx/explosion1.atlas"));
            this.animation = new Animation<>(.032f, atlas.findRegions("rexp"), Animation.PlayMode.NORMAL);
            this.width = 4;
            this.height = 4;
        } else if (type == 1) {
            this.atlas = new TextureAtlas(Gdx.files.internal("sprites/vfx/fireball.atlas"));
            this.animation = new Animation<>(.032f, atlas.findRegions("fireball"), Animation.PlayMode.NORMAL);
            this.width = 4;
            this.height = 4;
        }
    }

    /**
     * Updates the VFX animation
     * @param delta time since last frame
     */
    public void update(float delta) {
        elapsedTime += delta;
    }

    /**
     * Draws the visual effect
     * @param batch where to draw to
     */
    public void render(Batch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, false);
        batch.draw(currentFrame, position.x, position.y, this.width, this.height);
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void dispose() {
        atlas.dispose();
    }
}
