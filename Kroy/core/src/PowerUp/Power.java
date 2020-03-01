package PowerUp;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;

public interface Power {

    void render(Batch mapBatch);

    void update();

    Vector2 getPosition();

    void dispose();

    void invokePower(FireTruck truck);

    void setCanBeRendered(boolean b);

    boolean getCanBeRendered();

    void setCanBeDestroyed(boolean b);

    boolean getCanBeDestroyed();
}
