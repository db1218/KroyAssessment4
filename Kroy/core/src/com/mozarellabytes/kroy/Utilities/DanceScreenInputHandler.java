package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mozarellabytes.kroy.Minigame.DanceMove;
import com.mozarellabytes.kroy.Screens.DanceScreen;

public class DanceScreenInputHandler implements InputProcessor {

    DanceScreen danceScreen;

    public DanceScreenInputHandler(DanceScreen danceScreen){
        Gdx.app.log("keycode", "create");
        this.danceScreen = danceScreen;
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
                Gdx.app.exit();
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
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
}
