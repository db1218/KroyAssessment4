package PowerUp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public abstract class PowerUp implements Power {

    private Animation<TextureRegion> animation;
    private TextureAtlas atlas;
    private Vector2 position;
    private float elapsedTime;
    private TextureRegion currentFrame;

    private boolean canBeRendered;
    private boolean canBeDestroyed;

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

    public Vector2 getPosition() {
        return this.position;
    }

    public void dispose() {
        this.atlas.dispose();
    }

    public void yop(){Gdx.app.log("power", "pow");}

    public boolean getCanBeRendered() {
        return this.canBeRendered;
    }

    public boolean getCanBeDestroyed(){
        return this.canBeDestroyed;
    }

    public void setCanBeRendered(boolean b){
        this.canBeRendered = b;
    }

    public void setCanBeDestroyed(boolean b){
        this.canBeDestroyed = b;
    }

}
