package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.GUI.Buttons;
import com.mozarellabytes.kroy.Minigame.DanceMove;
import com.mozarellabytes.kroy.Screens.DanceScreen;

/**
 * Handles inputs for DanceScreen
 */
public class DanceScreenInputHandler implements InputProcessor {

    /**
     * Dance screen this class controls inputs for
     */
    final DanceScreen danceScreen;
    final Buttons buttons;

    /**
     * Dance screen constructor
     * @param danceScreen   screen this class controls
     */
    public DanceScreenInputHandler(DanceScreen danceScreen){
        this.danceScreen = danceScreen;
        this.buttons = danceScreen.getButtons();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode){
            case Input.Keys.DOWN:
                danceScreen.setLastMove(DanceMove.DOWN);
                break;
            case Input.Keys.UP:
                danceScreen.setLastMove(DanceMove.UP);
                break;
            case Input.Keys.RIGHT:
                danceScreen.setLastMove(DanceMove.RIGHT);
                break;
            case Input.Keys.LEFT:
                danceScreen.setLastMove(DanceMove.LEFT);
                break;
            case Input.Keys.SPACE:
                danceScreen.useCombo();
                break;
            case Input.Keys.ESCAPE:
                buttons.clickedPauseButton();
                break;
            case Input.Keys.M:
                buttons.clickedSoundButton();
                break;
            case Input.Keys.C:
                buttons.toControlScreen();
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode){
            case Input.Keys.ESCAPE:
                buttons.changePlayState();
                break;
            case Input.Keys.M:
                buttons.changeSound();
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        checkButtonClick(screenX, screenY);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        checkButtonUnclick(screenX, screenY);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private void checkButtonClick(int screenX, int screenY){
        Vector2 screenCoords = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
        buttons.checkButtonClick(screenCoords);
    }


    /** Checks if the user has lifted the mouse over a button and triggers the
     * appropriate action
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     */
    private void checkButtonUnclick(int screenX, int screenY){
        Vector2 screenCoords = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
        buttons.checkButtonUnclick(screenCoords);
    }
}

