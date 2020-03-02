package com.mozarellabytes.kroy.PowerUp;

import com.badlogic.gdx.Gdx;
import com.mozarellabytes.kroy.Entities.FireTruck;

public class Shield extends PowerUp {

    float timeInShield;
    float elapsedTime;
    FireTruck truck;

    public Shield() {
        super("shield");
        timeInShield = 5;
        elapsedTime = 0;
    }


    @Override
    public void update() {
        super.update();
        if (truck != null) {
            canBeRendered = false;
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
            truck.setShield(false);
            canBeDestroyed = true;
        }
    }



}
