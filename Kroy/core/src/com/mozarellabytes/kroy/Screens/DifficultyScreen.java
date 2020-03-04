package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.DifficultyScreenInputHandler;
import com.mozarellabytes.kroy.Utilities.SoundFX;

public class DifficultyScreen implements Screen {

    /** The game */
    private final Kroy game;
    public final OrthographicCamera camera;

    /** The menu screen image - see ui/menuscreen_blank_2 */
    private final Texture backgroundImage;

    /** Rectangle containing the position of the play button */
    private final Rectangle easyButton;

    /** Texture of the start button when it has not been clicked */
    private final Texture easyIdleTexture;

    /** Texture of the start button when has been clicked */
    private final Texture easyClickedTexture;

    /** Contains the current state of the start button:
     * startIdleTexture if the start button is not being pressed,
     * startClickedTexture if the start button has been pressed */
    private Texture currentEasyTexture;


    /** Rectangle containing the position of the control button */
    private final Rectangle mediumButton;

    /** Texture of the control button when it has not been clicked */
    private final Texture mediumIdleTexture;

    /** Texture of the control button when has been clicked */
    private final Texture mediumClickedTexture;

    /** Contains the current state of the control button:
     * controlsIdleTexture if the control button is not being pressed,
     * controlsClickedTexture if the control button has been pressed */
    private Texture currentMediumTexture;

    private final Rectangle hardButton;

    private final Texture hardIdleTexture;

    private final Texture hardClickedTexture;

    private Texture currentHardTexture;

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
    public DifficultyScreen(final Kroy game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

        backgroundImage = new Texture(Gdx.files.internal("menuscreen_blank_2.png"), true);
        backgroundImage.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        easyIdleTexture = new Texture(Gdx.files.internal("ui/controls_idle.png"), true);
        easyIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        easyClickedTexture = new Texture(Gdx.files.internal("ui/controls_clicked.png"), true);
        easyClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        mediumIdleTexture = new Texture(Gdx.files.internal("ui/controls_idle.png"), true);
        mediumIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        mediumClickedTexture = new Texture(Gdx.files.internal("ui/controls_clicked.png"), true);
        mediumClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        hardIdleTexture = new Texture(Gdx.files.internal("ui/controls_idle.png"), true);
        hardIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        hardClickedTexture = new Texture(Gdx.files.internal("ui/controls_clicked.png"), true);
        hardClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        soundOnIdleTexture = new Texture(Gdx.files.internal("ui/sound_on_idle.png"), true);
        soundOnIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOffIdleTexture = new Texture(Gdx.files.internal("ui/sound_off_idle.png"), true);
        soundOffIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOnClickedTexture = new Texture(Gdx.files.internal("ui/sound_on_clicked.png"), true);
        soundOnClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOffClickedTexture = new Texture(Gdx.files.internal("ui/sound_off_clicked.png"), true);
        soundOffClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        // need a new input handler?
        DifficultyScreenInputHandler ih = new DifficultyScreenInputHandler(this);

        if (SoundFX.music_enabled) {
            SoundFX.sfx_menu.setLooping(true);
            SoundFX.sfx_menu.play();
            currentSoundTexture = soundOffIdleTexture;
        } else {
            currentSoundTexture = soundOnIdleTexture;
        }

        currentEasyTexture = easyIdleTexture;
        currentMediumTexture = mediumIdleTexture;
        currentHardTexture = hardIdleTexture;

        easyButton = new Rectangle();
        easyButton.width = (float) (currentEasyTexture.getWidth()*0.75);
        easyButton.height = (float) (currentEasyTexture.getHeight()*0.75);
        easyButton.x = (int) (camera.viewportWidth/4 - easyButton.width/2);
        easyButton.y = (int) ((camera.viewportHeight/2 - easyButton.height/2) * 0.7);

        mediumButton = new Rectangle();
        mediumButton.width = (float) (currentMediumTexture.getWidth()*0.75);
        mediumButton.height = (float) (currentMediumTexture.getHeight()*0.75);
        mediumButton.x = (int) (camera.viewportWidth/2 - mediumButton.width/2);
        mediumButton.y = (int) ((camera.viewportHeight/2 - mediumButton.height/2) * 0.7);

        hardButton = new Rectangle();
        hardButton.width = (float) (currentHardTexture.getWidth()*0.75);
        hardButton.height = (float) (currentHardTexture.getHeight()*0.75);
        hardButton.x = (int) ((camera.viewportWidth/4) * 3 - hardButton.width/2);
        hardButton.y = (int) ((camera.viewportHeight/2 - hardButton.height/2) * 0.7);

        soundButton = new Rectangle();
        soundButton.width = 50;
        soundButton.height = 50;
        soundButton.x = camera.viewportWidth - soundButton.getWidth() - 5;
        soundButton.y = camera.viewportHeight - soundButton.getHeight() - 5;

        Gdx.input.setInputProcessor(ih);

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(51/255f, 34/255f, 99/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(backgroundImage, 0, 0, camera.viewportWidth, camera.viewportHeight);
        game.batch.draw(currentEasyTexture, easyButton.x, easyButton.y, easyButton.width, easyButton.height);
        game.batch.draw(currentMediumTexture, mediumButton.x, mediumButton.y, mediumButton.width, mediumButton.height);
        game.batch.draw(currentHardTexture, hardButton.x, hardButton.y, hardButton.width, hardButton.height);
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

    @Override
    public void dispose() {
        backgroundImage.dispose();
        currentEasyTexture.dispose();
        currentMediumTexture.dispose();
        currentHardTexture.dispose();
        easyClickedTexture.dispose();
        easyIdleTexture.dispose();
        mediumClickedTexture.dispose();
        mediumIdleTexture.dispose();
        hardClickedTexture.dispose();
        hardIdleTexture.dispose();
        currentSoundTexture.dispose();
        soundOnIdleTexture.dispose();
        soundOnClickedTexture.dispose();
        soundOffIdleTexture.dispose();
        soundOffClickedTexture.dispose();
        SoundFX.sfx_menu.stop();
    }

    public void toGameScreen() { game.setScreen(new GameScreen(game)); }


    /** Changes the texture of the start button when it has been clicked on */
    public void clickedEasyButton() {
        if (SoundFX.music_enabled){
            SoundFX.sfx_button_clicked.play();
        }
        currentEasyTexture = easyClickedTexture;
    }

    public void idleEasyButton() { currentEasyTexture = easyIdleTexture; }


    /** Changes the texture of the start button when it has been clicked on */
    public void clickedMediumButton() {
        if (SoundFX.music_enabled){
            SoundFX.sfx_button_clicked.play();
        }
        currentMediumTexture = mediumClickedTexture;
    }

    public void idleMediumButton() {
        currentMediumTexture = mediumIdleTexture;
    }


    /** Changes the texture of the start button when it has been clicked on */
    public void clickedHardButton() {
        if (SoundFX.music_enabled){
            SoundFX.sfx_button_clicked.play();
        }
        currentHardTexture = hardClickedTexture;
    }

    public void idleHardButton() {
        currentHardTexture = hardIdleTexture;
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

    /** The texture of the sound button when it has not been clicked on */
    public void idleSoundButton() {
        if (SoundFX.music_enabled){
            currentSoundTexture = soundOffIdleTexture;
        } else {
            currentSoundTexture = soundOnIdleTexture;
        }
    }


    public Rectangle getEasyButton() { return easyButton; }

    public Rectangle getMediumButton() { return mediumButton; }

    public Rectangle getHardButton() { return hardButton; }

    public Rectangle getSoundButton() {return soundButton; }

}
