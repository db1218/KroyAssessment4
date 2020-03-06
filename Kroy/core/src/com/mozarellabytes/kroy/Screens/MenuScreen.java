package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.MenuInputHandler;
import com.mozarellabytes.kroy.Utilities.SoundFX;

/** This screen is shown after the splash screen and is
 * where the player can choose to start the game or view
 * the control screen */
public class MenuScreen implements Screen {

    /** The game */
    private final Kroy game;
    public final OrthographicCamera camera;

    /** The menu screen image - see ui/menuscreen_blank_2 */
    private final Texture backgroundImage;

    /** Rectangle containing the position of the play button */
    private final Rectangle newGameButton;

    /** Texture of the start button when it has not been clicked */
    private final Texture newGameIdleTexture;

    /** Texture of the start button when has been clicked */
    private final Texture newGameClickedTexture;

    /** Contains the current state of the start button:
     * startIdleTexture if the start button is not being pressed,
     * startClickedTexture if the start button has been pressed */
    private Texture newGameStartTexture;

    /** Rectangle containing the position of the saves button */
    private final Rectangle savesButton;

    /** Texture of the saves button when it has not been clicked */
    private final Texture savesIdleTexture;

    /** Texture of the saves button when has been clicked */
    private final Texture savesClickedTexture;

    /** Contains the current state of the saves button:
     * savesIdleTexture if the start button is not being pressed,
     * savesClickedTexture if the start button has been pressed */
    private Texture currentSavesTexture;

    /** Rectangle containing the position of the control button */
    private final Rectangle controlsButton;

    /** Texture of the control button when it has not been clicked */
    private final Texture controlsIdleTexture;

    /** Texture of the control button when has been clicked */
    private final Texture controlsClickedTexture;

    /** Contains the current state of the control button:
     * controlsIdleTexture if the control button is not being pressed,
     * controlsClickedTexture if the control button has been pressed */
    private Texture currentControlsTexture;

    /** Rectangle containing the position of the sound button */
    private final Rectangle soundButton;

    /** Texture of the sound on button when it has not been clicked */
    private final Texture soundOnIdleTexture;

    /** Texture of the sound off button when it has not been clicked */
    private final Texture soundOffIdleTexture;

    /** Texture of the sound on button when it has been clicked */
    private final Texture soundOnClickedTexture;

    /** Texture of the sound off button when it has been clicked */
    private final Texture soundOffClickedTexture;
    private Texture currentSoundTexture;

    /** Constructs the MenuScreen
     *
     * @param game  LibGdx game
     */
    public MenuScreen(final Kroy game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

        backgroundImage = new Texture(Gdx.files.internal("menuscreen_blank_2.png"), true);
        backgroundImage.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        newGameIdleTexture = new Texture(Gdx.files.internal("ui/newgame_idle.png"), true);
        newGameIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        newGameClickedTexture = new Texture(Gdx.files.internal("ui/newgame_clicked.png"), true);
        newGameClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        savesIdleTexture = new Texture(Gdx.files.internal("ui/load_idle.png"), true);
        savesIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        savesClickedTexture = new Texture(Gdx.files.internal("ui/load_clicked.png"), true);
        savesClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        controlsIdleTexture = new Texture(Gdx.files.internal("ui/controls_idle.png"), true);
        controlsIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        controlsClickedTexture = new Texture(Gdx.files.internal("ui/controls_clicked.png"), true);
        controlsClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        soundOnIdleTexture = new Texture(Gdx.files.internal("ui/sound_on_idle.png"), true);
        soundOnIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOffIdleTexture = new Texture(Gdx.files.internal("ui/sound_off_idle.png"), true);
        soundOffIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOnClickedTexture = new Texture(Gdx.files.internal("ui/sound_on_clicked.png"), true);
        soundOnClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOffClickedTexture = new Texture(Gdx.files.internal("ui/sound_off_clicked.png"), true);
        soundOffClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        if (SoundFX.music_enabled) {
            SoundFX.sfx_menu.setLooping(true);
            SoundFX.sfx_menu.play();
            currentSoundTexture = soundOffIdleTexture;
        } else {
            currentSoundTexture = soundOnIdleTexture;
        }

        newGameStartTexture = newGameIdleTexture;
        currentSavesTexture = savesIdleTexture;
        currentControlsTexture = controlsIdleTexture;

        newGameButton = new Rectangle();
        newGameButton.width = (float) (newGameStartTexture.getWidth()*0.75);
        newGameButton.height = (float) (newGameStartTexture.getHeight()*0.75);
        newGameButton.x = (int) (camera.viewportWidth/2 - newGameButton.width/2);
        newGameButton.y = (int) ((camera.viewportHeight/2 - newGameButton.height/2) * 0.8);

        savesButton = new Rectangle();
        savesButton.width = (float) (currentSavesTexture.getWidth()*0.75);
        savesButton.height = (float) (currentSavesTexture.getHeight()*0.75);
        savesButton.x = (int) (camera.viewportWidth/2 - savesButton.width/2);
        savesButton.y = (int) ((camera.viewportHeight/2 - savesButton.height/2) * 0.5);

        controlsButton = new Rectangle();
        controlsButton.width = (float) (currentControlsTexture.getWidth()*0.75);
        controlsButton.height = (float) (currentControlsTexture.getHeight()*0.75);
        controlsButton.x = (int) (camera.viewportWidth/2 - controlsButton.width/2);
        controlsButton.y = (int) ((camera.viewportHeight/2 - controlsButton.height/2)*0.2);

        soundButton = new Rectangle();
        soundButton.width = 50;
        soundButton.height = 50;
        soundButton.x = camera.viewportWidth - soundButton.getWidth() - 5;
        soundButton.y = camera.viewportHeight - soundButton.getHeight() - 5;

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new MenuInputHandler(this));
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
        game.batch.draw(newGameStartTexture, newGameButton.x, newGameButton.y, newGameButton.width, newGameButton.height);
        game.batch.draw(currentSavesTexture, savesButton.x, savesButton.y, savesButton.width, savesButton.height);
        game.batch.draw(currentControlsTexture, controlsButton.x, controlsButton.y, controlsButton.width, controlsButton.height);
        game.batch.draw(currentSoundTexture, soundButton.x, soundButton.y, soundButton.width, soundButton.height);
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
        newGameStartTexture.dispose();
        newGameClickedTexture.dispose();
        newGameIdleTexture.dispose();
        currentControlsTexture.dispose();
        controlsClickedTexture.dispose();
        controlsIdleTexture.dispose();
        currentSoundTexture.dispose();
        soundOnIdleTexture.dispose();
        soundOnClickedTexture.dispose();
        soundOffIdleTexture.dispose();
        soundOffClickedTexture.dispose();
        SoundFX.sfx_menu.stop();
    }

    /** Changes the screen from menu screen to game screen */
    public void toDifficultyScreen() {
        game.setScreen(new DifficultyScreen(game));
        this.dispose();
    }

    /** Changes the texture of the start button when it has been clicked on */
    public void clickedNewGameButton() {
        if (SoundFX.music_enabled){
            SoundFX.sfx_button_clicked.play();
        }
        newGameStartTexture = newGameClickedTexture;
    }

    /** Changes the texture of the controls button when it has been clicked on */
    public void clickedControlsButton() {
        if (SoundFX.music_enabled){
            SoundFX.sfx_button_clicked.play();
        }
        currentControlsTexture = controlsClickedTexture;
    }

    /** Changes the texture of the controls button when it has been clicked on */
    public void clickedSavesButton() {
        if (SoundFX.music_enabled){
            SoundFX.sfx_button_clicked.play();
        }
        currentSavesTexture = savesClickedTexture;
    }

    /** Changes the texture of the sound button when it has been clicked on */
    public void clickedSoundButton() {
        if (SoundFX.music_enabled){
            currentSoundTexture = soundOffClickedTexture;
        } else {
            currentSoundTexture = soundOnClickedTexture;
        }
    }

    /** Turns the sound on and off and changes the sound icon accordingly,
     * turns the sound off in the sound was on and turns the sound on if the
     * sound was off */
    public void changeSound() {
        if (SoundFX.music_enabled){
            currentSoundTexture = soundOnIdleTexture;
            SoundFX.stopMusic();
        } else {
            currentSoundTexture = soundOffIdleTexture;
            SoundFX.playMenuMusic();
        }
    }

    /** The texture of the start button when it has not been clicked on */
    public void idleNewGameButton() {
        newGameStartTexture = newGameIdleTexture;
    }

    /** The texture of the control button when it has not been clicked on */
    public void idleControlsButton() {
        currentControlsTexture = controlsIdleTexture;
    }

    /** The texture of the saves button when it has not been clicked on */
    public void idleSavesButton() {
        currentSavesTexture = savesIdleTexture;
    }

    /** The texture of the sound button when it has not been clicked on */
    public void idleSoundButton() {
        if (SoundFX.music_enabled){
            currentSoundTexture = soundOffIdleTexture;
        } else {
            currentSoundTexture = soundOnIdleTexture;
        }
    }

    /** Changes the screen from the menu screen to the control screen */
    public void toControlScreen() {
        game.setScreen(new ControlsScreen(game, this, "menu"));
    }

    /** Changes the screen from the menu screen to the saves screen */
    public void toSavesScreen() {
        game.setScreen(new SaveScreen(game, this));
    }

    public Rectangle getNewGameButton() { return newGameButton; }

    public Rectangle getControlsButton() { return controlsButton; }

    public Rectangle getSoundButton() {return soundButton; }

    public Rectangle getSavesButton() {return savesButton; }
}
