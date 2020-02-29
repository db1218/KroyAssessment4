package PowerUp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Shield extends PowerUp {

    public Shield(Vector2 position) {
        super(position, "shield");
    }

    public void yop() {
        Gdx.app.log("shield", "h");
    }

}
