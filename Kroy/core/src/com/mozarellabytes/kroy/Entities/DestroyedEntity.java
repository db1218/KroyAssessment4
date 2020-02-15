package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;


public class DestroyedEntity {
    /** The texture to be displayed on the map
     *
     */
    private final Texture deadTexture;

    /** A Rectangle containing the location and size of the texture on the map
     *
     */
    private final Rectangle area;

    /** Constuctor for DestroyedEntity class
     *
     * @param deadTexture The texture to be displayed when the entity is destroyed
     * @param area The size and location of the destroyed entity on the map
     */
    public DestroyedEntity(Texture deadTexture, Rectangle area){
        this.deadTexture = deadTexture;
        this.area = area;
    }

    /** Constructor for DestroyedEntity class
     *
     * @param deadTexture The texture to be displayed when the entity is destroyed
     * @param x The x location of the entity on the map
     * @param y The y location of the entity on the map
     * @param width The width of the entity's texture
     * @param height The height on the entity's texture.
     */
    public DestroyedEntity(Texture deadTexture, float x, float y, float width, float height){
        this.deadTexture = deadTexture;
        this.area = new Rectangle(x, y, width, height);
    }

    /** Draws the destroyed entities texture on the map
     *
     * @param mapBatch the renderer in line with the map
     */
    public void draw(Batch mapBatch){
        mapBatch.draw(this.deadTexture, this.area.x, this.area.y, this.area.width, this.area.width);
    }
}
