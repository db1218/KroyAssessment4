package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mozarellabytes.kroy.GUI.MenuButton;
import com.mozarellabytes.kroy.GUI.MenuToggleButton;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.InputHandlers.MenuInputHandler;
import com.mozarellabytes.kroy.Utilities.SoundFX;
import com.mozarellabytes.kroy.GUI.MenuButton.State;
import com.mozarellabytes.kroy.GUI.MenuToggleButton.ToggleState;

/** This screen is shown after the splash screen and is
 * where the player can choose to start the game or view
 * the control screen */
public class MenuScreen implements Screen {

    /** The game */
    private final Kroy game;
    private final MenuInputHandler inputHandler;
    public final OrthographicCamera camera;

    /** The menu screen image - see ui/menuscreen_blank_2 */
    private final Texture backgroundImage;

    /** Custom Menu buttons */
    private final MenuButton newGameButton;
    private final MenuButton savesButton;
    private final MenuButton controlsButton;
    private final MenuToggleButton soundButton;

    /**
     * Constructs the MenuScreen
     *
     * @param game  LibGdx game
     */
    public MenuScreen(final Kroy game) {
        this.game = game;
        inputHandler = new MenuInputHandler(this);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

        backgroundImage = new Texture(Gdx.files.internal("menuscreen_new.png"), true);
        backgroundImage.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        newGameButton = new MenuButton("ui/newgame_idle.png", "ui/newgame_clicked.png");
        savesButton = new MenuButton("ui/load_idle.png", "ui/load_clicked.png");
        controlsButton = new MenuButton("ui/controls_idle.png", "ui/controls_clicked.png");
        soundButton = new MenuToggleButton("ui/sound_on_idle.png", "ui/sound_on_clicked.png", "ui/sound_off_idle.png", "ui/sound_off_clicked.png");

        if (SoundFX.music_enabled) {
            SoundFX.sfx_menu.setLooping(true);
            SoundFX.sfx_menu.play();
            soundButton.setToggleState(ToggleState.ON);
        } else {
            soundButton.setToggleState(ToggleState.OFF);
        }

        newGameButton.setPosition((int) (camera.viewportWidth/2 - newGameButton.getWidth()/2),
                (int) ((camera.viewportHeight/2 - newGameButton.getHeight()/2) * 0.8));

        savesButton.setPosition((int) (camera.viewportWidth/2 - savesButton.getWidth()/2),
                (int) ((camera.viewportHeight/2 - savesButton.getHeight()/2) * 0.5));

        controlsButton.setPosition((int) (camera.viewportWidth/2 - controlsButton.getWidth()/2),
                (int) ((camera.viewportHeight/2 - controlsButton.getHeight()/2) * 0.2));

        soundButton.setPosition((int) (camera.viewportWidth - soundButton.getWidth() - 5),
                (int) (camera.viewportHeight - soundButton.getHeight() - 5));

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputHandler);
        SoundFX.decideMusic(this);
    }

    /** Renders the menu screen consisting of the background and the start, controls and sound buttons.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(51/255f, 34/255f, 99/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(backgroundImage, 0, 0, camera.viewportWidth, camera.viewportHeight);
        game.batch.draw(newGameButton.getCurrentTexture(), newGameButton.getX(), newGameButton.getY(), newGameButton.getWidth(), newGameButton.getHeight());
        game.batch.draw(savesButton.getCurrentTexture(), savesButton.getX(), savesButton.getY(), savesButton.getWidth(), savesButton.getHeight());
        game.batch.draw(controlsButton.getCurrentTexture(), controlsButton.getX(), controlsButton.getY(), controlsButton.getWidth(), controlsButton.getHeight());
        game.batch.draw(soundButton.getCurrentTexture(), soundButton.getX(), soundButton.getY(), soundButton.getWidth(), soundButton.getHeight());
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    /** Called when this screen should release all resources. */
    @Override
    public void dispose() {
        backgroundImage.dispose();
        savesButton.dispose();
        controlsButton.dispose();
        newGameButton.dispose();
        soundButton.dispose();
    }

    /** Changes the screen from menu screen to game screen */
    public void toDifficultyScreen() {
        game.setScreen(new DifficultyScreen(game));
        this.dispose();
    }

    /** Changes the texture of the start button when it has been clicked on */
    public void clickedNewGameButton() {
        newGameButton.changeState(State.ACTIVE);
    }

    /** Changes the texture of the controls button when it has been clicked on */
    public void clickedControlsButton() {
        controlsButton.changeState(State.ACTIVE);
    }

    /** Changes the texture of the controls button when it has been clicked on */
    public void clickedSavesButton() {
        savesButton.changeState(State.ACTIVE);
    }

    /** Changes the texture of the sound button when it has been clicked on */
    public void clickedSoundButton() {
        soundButton.changeState(State.ACTIVE);
    }

    /** Turns the sound on and off and changes the sound icon accordingly,
     * turns the sound off in the sound was on and turns the sound on if the
     * sound was off */
    public void changeSound() {
        if (SoundFX.music_enabled) soundButton.setToggleState(ToggleState.OFF);
        else soundButton.setToggleState(ToggleState.ON);
        SoundFX.toggleMusic(this);
    }

    /** The texture of the start button when it has not been clicked on */
    public void idleNewGameButton() {
        newGameButton.changeState(State.IDLE);
    }

    /** The texture of the control button when it has not been clicked on */
    public void idleControlsButton() {
        controlsButton.changeState(State.IDLE);
    }

    /** The texture of the saves button when it has not been clicked on */
    public void idleSavesButton() {
        savesButton.changeState(State.IDLE);
    }

    /** The texture of the sound button when it has not been clicked on */
    public void idleSoundButton() {
        soundButton.changeState(State.IDLE);
    }

    /** Changes the screen from the menu screen to the control screen */
    public void toControlScreen() {
        game.setScreen(new ControlsScreen(game, this, "menu"));
    }

    /** Changes the screen from the menu screen to the saves screen */
    public void toSavesScreen() {
        if (Gdx.files.internal("saves/").list().length > 0) {
            game.setScreen(new SaveScreen(game, this));
        }
    }

    public Rectangle getNewGameButton() {
        return newGameButton.getRectangle();
    }

    public Rectangle getControlsButton() {
        return controlsButton.getRectangle();
    }

    public Rectangle getSoundButton() {
        return soundButton.getRectangle();
    }

    public Rectangle getSavesButton() {
        return savesButton.getRectangle();
    }
}
