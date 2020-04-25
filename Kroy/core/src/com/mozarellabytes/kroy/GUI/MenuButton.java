package com.mozarellabytes.kroy.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mozarellabytes.kroy.Screens.MenuScreen;
import com.mozarellabytes.kroy.Utilities.SoundFX;

/**
 * Buttons which change image when a user
 * clicks on it. This button only has "idle"
 * and "active" to simulate the user clicking
 * the button. Once the button is released,
 * the button returns to an idle state.
 */
public class MenuButton {

    /**
     * Properties of the button
     */
    private final Rectangle area;
    protected Texture currentTexture, activeTexture, idleTexture;

    /**
     * States of the button
     */
    public enum State {
        ACTIVE,
        IDLE
    }

    /**
     * Constructs a menu button
     *
     * @param idleTexturePath   path of the idle image
     * @param activeTexturePath path of the active image
     */
    public MenuButton(String idleTexturePath, String activeTexturePath) {
        this.idleTexture = new Texture(Gdx.files.internal(idleTexturePath), true);
        this.idleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        this.currentTexture = new Texture(Gdx.files.internal(idleTexturePath), true);
        this.currentTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        this.activeTexture = new Texture(Gdx.files.internal(activeTexturePath), true);
        this.activeTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        this.area = new Rectangle(0, 0, currentTexture.getWidth()*0.75f, currentTexture.getHeight()*0.75f);
    }

    /**
     * Changes the state of the button
     *
     * @param state new state of the button
     */
    public void changeState(State state) {
        if (SoundFX.music_enabled && state.equals(State.ACTIVE)) SoundFX.sfx_button_clicked.play();
        currentTexture = (state.equals(State.IDLE)) ? idleTexture : activeTexture;
    }

    /**
     * Sets the x and y coordinates of the button
     * @param x coordinate
     * @param y coordinate
     */
    public void setPosition(int x, int y) {
        this.area.setPosition(x, y);
    }

    public float getX() {
        return this.area.getX();
    }

    public float getY() {
        return this.area.getY();
    }

    public float getWidth() {
        return this.area.getWidth();
    }

    public float getHeight() {
        return this.area.getHeight();
    }

    public Rectangle getRectangle() {
        return this.area;
    }

    public Texture getCurrentTexture() {
        return this.currentTexture;
    }

    /**
     * Objects to dispose of when {@link MenuScreen}
     * is disposed of
     */
    public void dispose() {
        this.idleTexture.dispose();
        this.activeTexture.dispose();
        this.currentTexture.dispose();
    }
}
