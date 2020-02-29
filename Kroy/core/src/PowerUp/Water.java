package PowerUp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Water extends PowerUp{

    public Water(Vector2 position) {
        super(position, "water");
    }

    public void yop() {
        Gdx.app.log("water", "h");
    }
}
