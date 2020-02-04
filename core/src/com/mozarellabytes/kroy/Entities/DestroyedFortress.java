package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class DestroyedFortress {
    private final Texture deadTexture;
    private final Rectangle area;
    public DestroyedFortress(Texture deadTexture, Rectangle area){
        this.deadTexture = deadTexture;
        this.area = area;
    }

    public void draw(Batch mapBatch){
        mapBatch.draw(this.deadTexture, this.area.x, this.area.y, this.area.width, this.area.width);
    }
}
