package com.mozarellabytes.kroy.InputHandlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mozarellabytes.kroy.Entities.Fortress;
import com.mozarellabytes.kroy.Entities.Patrol;
import com.mozarellabytes.kroy.GUI.Buttons;
import com.mozarellabytes.kroy.GUI.GUI;
import com.mozarellabytes.kroy.PowerUp.PowerUp;
import com.mozarellabytes.kroy.Screens.GameScreen;

/**
 * Manages inputs for GameScreen. Controls many aspects such as:
 * - Clicking buttons in top left
 * - Clicking on entities
 * - Drawing truck path
 * - Toggling auto attack
 * - Key binds for buttons
 * - Freezing game
 * - Undo/Redo segments of truck path
 */
public class GameInputHandler implements InputProcessor {

    /** The game screen that this input handler controls */
    private final GameScreen gameScreen;

    /** The graphical user interface - contains the buttons */
    private final GUI gui;
    private final Buttons buttons;

    /** Constructs the GameInputHandler
     *
     * @param gameScreen The game screen that this input handler controls
     * @param gui The graphical user interface - contains the buttons
     */
    public GameInputHandler(GameScreen gameScreen, GUI gui) {
        this.gameScreen = gameScreen;
        this.gui = gui;
        this.buttons = gui.getButtons();
    }

    /** Called when a key was pressed
     *
     * This handles toggling sound, the control screen, the pause
     * screen and makes the fire trucks attack a fortress that is
     * within it's range
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                buttons.clickedPauseButton();
                break;
            case Input.Keys.C:
                buttons.toControlScreen();
                break;
            case Input.Keys.M:
                buttons.clickedSoundButton();
                break;
            case Input.Keys.S:
                gameScreen.saveGameState();
                break;
            case Input.Keys.SPACE:
                if (gameScreen.isNotPaused()) gameScreen.changeState(GameScreen.PlayState.FREEZE);
                break;
            case Input.Keys.A:
                if (gameScreen.isNotPaused()) gameScreen.toggleTruckAttack();
                break;
            case Input.Keys.LEFT:
                if (gameScreen.getSelectedTruck() != null) gameScreen.getSelectedTruck().undoSegment();
                break;
            case Input.Keys.RIGHT:
                if (gameScreen.getSelectedTruck() != null) gameScreen.getSelectedTruck().redoSegment();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                buttons.changePlayState();
                break;
            case Input.Keys.M:
                buttons.changeSound();
                break;
        }
        buttons.resetButtons();
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /** Checks whether the user clicks on a firetruck, fortress, button or the end
     * of a firetrucks path
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 clickCoordinates = generateClickCoordinates(screenX, screenY);
        if (gameScreen.isNotPaused()) {
            if (gameScreen.checkClick(clickCoordinates)) {
                gameScreen.getSelectedTruck().resetPath();
                gameScreen.getSelectedTruck().addTileToPathSegment(clickCoordinates);
            } else if (!gameScreen.checkTrailClick(clickCoordinates) &&
                    !checkFortressClick(clickCoordinates) &&
                    !checkPatrolClick(clickCoordinates) &&
                    !checkFireStationClick(clickCoordinates) &&
                    !checkPowerupClick(clickCoordinates)) {
                gameScreen.setSelectedTruck(null);
                gameScreen.setSelectedEntity(null);
            }
        }
        checkButtonClick(screenX, screenY);
        return true;
    }

    /** The user draws a path for the fire truck, if the path is valid the coordinate
     * positions are added to the trucks path
     * @param pointer the pointer for the event.
     * @return whether the input was processed */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (gameScreen.isNotPaused()) {
            if (gameScreen.getSelectedTruck() != null) {
                Vector2 clickCoordinates = generateClickCoordinates(screenX, screenY);
                gameScreen.getSelectedTruck().addTileToPathSegment(clickCoordinates);
            }
        }
        return true;
    }

    /** Check if a user clicks up on a button or if the user draws multiple
     * trucks to end on the same tile
     *
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (gameScreen.isNotPaused()) {
            if (gameScreen.getSelectedTruck() != null) {
                if (!gameScreen.getSelectedTruck().pathSegment.isEmpty()) {
                    if (gameScreen.getStation().doTrucksHaveSameLastTile()) {
                        gameScreen.shortenActiveSegment();
                    }
                    if (gameScreen.getSelectedTruck().canPathSegmentBeAddedToRoute()) {
                        gameScreen.getSelectedTruck().addPathSegmentToRoute();
                        if (gameScreen.getState().equals(GameScreen.PlayState.PLAY))
                            gameScreen.getSelectedTruck().generatePathFromLastSegments();
                    }
                }
            }
        }
        checkButtonUnclick(screenX, screenY);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    /** Maps the position of where the user clicked on the screen to the tile that they clicked on
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @return a Vector2 containing the tile that the user clicked on
     */
    private Vector2 generateClickCoordinates(int screenX, int screenY){
        Vector2 clickCoordinates = new Vector2(screenX, screenY);
        Vector3 position = gameScreen.getCamera().unproject(new Vector3(clickCoordinates.x, clickCoordinates.y, 0));
        return new Vector2((int) position.x, (int) position.y);
    }


    /** Checks if user clicked on a fortress, if it did this fortress
     * becomes the selected entity meaning its stats will be rendered
     * in the top left hand corner
     *
     * @param position2d the tile that was clicked
     * @return <code> true </code> If a fortress has been clicked on
     *         <code> false </code> Otherwise
     */
    private boolean checkFortressClick(Vector2 position2d) {
        for (Fortress fortress : gameScreen.getFortresses()) {
            if (fortress.getArea().contains(position2d)) {
                gameScreen.setSelectedEntity(fortress);
                return true;
            }
        }
        return false;
    }

    /** Checks if user clicked on the fire station, if it did it
     * becomes the selected entity meaning its stats will be rendered
     * in the top left hand corner
     *
     * @param position2d the tile that was clicked
     * @return <code> true </code> If the fire station has been clicked on
     *         <code> false </code> Otherwise
     */
    private boolean checkFireStationClick(Vector2 position2d) {
        if (gameScreen.getStation().getArea().contains(position2d)) {
            gameScreen.setSelectedEntity(gameScreen.getStation());
            return true;
        }
        return false;
    }

    /** Checks if user clicked on a power up, if it did it
     * becomes the selected entity meaning its stats will be rendered
     * in the top left hand corner
     *
     * @param position2d the tile that was clicked
     * @return <code> true </code> If a power up has been clicked on
     *         <code> false </code> Otherwise
     */
    private boolean checkPowerupClick(Vector2 position2d) {
        for (PowerUp powerUp : gameScreen.getPowerUps()) {
            if (powerUp.getPosition().equals(position2d)) {
                gameScreen.setSelectedEntity(powerUp);
                return true;
            }
        }
        return false;
    }

    /** Checks if user clicked on a patrol, if it did this patrol
     * becomes the selected entity meaning its stats will be rendered
     * in the top left hand corner
     *
     * @param position2d the tile that was clicked
     * @return <code> true </code> If a fortress has been clicked on
     *         <code> false </code> Otherwise
     */
    private boolean checkPatrolClick(Vector2 position2d) {
        if (gameScreen.getBossPatrol() != null && gameScreen.getBossPatrol().getRoundedPosition().dst(position2d) < 1) {
            gameScreen.setSelectedEntity(gameScreen.getBossPatrol());
            return true;
        }
        for (Patrol patrol : gameScreen.getPatrols()) {
            if (patrol.getPosition().dst(position2d) < 1) {
                gameScreen.setSelectedEntity(patrol);
                return true;
            }
        }
        return false;
    }

    /** Checks if the user clicked on the home, pause or sound button
     * and changes the sprite accordingly
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     */
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