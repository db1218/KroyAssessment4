package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Entities.Patrol;
import com.mozarellabytes.kroy.GameState;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Minigame.*;
import com.mozarellabytes.kroy.Utilities.*;

/**
 * The screen for the minigame that triggers when a firetruck meets an ET patrol
 */

public class DanceScreen implements Screen, BeatListener {

    /** Instance of our game that allows us the change screens */
    private final Kroy game;
    private final GameState gameState;

    private final DanceScreenInputHandler danceInputHandler;

    /** Camera to set the projection for the screen */
    private final OrthographicCamera camera;

    /** Object for handling those funky beats */
    private final DanceManager danceMan;

    private final Screen previousScreen;
    private boolean hasShownTutorial = false;

    private final Texture targetBoxTexture;

    private final FireTruck firetruck;
    private final Patrol patrol;
    private final Dancer firefighter;
    private final Dancer ETDancer;

    private final Vector2 arrowsOrigin;
    private final Vector2 resultLocation;
    private final Vector2 comboLocation;
    private final Vector2 firefighterLocation;
    private final Vector2 etLocation;
    private final Vector2 comboHintLocation;

    private final int ARROW_SIZE = 96;

    private DanceResult lastResult = null;

    /**
     * Constructor for Dance Screen
     *
     * @param game              instance of Game
     * @param gameState         state of the game to know if the game should end after minigame ends
     * @param previousScreen    allows to return to the game screen
     * @param firetruck         the firetruck from the game (fire man in minigame)
     * @param patrol            the patrol from the game
     */
    public DanceScreen(Kroy game, GameState gameState, Screen previousScreen, FireTruck firetruck, Patrol patrol) {
        this.game = game;
        this.gameState = gameState;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        this.previousScreen = previousScreen;

        this.danceMan = new DanceManager(140f);
        this.danceMan.subscribeToBeat(this);

        this.patrol = patrol;
        this.firetruck = firetruck;
        this.firefighter = new Dancer((int) firetruck.getHP());
        this.ETDancer = new Dancer((int) patrol.getHP());

        this.targetBoxTexture = new Texture(Gdx.files.internal("sprites/dance/targetBox.png"), true);
        this.targetBoxTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        this.arrowsOrigin = new Vector2(camera.viewportWidth/2-ARROW_SIZE/2f, camera.viewportHeight/3);
        this.resultLocation = new Vector2(0, camera.viewportHeight/4);
        this.comboLocation = new Vector2(0, camera.viewportHeight/7);
        this.firefighterLocation = new Vector2(camera.viewportWidth/4-256, camera.viewportHeight/5);
        this.etLocation = new Vector2((3*camera.viewportWidth)/4-256, camera.viewportHeight/5);
        this.comboHintLocation = new Vector2(camera.viewportWidth/4, (3*camera.viewportHeight)/5);

        this.danceInputHandler = new DanceScreenInputHandler(this);

    }

    /**
     * Manages all of the updates/checks during the game
     *
     * @param delta The time in seconds since the last render
     */
    @Override
    public void render(float delta) {

        this.danceMan.update(delta);

        checkIfOver();

        this.firefighter.addTimeInState(delta);
        this.ETDancer.addTimeInState(delta);

        if (this.danceMan.hasMissedLastBeat()) {
            if (this.firefighter.getTimeInState() > this.danceMan.getPhase()/4) {
                this.lastResult = DanceResult.MISSED;
            }
        }

        Gdx.gl.glClearColor(51/255f, 34/255f, 99/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        this.game.batch.setProjectionMatrix(camera.combined);

        this.game.batch.begin();

        this.game.batch.draw(firefighter.getTexture("firefighter"), firefighterLocation.x, firefighterLocation.y, 512, 512);
        this.game.batch.draw(ETDancer.getTexture("ET"), etLocation.x, etLocation.y, 512, 512);

        // Render arrows
        int i = 0;
        for (DanceMove move : danceMan.getMoveList()) {
            if (move.getArrowTexture() != null){
                int ARROW_DISPLACEMENT = 128;
                this.game.batch.draw(move.getArrowTexture(), arrowsOrigin.x, arrowsOrigin.y + i * ARROW_DISPLACEMENT - phaseLerp(danceMan.getPhase()) * ARROW_DISPLACEMENT, ARROW_SIZE, ARROW_SIZE);
            }
            i++;
        }

        this.game.batch.draw(targetBoxTexture, arrowsOrigin.x, arrowsOrigin.y, ARROW_SIZE, ARROW_SIZE);

        if (danceMan.getCombo() > 2) {
            this.game.font50.draw(game.batch, "Press [SPACE] to use combo!", comboHintLocation.x, (comboHintLocation.y + danceMan.getBeatProximity()* camera.viewportHeight/32), camera.viewportWidth, 1, false);
        }

        if (lastResult != null) {
            this.game.font120.draw(game.batch, lastResult.toString(), resultLocation.x, resultLocation.y,camera.viewportWidth, 1, false);
        }

        this.game.font60.draw(game.batch, danceMan.getCombo() + "x", comboLocation.x, comboLocation.y, camera.viewportWidth, 1, false);

        this.game.batch.end();

        drawHealthBars();
    }

    @Override
    public void show() {
        SoundFX.decideMusic(this);
        if (!hasShownTutorial && !((GameScreen)previousScreen).gameState.hasDanceTutorialShown()) {
            this.hasShownTutorial = true;
            ((GameScreen)previousScreen).gameState.setDanceTutorialShown();
            this.game.setScreen(new ControlsScreen(game, this, "dance"));
        } else {
            Gdx.input.setInputProcessor(danceInputHandler);
        }
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

    }

    /**
     * Checks if the minigame should end, called every frame
     */
    private void checkIfOver() {
        if (firefighter.getHealth() <= 0) {
            firetruck.setHP(firefighter.getHealth());
            ((GameScreen) previousScreen).checkIfTruckDestroyed(firetruck);
            if (gameState.shouldGameEnd()) {
                gameState.hasGameEnded(game);
            } else {
                goToGameScreen();
            }
        } else if (ETDancer.getHealth() <= 0) {
            patrol.setHP(ETDancer.getHealth());
            goToGameScreen();
        }
    }

    /**
     * Go back to Game screen
     */
    private void goToGameScreen() {
        game.setScreen(previousScreen);
    }

    /**
     * Draw the health bars of the patrol and fire man
     */
    private void drawHealthBars() {
        this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.game.shapeRenderer.setProjectionMatrix(camera.combined);
        drawHealthbar(camera.viewportWidth/4, camera.viewportHeight/5, firefighter.getHealth()/firetruck.type.getMaxHP());
        drawHealthbar((camera.viewportWidth * 3)/4, camera.viewportHeight/5, ETDancer.getHealth()/patrol.type.getMaxHP());
        this.game.shapeRenderer.end();
    }

    /** Draws a health bar
     * @param x The of the healthbar's origin
     * @param y The y of the healthbar's origin
     * @param percentage How full the health bar is where 1f is full, 0f is empty
     * */
    private void drawHealthbar(float x, float y, float percentage) {
        int width = 500;
        int height = 50;
        float offset = height * .2f;
        this.game.shapeRenderer.rect(x-width/2f, y, width, height, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        this.game.shapeRenderer.rect(x-width/2f + offset, y + offset, (width - 2*offset) * percentage, height - 2*offset, Color.RED, Color.RED, Color.RED, Color.RED);
    }

    /**
     * Function for determining how much the arrow should move
     * down the screen
     *
     * @param phase factor in determining how much it should phase
     * @return      distance the arrow should move
     */
    public static float phaseLerp(float phase) {
        return (float) Math.pow(2, 10f * (phase-1));
    }

    /**
     * Scales the damage dealt to patrol
     *
     * @param combo scaling factor
     * @return      amount of damage done
     */
    public static float scaleDamage(float combo) {
        return (float) (20 * (Math.pow(1.2, combo)-1f));
    }

    /**
     * Displays the dance sequence for on beat
     */
    public void onBeat() {
        this.ETDancer.updateJive();
    }

    /**
     * Displays the dance sequence for off beat
     */
    @Override
    public void offBeat() {
        if (firefighter.getTimeInState() > danceMan.getPhase()/2) this.firefighter.setState(DanceMove.WAIT);
        this.ETDancer.updateJive();
    }

    /**
     * Moves the result
     * @param result    how "on time" the player was
     */
    @Override
    public void moveResult(DanceResult result) {
        if (result.equals(DanceResult.WRONG)){
            if (!this.firetruck.inShield()) this.firefighter.damage(20);
            this.ETDancer.startJive();
        }
    }

    /**
     * Updates the move
     * @param move  the move the player took
     */
    public void setLastMove(DanceMove move){
        this.lastResult = this.danceMan.takeMove(move);
        this.firefighter.setState(move);
        //this.kickNum++;
        //kickIt();
    }

    /**
     * Play sound effect
     */
    public void kickIt() {
        int kickNum = 0;
        if (kickNum %2 == 0) {
            SoundFX.sfx_kick.play();
        } else {
            SoundFX.sfx_snare.play();
        }
    }

    /**
     * When space is clicked, combo is used
     */
    public void useCombo(){
        int combo = danceMan.getCombo();
        this.ETDancer.damage((int)scaleDamage(combo));
        this.danceMan.killCombo();
    }

}
