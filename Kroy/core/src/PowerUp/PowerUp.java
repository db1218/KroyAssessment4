package PowerUp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;

public abstract class PowerUp {

    private Animation<TextureRegion> animation;
    private TextureAtlas atlas;
    private Vector2 position;
    private float elapsedTime;
    private TextureRegion currentFrame;

    boolean canBeRendered;
    boolean canBeDestroyed;

    public PowerUp(String animationType){
        this.atlas = new TextureAtlas(Gdx.files.internal("sprites/powerups/powerup.atlas"));
        this.currentFrame = new TextureRegion();
        this.animation = new Animation<TextureRegion>(.032f, atlas.findRegions(animationType), Animation.PlayMode.LOOP);
        this.canBeRendered = true;
        this.canBeDestroyed = false;
    }

    public PowerUp(Vector2 position, String animationType) {
        this.position = position;
        this.atlas = new TextureAtlas(Gdx.files.internal("sprites/powerups/powerup.atlas"));
        this.currentFrame = new TextureRegion();
        this.animation = new Animation<TextureRegion>(.032f, atlas.findRegions(animationType), Animation.PlayMode.LOOP);
        this.canBeRendered = true;
        this.canBeDestroyed = false;
    }

    public void render(Batch mapBatch) {
        mapBatch.draw(currentFrame, position.x, position.y, 1, 1);
    }

    public void update() {
        // Accumulate amount of time that has passed
        elapsedTime += Gdx.graphics.getDeltaTime();
        // Get current frame of animation for the current stateTime
        currentFrame = animation.getKeyFrame(elapsedTime, true);
    }

    public void drawStats(ShapeRenderer shapeMapRenderer) {
        // Need to make this the same as the timer
        shapeMapRenderer.rect(this.getPosition().x - 0.1f, this.getPosition().y + 1.4f, 1.2f, 0.55f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        shapeMapRenderer.rect(this.getPosition().x, this.getPosition().y + 1.5f, 1f, 0.34f, Color.GOLDENROD, Color.GOLDENROD, Color.GOLDENROD, Color.GOLDENROD);
        shapeMapRenderer.rect(this.getPosition().x, this.getPosition().y + 1.5f, 1f, 0.34f, Color.GOLD, Color.GOLD, Color.GOLD, Color.GOLD);
    }



    public Vector2 getPosition() {
        return this.position;
    }

    public void dispose() {
        this.atlas.dispose();
    }

    public boolean getCanBeRendered() {
        return this.canBeRendered;
    }

    public boolean getCanBeDestroyed(){
        return this.canBeDestroyed;
    }

    void removePowerUp() {
        canBeRendered = false;
        canBeDestroyed = true;
    }

    public abstract void invokePower(FireTruck truck);

    public void setPosition(Vector2 position){
        this.position = position;
    }


}
