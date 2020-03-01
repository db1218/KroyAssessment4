package PowerUp;

import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;

public class Water extends PowerUp {

    public Water(Vector2 position) {
        super(position, "water");
    }

    @Override
    public void invokePower(FireTruck truck) {
        refillReserve(truck);
        removePowerUp();
    }

    private void refillReserve(FireTruck truck) {
        int maxReserve = (int)truck.type.getMaxReserve();
        int currentReserve = (int) truck.getReserve();
        truck.refill(maxReserve - currentReserve);
    }

}
