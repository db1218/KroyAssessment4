package com.mozarellabytes.kroy.PowerUp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Warning {

    private Texture texture;
    private Vector2 coord;

    public Warning(Vector2 position) {
        texture = new Texture(Gdx.files.internal("sprites/Patrol/warning.png"));
        this.coord = position;
    }

    public void render(Batch batch) {
        batch.draw(texture, coord.x-0.5f, coord.y+0.5f, 2, 2);
    }

    public void dispose() {
        texture.dispose();
    }
}
