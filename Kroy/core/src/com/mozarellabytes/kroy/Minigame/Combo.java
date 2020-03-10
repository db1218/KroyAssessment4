package com.mozarellabytes.kroy.Minigame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Combo {

        private Animation<TextureRegion> animation;
        private TextureAtlas atlas;
        private Vector2 position;
        private float elapsedTime;
        private TextureRegion currentFrame;

        public Combo(String animationType, Vector2 position){
            this.atlas = new TextureAtlas(Gdx.files.internal("sprites/vfx/combos.atlas"));
            this.currentFrame = new TextureRegion();
            this.animation = new Animation<TextureRegion>(.032f, atlas.findRegions(animationType), Animation.PlayMode.NORMAL);
            this.position = position;
        }

        public void update() {
            updateAnimation();
        }

        private void updateAnimation(){
            // Accumulate amount of time that has passed
            elapsedTime += Gdx.graphics.getDeltaTime();
            // Get current frame of animation for the current stateTime
            currentFrame = animation.getKeyFrame(elapsedTime, true);
        }

        public void render(Batch mapBatch) {
            mapBatch.draw(currentFrame, position.x, position.y, 1, 1);
        }

        public void dispose() { this.atlas.dispose(); }

        public void setPosition(Vector2 position){ this.position = position; }

}
