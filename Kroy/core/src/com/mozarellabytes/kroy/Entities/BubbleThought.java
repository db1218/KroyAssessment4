package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class BubbleThought {
    private Vector2 position;
    private TextureAtlas atlas;
    private Animation<TextureRegion> bubble;
    private float elapsedTime;

    public BubbleThought(int type, Vector2 position) {
        this.atlas = new TextureAtlas(Gdx.files.internal("sprites/vfx/bubbles.atlas"));
        this.bubble = new Animation<>(.032f, atlas.findRegions("bubble"), Animation.PlayMode.NORMAL);
        this.position = position;
        setType(type);
    }

    private void setType(int type) {
        switch (type) {
            case 0:
                bubble = new Animation<>(.06f, atlas.findRegions("life"), Animation.PlayMode.NORMAL);
                break;
            case 1:
                bubble = new Animation<>(.06f, atlas.findRegions("drop"), Animation.PlayMode.NORMAL);
                break;
            case 2:
                bubble = new Animation<>(.06f, atlas.findRegions("death"), Animation.PlayMode.NORMAL);
                break;
            case 3:
                bubble = new Animation<>(.06f, atlas.findRegions("dot"), Animation.PlayMode.NORMAL);
        }
    }

    public void update(Batch batch) {
        elapsedTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = bubble.getKeyFrame(elapsedTime, true);
        batch.draw(currentFrame, position.x, position.y, .8f, .8f);
    }

    public void setPosition(Vector2 position) {
        this.position = new Vector2(position.x - .8f, position.y);
    }

    public void dispose(){
        this.atlas.dispose();
        this.dispose();
    }
}
