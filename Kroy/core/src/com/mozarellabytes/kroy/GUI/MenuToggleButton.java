package com.mozarellabytes.kroy.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mozarellabytes.kroy.Screens.MenuScreen;
import com.mozarellabytes.kroy.Utilities.SoundFX;
import javafx.scene.control.Toggle;

/**
 * Extension of {@link MenuButton} which allows
 * for the button to change between 4 states:
 * - Idle state 1
 * - Clicked state 1
 * then when released...
 * - Idle state 2
 * - Clicked state 2
 * This acts as a toggle button, which changes
 * state each time it is clicked.
 */
public class MenuToggleButton extends MenuButton {

    /**
     * Properties of the toggle button.
     */
    private Texture activeTexture2, idleTexture2;
    private ToggleState toggleState;

    /**
     * Two states of a toggle button
     */
    public enum ToggleState {
        ON,
        OFF
    }

    /**
     * Constructs a Toggle Button, with the first textures
     * being set to the {@link MenuButton} textures, then
     * the second textures are kept in this class.
     *
     * @param idleTexturePath1      first idle texture path
     * @param activeTexturePath1    first active texture path
     * @param idleTexturePath2      second idle texture path
     * @param activeTexturePath2    second active texture path
     */
    public MenuToggleButton(String idleTexturePath1, String activeTexturePath1, String idleTexturePath2, String activeTexturePath2) {
        super(idleTexturePath1, activeTexturePath1);
        this.idleTexture2 = new Texture(Gdx.files.internal(idleTexturePath2), true);
        this.idleTexture2.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        this.activeTexture2 = new Texture(Gdx.files.internal(activeTexturePath2), true);
        this.activeTexture2.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
    }

    /**
     * Changes the state of the button
     *
     * @param state new state of the button
     */
    @Override
    public void changeState(State state) {
        if (state.equals(State.ACTIVE)) setClicked();
        else setIdle();
    }

    /**
     * Sets the clicked texture, depending
     * on the toggle state + plays sfx
     */
    private void setClicked() {
        if (SoundFX.music_enabled) SoundFX.sfx_button_clicked.play();
        super.currentTexture = (toggleState.equals(ToggleState.ON)) ? activeTexture : activeTexture2;
    }

    /**
     * Sets the idle texture, depending
     * on the toggle state
     */
    private void setIdle() {
        super.currentTexture = (toggleState.equals(ToggleState.ON)) ? idleTexture : idleTexture2;
    }

    /**
     * Sets the toggle state of the button
     * and sets the button to its idle texture
     *
     * @param toggleState   of the button
     */
    public void setToggleState(ToggleState toggleState) {
        this.toggleState = toggleState;
        this.setIdle();
    }

    /**
     * Objects to dispose of when {@link MenuScreen}
     * is disposed of
     */
    public void dispose() {
        this.idleTexture2.dispose();
        this.activeTexture2.dispose();
    }
}
