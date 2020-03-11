package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class VFX {
    private TextureAtlas atlas;
    private Vector2 position;
    private float elapsedTime;
    private int width, height;
    private Animation<TextureRegion> animation;
    private boolean isPowerup;

    public VFX(int type, Vector2 position) {
        setType(type);
        this.position = position;
    }

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

    public void update(Batch batch) {
        elapsedTime += Gdx.graphics.getDeltaTime();

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
