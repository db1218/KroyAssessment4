package PowerUp;

import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;

public class Heart extends PowerUp {

    public Heart(Vector2 position) {
        super(position, "heart");
    }

    @Override
    public void invokePower(FireTruck truck) {
        int fullHP = (int)truck.type.getMaxHP();
        truck.setHP(fullHP);
        removePowerUp();
    }

    private void removePowerUp() {
        setCanBeRendered(false);
        setCanBeDestroyed(true);
    }

}
