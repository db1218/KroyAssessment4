package com.mozarellabytes.kroy.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mozarellabytes.kroy.Entities.FireStation;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Entities.Fortress;
import com.mozarellabytes.kroy.Entities.Patrol;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.PowerUp.PowerUp;
import com.mozarellabytes.kroy.Screens.GameScreen;

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

    private final Buttons buttons;

    private final ArrayList<GUIElement> GUIElements;
    private final float padding;
    private final Batch batch;
    private final ShapeRenderer shapeRenderer;

    /**
     * Gap between lines in the stats area
     */
    private final int newLineHeight;

    /** Camera to set the projection for the screen */
    private final OrthographicCamera pauseCamera;

    /** Constructor for GUI
     *
     * @param game          The Kroy game
     * @param gameScreen    Screen where these methods will be rendered
     */
    public GUI(Kroy game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.buttons = new Buttons(gameScreen);
        this.selectedH = 250;
        this.selectedX = 5;
        this.selectedY = Gdx.graphics.getHeight() - 5 - this.selectedH;
        newLineHeight = 20;

        batch = game.batch;
        shapeRenderer = game.shapeRenderer;

        pauseCamera = new OrthographicCamera();
        pauseCamera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

        padding = 5f;
        GUIElements = new ArrayList<>();
        GUIElements.add(new GUIElement(250));
        GUIElements.add(new GUIElement(30));
        GUIElements.add(new GUIElement(30));
        GUIElements.add(new GUIElement(30));
        GUIElements.add(new GUIElement(30));
    }

    /**
     * Render the elements to the screen
     */
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

    /**
     * Render an entity to the screen
     */
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
            } else if (entity instanceof Patrol) {
                Patrol patrol = (Patrol) entity;
                renderSelectedEntityBars(patrol);
                shapeRenderer.end();
                batch.begin();
                renderSelectedEntityText(patrol);
                batch.end();
            } else if (entity instanceof FireStation) {
                FireStation station = (FireStation) entity;
                renderSelectedEntityBars(station);
                shapeRenderer.end();
                batch.begin();
                renderSelectedEntityText(station);
                batch.end();
            } else if (entity instanceof PowerUp) {
                PowerUp powerup = (PowerUp) entity;
                renderSelectedEntityBars(powerup);
                shapeRenderer.end();
                batch.begin();
                renderSelectedEntityText(powerup);
                batch.end();
            }
        } else {
            shapeRenderer.end();
        }
    }

    /**
     * Render element backgrounds to the screen
     */
    private void renderBackgrounds() {
        shapeRenderer.setColor(0, 0, 0, 0.5f);
        float previousY = Gdx.graphics.getHeight();
        for (GUIElement GUIElement : GUIElements) {
            previousY -= GUIElement.getBackground().height + padding;
            shapeRenderer.rect(padding, previousY, GUIElement.getBackground().width, GUIElement.getBackground().height);
        }
    }

    /**
     * Render element text to the screen
     */
    private void renderText() {
        float previousY = Gdx.graphics.getHeight();
        for (GUIElement GUIElement : this.GUIElements) {
            previousY -= GUIElement.getBackground().height + padding;
            game.font19.draw(batch, GUIElement.getText(), padding + 5, previousY + 21);
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

    /**
     * Calls the methods which render the attributes and
     * health bar of a station in the stats area
     *
     * @param station   the FireStation that owns the stats
     *                  that are being displayed
     */
    private void renderSelectedEntityBars(FireStation station) {
        renderSelectedEntityBar(station.getHP(), station.getMaxHP(), Color.RED, Color.FIREBRICK, 1);
    }

    /**
     * Calls the methods which render the attributes and
     * health of a fortress in the stats area
     *
     * @param fortress  in stats area
     */
    private void renderSelectedEntityBars(Fortress fortress) {
        renderSelectedEntityBar(fortress.getHP(), fortress.getType().getMaxHP(), Color.RED, Color.FIREBRICK, 1);
    }

    /**
     * Calls the methods which render the attributes and
     * health of a patrol in the stats area
     *
     * @param patrol  in stats area
     */
    private void renderSelectedEntityBars(Patrol patrol) {
        renderSelectedEntityBar(patrol.getHP(), patrol.getType().getMaxHP(), Color.RED, Color.FIREBRICK, 1);
    }

    /**
     * Calls the methods which render the attributes and
     * health of a power up in the stats area
     *
     * @param powerUp  in stats area
     */
    private void renderSelectedEntityBars(PowerUp powerUp) {
        renderSelectedEntityBar(powerUp.getTimeLeftOnScreen(), powerUp.getTimeOnScreen(), Color.GOLD, Color.GOLDENROD, 1);
    }

    /**
     * Renders the attributes in a vertical layout
     * of the FireTruck
     *
     * @param truck the FireTruck that owns the stats
     *              that are being displayed
     */
    private void renderSelectedEntityText(FireTruck truck) {
        renderEntityName(truck.getType().getName());
        game.font19.draw(batch, "HP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50);
        game.font19.draw(batch, String.format("%.1f", truck.getHP()) + " / " + String.format("%.1f", truck.getType().getMaxHP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLineHeight);
        game.font19.draw(batch, "Reserve: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLineHeight*2);
        game.font19.draw(batch, String.format("%.1f", truck.getReserve()) + " / " + String.format("%.1f", truck.getType().getMaxReserve()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLineHeight*3);
        game.font19.draw(batch, "Speed: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLineHeight*4);
        game.font19.draw(batch, String.format("%.1f", truck.getType().getSpeed()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLineHeight*5);
        game.font19.draw(batch, "Range: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLineHeight*6);
        game.font19.draw(batch, String.format("%.1f", truck.getRange()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLineHeight*7);
        game.font19.draw(batch, "AP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLineHeight*8);
        game.font19.draw(batch, String.format("%.2f", truck.getAP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLineHeight*9);
    }

    /**
     * Renders the attributes in a vertical layout
     * of the FireStation
     *
     * @param station   the FireStation that owns the stats
     *                  that are being displayed
     */
    private void renderSelectedEntityText(FireStation station) {
        renderEntityName("Fire Station");
        game.font19.draw(batch, "HP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50);
        game.font19.draw(batch, String.format("%.1f", station.getHP()) + " / " + String.format("%.1f", station.getMaxHP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLineHeight);
    }

    /**
     * Renders the attributes in a vertical layout
     * of a PowerUp
     *
     * @param powerUp   the Power Upp that owns the stats
     *                  that are being displayed
     */
    private void renderSelectedEntityText(PowerUp powerUp) {
        renderEntityName(powerUp.getName() + " Power up");
        game.font19.draw(batch, "Despawn time: ", this.selectedX + 15, this.selectedY + this.selectedH - 50);
        game.font19.draw(batch, (powerUp.getTimeLeftOnScreen() <= 0) ? "Despawned" : String.format("%.1f", powerUp.getTimeLeftOnScreen()) + " / " + String.format("%.1f", powerUp.getTimeOnScreen()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLineHeight);
        game.font19.draw(batch, "Description: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLineHeight*2);
        game.font19.draw(batch, wrapString(powerUp.getDesc(), 20), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLineHeight*3);
    }

    /**
     * Wraps a string by inserting new lines with
     * a maximum of maxLetters on each line
     *
     * @param string        string to wrap
     * @param maxLetters    maximum chars on a line
     * @return              wrapped string
     */
    private String wrapString(String string, int maxLetters) {
        StringBuilder toReturn = new StringBuilder();
        int index = 0;
        for (String word : string.split(" ")) {
            index += word.length() + 1;
            if (index >= maxLetters) {
                toReturn.append("\n");
                index = word.length() + 1;
            }
            toReturn.append(word).append(" ");
        }
        return toReturn.toString();
    }

    /**
     * Renders the attributes in a vertical layout
     * of the Fortress
     *
     * @param fortress  the Fortress that owns the stats
     *                  that are being displayed
     */
    private void renderSelectedEntityText(Fortress fortress) {
        renderEntityName(fortress.getType().getName());
        game.font19.draw(batch, "HP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50);
        game.font19.draw(batch, String.format("%.1f", fortress.getHP()) + " / " + String.format("%.1f", fortress.getType().getMaxHP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLineHeight);
        game.font19.draw(batch, "Range: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLineHeight*2);
        game.font19.draw(batch, String.format("%.1f", fortress.getType().getRange()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLineHeight*3);
        game.font19.draw(batch, "AP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLineHeight*4);
        game.font19.draw(batch, String.format("%.2f", fortress.getType().getAP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLineHeight*5);
    }

    /**
     * Renders the attributes in a vertical layout
     * of the Patrol
     *
     * @param patrol    the patrol that owns the stats
     *                  that are being displayed
     */
    private void renderSelectedEntityText(Patrol patrol) {
        renderEntityName(patrol.getName());
        game.font19.draw(batch, "HP: ", this.selectedX + 15, this.selectedY + this.selectedH - 50);
        game.font19.draw(batch, String.format("%.1f", patrol.getHP()) + " / " + String.format("%.1f", patrol.getType().getMaxHP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLineHeight);
        game.font19.draw(batch, "Damage: ", this.selectedX + 15, this.selectedY + this.selectedH - 50 - newLineHeight*2);
        game.font19.draw(batch, String.format("%.2f", patrol.getType().getAP()), this.selectedX + 20, this.selectedY + this.selectedH - 50 - newLineHeight*3);
    }

    private void renderEntityName(String name) {
        if (name.length() > 14) game.font19.draw(batch, name, this.selectedX + 10, this.selectedY + this.selectedH - 10);
        else game.font26.draw(batch, name, this.selectedX + 10, this.selectedY + this.selectedH - 10);
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
        shapeRenderer.rect(this.selectedX + GUIElements.get(0).getBackground().width - positionSpacer - outerSpacing - barSpacer, this.selectedY + outerSpacing, 35, this.selectedH - outerSpacing*2 - spaceForText, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        shapeRenderer.rect(this.selectedX + GUIElements.get(0).getBackground().width - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.selectedY + outerSpacing + innerSpacing, 35 - innerSpacing*2, barHeight, backgroundColour, backgroundColour, backgroundColour, backgroundColour);
        shapeRenderer.rect(this.selectedX + GUIElements.get(0).getBackground().width - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.selectedY + outerSpacing + innerSpacing, 35 - innerSpacing*2, value/maxValue*barHeight, progressColour, progressColour, progressColour, progressColour);
    }

    /** Renders the buttons to the game screen */
    public void renderButtons() {
        buttons.renderButtons(batch);
    }

    /** Renders the text to the screen when the game is paused */
    public void renderPauseScreenText() {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(game.font26, "Game paused \n \n Press 'ESC' or the Pause button \n To return to the game");

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


    /**
     * Updates tne difficulty timer on the screen
     *
     * @param time  until damage increase
     */
    public void updateDifficultyTime(float time) {
        GUIElement GUIElement = GUIElements.get(1);
        GUIElement.setText(String.format("Damage Increase: %.1f", time));
        this.GUIElements.set(1, GUIElement);
    }

    /**
     * Updates difficulty multiplier on the screen
     *
     * @param multiplier    the difficulty multiplier
     */
    public void updateDifficultyMultiplier(float multiplier) {
        GUIElement GUIElement = GUIElements.get(2);
        GUIElement.setText("Damage Multiplier: " + Math.round(multiplier) + "x");
        this.GUIElements.set(2, GUIElement);
    }

    /**
     * Updates freeze cooldown on the screen
     *
     * @param cooldown  time until freeze available
     */
    public void updateFreezeCooldown(float cooldown) {
        GUIElement GUIElement = GUIElements.get(3);
        if (cooldown > 0) GUIElement.setText(String.format("Freeze Cooldown: %.1f", cooldown));
        else GUIElement.setText("Freeze available [SPACE]");
        this.GUIElements.set(3, GUIElement);
    }

    /**
     * Updates truck attack whenever the user clicks A
     * @param truckAttack    <code>true</code> if truck attack is enabled
     *                      <code>false</code> otherwise
     */
    public void updateAttackMode(boolean truckAttack) {
        GUIElement GUIElement = GUIElements.get(4);
        if (truckAttack) GUIElement.setText("Truck attack: ON [A]");
        else GUIElement.setText("Truck attack: OFF [A]");
        this.GUIElements.set(4, GUIElement);
    }

    public Buttons getButtons() { return this.buttons; }

}
