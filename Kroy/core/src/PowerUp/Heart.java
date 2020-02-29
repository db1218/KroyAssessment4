package PowerUp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Heart extends PowerUp {

    public Heart(Vector2 position) {
        super(position, "heart");
    }

    public void yop() {
        Gdx.app.log("heart", "h");
    }
}
