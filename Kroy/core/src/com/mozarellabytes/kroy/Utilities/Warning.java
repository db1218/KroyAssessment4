package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

/**
 * This is a pop-up sprite of a warning sign instantiated when a truck collides with a
 * patrol.
 */
public class Warning {

    private final Texture texture;
    private final Vector2 coord;

    /**
     * Constructor for the warning sprite.
     * @param position Where the sprite should be shown in tile coordinates.
     */
    public Warning(Vector2 position) {
        texture = new Texture(Gdx.files.internal("sprites/Patrol/warning.png"));
        this.coord = position;
    }

    /**
     * Render method to show the texture in the given location.
     * @param batch Batch used to draw the texture.
     */
    public void render(Batch batch) {
        batch.draw(texture, coord.x-0.5f, coord.y+0.5f, 2, 2);
    }

    public void dispose() {
        texture.dispose();
    }
}
