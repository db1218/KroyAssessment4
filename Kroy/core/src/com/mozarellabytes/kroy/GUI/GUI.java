package com.mozarellabytes.kroy.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Entities.Fortress;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Screens.GameScreen;
import com.mozarellabytes.kroy.Utilities.SoundFX;

import java.util.ArrayList;

/**
 * This Class is responsible for displaying the majority of the GUI that the
 * user can see and interact with that are apart from the main function of
 * the game i.e. drawing the FireTruck's path. The GUI renders the buttons
 * visible in the top right corner whilst playing the game, along with
 * rendering the stats area in the top left corner when an entity is selected
 * by clicking on it on the map
 */
public class GUI {

    /** LibGdx game */
    private final Kroy game;

    /** Coordinates and dimensions of the stats box */
    private final int selectedX;
    private final int selectedY;
    private final int selectedH;

    /** The screen where the buttons are rendered */
    private final GameScreen gameScreen;

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

    private final ArrayList<Element> elements;
    private final float padding;
    private final Batch batch;
    private final ShapeRenderer shapeRenderer;

    /** Camera to set the projection for the screen */
    private final OrthographicCamera pauseCamera;

    /** Constructor for GUI
     *
     * @param game          The Kroy game
     * @param gameScreen    Screen where these methods will be rendered
     */
    public GUI(Kroy game, GameScreen gameScreen) {
        Gdx.app.log("new GIO", "r");
        this.game = game;
        this.gameScreen = gameScreen;
        this.selectedH = 250;
        int selectedW = 250;
        this.selectedX = 5;
        this.selectedY = Gdx.graphics.getHeight() - 5 - this.selectedH;

        batch = game.batch;
        shapeRenderer = game.shapeRenderer;

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
        currentPauseTexture = saveButtonIdle;
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

        pauseCamera = new OrthographicCamera();
        pauseCamera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

        padding = 5f;
        elements = new ArrayList<>();
        elements.add(new Element(250));
        elements.add(new Element(30));
        elements.add(new Element(30));
        elements.add(new Element(30));
        elements.add(new Element(30));
    }

    public void renderElements() {
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        renderBackgrounds();
        renderEntity();
        Gdx.gl20.glDisable(GL20.GL_BLEND);
        batch.begin();
        renderText();
        batch.end();
    }

    private void renderEntity() {
        Object entity = gameScreen.getSelectedEntity();
        if (entity != null) {
            if (entity instanceof FireTruck) {
                FireTruck truck = (FireTruck) entity;
                renderSelectedEntityBars(truck);
                shapeRenderer.end();
                batch.begin();
                renderSelectedEntityText(truck);
                batch.end();
            } else if (entity instanceof Fortress) {
                Fortress fortress = (Fortress) entity;
                renderSelectedEntityBars(fortress);
                shapeRenderer.end();
                batch.begin();
                renderSelectedEntityText(fortress);
                batch.end();
            }
        } else {
            shapeRenderer.end();
        }
    }

    private void renderBackgrounds() {
        shapeRenderer.setColor(0, 0, 0, 0.5f);
        float previousY = Gdx.graphics.getHeight();
        for (Element element : elements) {
            previousY -= element.getBackground().height + padding;
            shapeRenderer.rect(padding, previousY, element.getBackground().width, element.getBackground().height);
        }
    }

    private void renderText() {
        float previousY = Gdx.graphics.getHeight();
        for (Element element : this.elements) {
            previousY -= element.getBackground().height + padding;
            game.font19.draw(batch, element.getText(), padding + 5, previousY + 21);
        }
    }

    /**
     * Calls the methods which render the attributes and
     * health/reserve bars of a truck in the stats area
     *
     * @param truck the FireTruck that owns the stats
     *              that are being displayed
     */
    private void renderSelectedEntityBars(FireTruck truck) {
        renderSelectedEntityBar(truck.getHP(), truck.getType().getMaxHP(), Color.RED, Color.FIREBRICK, 1);
        renderSelectedEntityBar(truck.getReserve(), truck.getType().getMaxReserve(), Color.CYAN, Color.BLUE, 2);
    }

    private void renderSelectedEntityBars(Fortress fortress) {
        renderSelectedEntityBar(fortress.getHP(), fortress.getFortressType().getMaxHP(), Color.RED, Color.FIREBRICK, 1);
    }

    /**
     * Renders the attributes in a vertical layout
     * of the FireTruck
     *
     * @param truck the FireTruck that owns the stats
     *              that are being displayed
     */
    private void renderSelectedEntityText(FireTruck truck) {
        int newLine = 20;
        game.font26.draw(batch, truck.getType().getName(), this.selectedX + 10, this.selectedY + this.selectedH - 10);
        game.font19.draw(batch, "HP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50);
        game.font19.draw(batch, String.format("%.1f", truck.getHP()) + " / " + String.format("%.1f", truck.getType().getMaxHP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine);
        game.font19.draw(batch, "Reserve: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*2);
        game.font19.draw(batch, String.format("%.1f", truck.getReserve()) + " / " + String.format("%.1f", truck.getType().getMaxReserve()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*3);
        game.font19.draw(batch, "Speed: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*4);
        game.font19.draw(batch, String.format("%.1f", truck.getType().getSpeed()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*5);
        game.font19.draw(batch, "Range: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*6);
        game.font19.draw(batch, String.format("%.1f", truck.getRange()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*7);
        game.font19.draw(batch, "AP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*8);
        game.font19.draw(batch, String.format("%.2f", truck.getAP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*9);
    }

    /**
     * Renders the attributes in a vertical layout
     * of the Fortress
     *
     * @param fortress  the Fortress that owns the stats
     *                  that are being displayed
     */
    private void renderSelectedEntityText(Fortress fortress) {
        int newLine = 20;
        if(fortress.getFortressType().getName().length() > 14) game.font19.draw(batch, fortress.getFortressType().getName(), this.selectedX + 10, this.selectedY + this.selectedH - 10);
        else game.font26.draw(batch, fortress.getFortressType().getName(), this.selectedX + 10, this.selectedY + this.selectedH - 10);
        game.font19.draw(batch, "HP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50);
        game.font19.draw(batch, String.format("%.1f", fortress.getHP()) + " / " + String.format("%.1f", fortress.getFortressType().getMaxHP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine);
        game.font19.draw(batch, "Range: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*2);
        game.font19.draw(batch, String.format("%.1f", fortress.getFortressType().getRange()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*3);
        game.font19.draw(batch, "AP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLine*4);
        game.font19.draw(batch, String.format("%.2f", fortress.getFortressType().getAP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLine*5);
    }

    /**
    * Renders the stat bars which are currently used to
    * show the health/reserve of trucks and health of
    * fortresses. The integers inside the method that
    * have values set to them are customisable to get
    * the desired layout/formatting of the bars
    *  @param value             the value towards the goal
    * @param maxValue          the goal
    * @param progressColour    the colour of the value bar
    * @param backgroundColour  the colour behind the value bar
    * @param position          the 'bar number' to allow multiple
    *                          bars along side each other
    *                          (1 to infinity)
    */
    private void renderSelectedEntityBar(float value, float maxValue, Color progressColour, Color backgroundColour, int position) {
        int outerSpacing = 10;
        int innerSpacing = 5;
        int spaceForText = 35;
        int barHeight = this.selectedH - outerSpacing*2 - innerSpacing*2 - spaceForText;
        int positionSpacer = position * 35;
        int barSpacer = 0;
        if (position > 1) barSpacer = 5;
        shapeRenderer.rect(this.selectedX + elements.get(0).getBackground().width - positionSpacer - outerSpacing - barSpacer, this.selectedY + outerSpacing, 35, this.selectedH - outerSpacing*2 - spaceForText, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        shapeRenderer.rect(this.selectedX + elements.get(0).getBackground().width - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.selectedY + outerSpacing + innerSpacing, 35 - innerSpacing*2, barHeight, backgroundColour, backgroundColour, backgroundColour, backgroundColour);
        shapeRenderer.rect(this.selectedX + elements.get(0).getBackground().width - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.selectedY + outerSpacing + innerSpacing, 35 - innerSpacing*2, value/maxValue*barHeight, progressColour, progressColour, progressColour, progressColour);
    }

    /** Renders the buttons to the game screen */
    public void renderButtons() {
        batch.begin();
        batch.draw(currentSoundTexture, soundButton.x, soundButton.y, soundButton.width, soundButton.height);
        batch.draw(currentHomeTexture, homeButton.x, homeButton.y, homeButton.width, homeButton.height);
        batch.draw(currentPauseTexture, pauseButton.x, pauseButton.y, pauseButton.width, pauseButton.height);
        batch.draw(currentSaveTexture, saveButton.x, saveButton.y, saveButton.width, saveButton.height);
        batch.draw(currentInfoTexture, infoButton.x, infoButton.y, infoButton.width, infoButton.height);
        batch.end();
    }

    /** Renders the text to the screen when the game is paused */
    public void renderPauseScreenText() {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(game.font26, "Game paused \n");
        layout.setText(game.font26, "Press 'ESC' or the Pause button \n To return to the game");

        batch.setProjectionMatrix(pauseCamera.combined);
        batch.begin();
        game.font26.draw(batch, layout,pauseCamera.viewportWidth/2 - layout.width/2f, pauseCamera.viewportHeight/2);
        batch.end();
    }

    /**
     * Renders the "Game Frozen text" with the time remaining
     * of the freeze displayed too
     *
     * @param freezeCooldown    time of freeze remaining
     */
    public void renderFreezeScreenText(float freezeCooldown) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(game.font26, String.format("Game Frozen! \n You have %.1f seconds to draw your truck paths \n Or press space when you're ready to go", freezeCooldown));

        batch.setProjectionMatrix(pauseCamera.combined);
        batch.begin();
        game.font26.draw(batch, layout,pauseCamera.viewportWidth/2 - layout.width/2f, pauseCamera.viewportHeight - 25);
        batch.end();
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
        if (SoundFX.music_enabled) SoundFX.sfx_button_clicked.play();
        if (gameScreen.getState().equals(GameScreen.PlayState.PLAY)) {
            currentPauseTexture = pauseButtonClicked;
            if (SoundFX.music_enabled) SoundFX.sfx_pause.play();
        } else {
            currentPauseTexture = pauseButtonIdle;
            if (SoundFX.music_enabled) SoundFX.sfx_unpause.play();
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
        SoundFX.toggleMusic(gameScreen);
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

    /**
     * Updates tne difficulty timer on the screen
     *
     * @param time  until damage increase
     */
    public void updateDifficultyTime(float time) {
        Element element = elements.get(1);
        element.setText(String.format("Damage Increase: %.1f", time));
        this.elements.set(1, element);
    }

    /**
     * Updates difficulty multiplier on the screen
     *
     * @param multiplier    the difficulty multiplier
     */
    public void updateDifficultyMultiplier(float multiplier) {
        Element element = elements.get(2);
        element.setText("Damage Multiplier: " + Math.round(multiplier) + "x");
        this.elements.set(2, element);
    }

    /**
     * Updates freeze cooldown on the screen
     *
     * @param cooldown  time until freeze available
     */
    public void updateFreezeCooldown(float cooldown) {
        Element element = elements.get(3);
        if (cooldown > 0) element.setText(String.format("Freeze Cooldown: %.1f", cooldown));
        else element.setText("Freeze available [SPACE]");
        this.elements.set(3, element);
    }

    /**
     * Updates truck attack whenever the user clicks A
     * @param truckAttack    <code>true</code> if truck attack is enabled
     *                      <code>false</code> otherwise
     */
    public void updateAttackMode(boolean truckAttack) {
        Element element = elements.get(4);
        if (truckAttack) element.setText("Truck attack: ON [A]");
        else element.setText("Truck attack: OFF [A]");
        this.elements.set(4, element);
    }

    public Rectangle getHomeButton() { return this.homeButton; }

    public Rectangle getSoundButton() { return this.soundButton; }

    public Rectangle getSaveButton() { return this.saveButton; }

    public Rectangle getPauseButton() { return this.pauseButton; }

    public Rectangle getInfoButton() { return this.infoButton; }
}
