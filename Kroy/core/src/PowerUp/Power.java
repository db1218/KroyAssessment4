package PowerUp;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public interface Power {

    void render(Batch mapBatch);

    void update();

    Vector2 getPosition();

    void dispose();

    void yop ();
}
