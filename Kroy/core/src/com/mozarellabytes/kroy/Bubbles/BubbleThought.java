package com.mozarellabytes.kroy.Bubbles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class BubbleThought {
    private Vector2 position;
    private TextureAtlas atlas;
    private Animation<TextureRegion> animation;
    private float elapsedTime;

    public BubbleThought(Vector2 position, String animationType) {
        this.atlas = new TextureAtlas(Gdx.files.internal("sprites/vfx/bubbles.atlas"));
        this.position = position;
        this.animation = new Animation<TextureRegion>(.032f, atlas.findRegions(animationType), Animation.PlayMode.LOOP);
    }

    public void update(Batch batch) {
        elapsedTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);
        batch.draw(currentFrame, position.x, position.y, .8f, .8f);
    }

    public void setPosition(Vector2 position) {
        this.position = new Vector2(position.x - .8f, position.y);
    }
    public Vector2 getPosition(Vector2 position) {
        return this.position;
    }

    public void dispose(){
        this.atlas.dispose();
        this.dispose();
    }
}
