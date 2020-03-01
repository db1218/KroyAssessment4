package PowerUp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;


public class Shield extends PowerUp {

    float timeInShield;
    float elapsedTime;
    FireTruck truck;

    public Shield(Vector2 position) {
        super(position, "shield");
        timeInShield = 5;
        elapsedTime = 0;
    }

    @Override
    public void update() {
        super.update();
        if (this.truck != null) {
            this.setCanBeRendered(false);
            elapsedTime += Gdx.graphics.getRawDeltaTime();
            checkIfFinishedShield();
        }
    }

    @Override
    public void invokePower(FireTruck truck) {
        this.truck = truck;
        truck.setShield(true);
    }


    private void checkIfFinishedShield() {
        if (elapsedTime >= timeInShield){
            this.truck.setShield(false);
            this.elapsedTime = 0;
            this.setCanBeDestroyed(true);
        }
    }



}
