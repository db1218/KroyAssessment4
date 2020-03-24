package com.mozarellabytes.kroy.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Screens.GameScreen;
import com.mozarellabytes.kroy.GUI.ButtonBar;
import com.mozarellabytes.kroy.Utilities.SoundFX;

public class Buttons {

    private final ButtonBar parent;

    /** Rectangle containing the homeButton's coordinates, height and width */
    private final Rectangle homeButton;
    /** Texture of the homeButton when it is not being clicked on */
    private final Texture homeButtonIdle;
    /** Texture of the homeButton when it's being clicked */
    private final Texture homeButtonClicked;
    /** Texture of the homeButton that is rendered to the screen */
    private Texture currentHomeTexture;

    /** Rectangle containing the pauseButton's coordinates, height and width */
    private final Rectangle pauseButton;
    /** Texture of the pausebutton when it is not being clicked on */
    private final Texture pauseButtonIdle;
    /** Texture of the pauseButton when it's being clicked */
    private final Texture pauseButtonClicked;
    /** Texture of the pauseButton that is rendered to the screen */
    private Texture currentPauseTexture;

    /** Rectangle containing the saveButton's coordinates, height and width */
    private final Rectangle saveButton;
    /** Texture of the saveButton when it is not being clicked on */
    private final Texture saveButtonIdle;
    /** Texture of the saveButton when it's being clicked */
    private final Texture saveButtonClicked;
    /** Texture of the saveButton that is rendered to the screen */
    private Texture currentSaveTexture;

    /** Rectangle containing the infoButton's coordinates, height and width */
    private final Rectangle infoButton;
    /** Texture of the infoButton when it is not being clicked on */
    private final Texture infoButtonIdle;
    /** Texture of the infoButton when it's being clicked */
    private final Texture infoButtonClicked;
    /** Texture of the infoButton that is rendered to the screen */
    private Texture currentInfoTexture;

    /** Rectangle containing the soundButton's coordinates, height and width */
    private final Rectangle soundButton;
    /** Texture of the soundButton when music is off to turn the music on
     * when it is not being clicked */
    private final Texture soundOnIdleTexture;
    /** Texture of the soundButton when music is on to turn the music off
     * when it is not being clicked */
    private final Texture soundOffIdleTexture;
    /** Texture of the soundButton when music is off and the soundButton is
     * being clicked to turn the sound on*/
    private final Texture soundOnClickedTexture;
    /** Texture of the soundButton when music is on and the soundButton is
     * being clicked to turn the sound off */
    private final Texture soundOffClickedTexture;
    /** Texture of the soundButton that is rendered to the screen */
    private Texture currentSoundTexture;

    public Buttons(ButtonBar screen) {
        homeButtonIdle = new Texture(Gdx.files.internal("ui/home_idle.png"), true);
        homeButtonIdle.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        homeButtonClicked = new Texture(Gdx.files.internal("ui/home_clicked.png"), true);
        homeButtonClicked.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        pauseButtonIdle = new Texture(Gdx.files.internal("ui/pause_idle.png"), true);
        pauseButtonIdle.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        pauseButtonClicked = new Texture(Gdx.files.internal("ui/pause_clicked.png"), true);
        pauseButtonClicked.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        saveButtonIdle = new Texture(Gdx.files.internal("ui/save_idle.png"), true);
        saveButtonIdle.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        saveButtonClicked = new Texture(Gdx.files.internal("ui/save_clicked.png"), true);
        saveButtonClicked.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        infoButtonIdle = new Texture(Gdx.files.internal("ui/info_idle.png"), true);
        infoButtonIdle.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        infoButtonClicked = new Texture(Gdx.files.internal("ui/info_clicked.png"), true);
        infoButtonClicked.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        soundOnIdleTexture = new Texture(Gdx.files.internal("ui/sound_on_idle.png"), true);
        soundOnIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOffIdleTexture = new Texture(Gdx.files.internal("ui/sound_off_idle.png"), true);
        soundOffIdleTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOnClickedTexture = new Texture(Gdx.files.internal("ui/sound_on_clicked.png"), true);
        soundOnClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        soundOffClickedTexture = new Texture(Gdx.files.internal("ui/sound_off_clicked.png"), true);
        soundOffClickedTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        currentHomeTexture = homeButtonIdle;
        currentPauseTexture = pauseButtonIdle;
        currentSaveTexture = saveButtonIdle;
        currentInfoTexture = infoButtonIdle;

        if (SoundFX.music_enabled) {
            currentSoundTexture = soundOffIdleTexture;
        } else {
            currentSoundTexture = soundOnIdleTexture;
        }

        homeButton = new Rectangle(Gdx.graphics.getWidth() - 33, Gdx.graphics.getHeight() - 33, 30, 30);
        soundButton = new Rectangle(Gdx.graphics.getWidth() - 70, Gdx.graphics.getHeight() - 33, 30, 30);
        pauseButton = new Rectangle(Gdx.graphics.getWidth() - 107, Gdx.graphics.getHeight() - 33, 30, 30);
        infoButton = new Rectangle(Gdx.graphics.getWidth() - 144, Gdx.graphics.getHeight() - 33, 30, 30);
        saveButton = new Rectangle(Gdx.graphics.getWidth() - 181, Gdx.graphics.getHeight() - 33, 30, 30);

        this.parent = screen;
    }

    /** Sets the homeButton texture to homeButtonClicked while the homeButton
     * is being clicked on */
    public void clickedHomeButton() {
        if (SoundFX.music_enabled) SoundFX.sfx_button_clicked.play();
        currentHomeTexture = homeButtonClicked;
    }

    /** Sets the infoButton texture to "Idle" if the previous was "Clicked",
     * else it sets it to "Clicked" */
    public void clickedInfoButton() {
        if (SoundFX.music_enabled) SoundFX.sfx_button_clicked.play();
        if (currentInfoTexture == infoButtonIdle) currentInfoTexture = infoButtonClicked;
        else currentInfoTexture = infoButtonIdle;
    }

    /** Sets the soundButton texture to either soundOffClickedTexture or
     * soundOnClickedTexture while the soundButton is being clicked on */
    public void clickedSoundButton() {
        if (SoundFX.music_enabled) currentSoundTexture = soundOffClickedTexture;
        else currentSoundTexture = soundOnClickedTexture;
    }

    /** Sets the pauseButton texture that is rendered to the screen and pauses
     * and unpauses the game */
    public void clickedPauseButton() {
        currentPauseTexture = pauseButtonClicked;

        if (SoundFX.music_enabled){
            if (parent.getState().equals(GameScreen.PlayState.PLAY)) SoundFX.sfx_pause.play();
            else SoundFX.sfx_unpause.play();
        }
    }

    /** Sets the saveButton texture that is rendered to the screen and saves the game */
    public void clickedSaveButton() {
        if (SoundFX.music_enabled) SoundFX.sfx_pause.play();
        if (currentSaveTexture == saveButtonIdle) currentSaveTexture = saveButtonClicked;
        else currentSaveTexture = saveButtonIdle;
    }

    /** Sets the homeButton texture that is rendered to the screen */
    public void idleHomeButton() {
        currentHomeTexture = homeButtonIdle;
    }

    /** Sets the pauseButton texture that is rendered to the screen */
    public void idlePauseButton() {
        currentPauseTexture = pauseButtonIdle;
    }

    /** Sets the saveButton texture that is rendered to the screen */
    public void idleSaveButton() {
        currentSaveTexture = saveButtonIdle;
    }

    public void idleInfoButton() {
        currentInfoTexture = infoButtonIdle;
    }

    /** Sets the soundButton texture that is rendered to the screen */
    public void idleSoundButton() {
        if (SoundFX.music_enabled) currentSoundTexture = soundOffIdleTexture;
        else currentSoundTexture = soundOnIdleTexture;
    }

    /** Toggles the sound, called if 'S' key or the sound button
     * is pressed */
    public void changeSound() {
        if (SoundFX.music_enabled) currentSoundTexture = soundOnIdleTexture;
        else currentSoundTexture = soundOffIdleTexture;
       // SoundFX.toggleMusic();
    }

    public void changeState() {
        if (parent.getState().equals(GameScreen.PlayState.PAUSE)) parent.setState(GameScreen.PlayState.PLAY);
        else if (parent.getState().equals(GameScreen.PlayState.PLAY)) parent.setState(GameScreen.PlayState.PAUSE);
    }

    /**
     * Reset the buttons when game screen appears
     */
    public void resetButtons() {
        idleHomeButton();
        idleInfoButton();
        idlePauseButton();
        idleSoundButton();
        idleSaveButton();
    }

    public void renderButtons(Batch batch) {
        batch.begin();
        batch.draw(currentSoundTexture, soundButton.x, soundButton.y, soundButton.width, soundButton.height);
        batch.draw(currentHomeTexture, homeButton.x, homeButton.y, homeButton.width, homeButton.height);
        batch.draw(currentPauseTexture, pauseButton.x, pauseButton.y, pauseButton.width, pauseButton.height);
        batch.draw(currentSaveTexture, saveButton.x, saveButton.y, saveButton.width, saveButton.height);
        batch.draw(currentInfoTexture, infoButton.x, infoButton.y, infoButton.width, infoButton.height);
        batch.end();
    }

    public void checkButtonUnclick(Vector2 screenCoords) {
        resetButtons();
        if (homeButton.contains(screenCoords)) parent.toHomeScreen();
        if (soundButton.contains(screenCoords)) parent.changeSound();
        if (pauseButton.contains(screenCoords)) changeState();
        if (saveButton.contains(screenCoords)) parent.saveGameState();
        if (infoButton.contains(screenCoords)) parent.toControlScreen();
    }

    public void checkButtonClick(Vector2 screenCoords) {
        if (homeButton.contains(screenCoords)) clickedHomeButton();
        else if (pauseButton.contains(screenCoords)) clickedPauseButton();
        else if (soundButton.contains(screenCoords)) clickedSoundButton();
        else if (infoButton.contains(screenCoords)) clickedInfoButton();
        else if (saveButton.contains(screenCoords)) clickedSaveButton();
    }
}
