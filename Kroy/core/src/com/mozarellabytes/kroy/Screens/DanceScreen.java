package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Entities.Patrol;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Minigame.*;
import com.mozarellabytes.kroy.Utilities.*;
import com.mozarellabytes.kroy.GUI.GUI;

/**
 * The screen for the minigame that triggers when a firetruck meets an ET patrol
 */

public class DanceScreen implements Screen, BeatListener {

    /** Instance of our game that allows us the change screens */
    private final Kroy game;

    private DanceScreenInputHandler danceInputHandler;

    /** Camera to set the projection for the screen */
    private final OrthographicCamera camera;

    /** Object for handling those funky beats */
    private final DanceManager danceMan;

    private Screen previousScreen;
    private boolean hasShownTutorial = false;

    private final Texture targetBoxTexture;

    private FireTruck firetruck;
    private Patrol patrol;
    private Dancer firefighter;
    private Dancer ETDancer;

    private final Vector2 arrowsOrigin;
    private final Vector2 resultLocation;
    private final Vector2 comboLocation;
    private final Vector2 firefighterLocation;
    private final Vector2 etLocation;
    private final Vector2 comboHintLocation;

    private final int ARROW_DISPLACEMENT = 128;
    private final int ARROW_SIZE = 96;

    private DanceResult lastResult = null;


    public DanceScreen(Kroy game, Screen previousScreen, FireTruck firetruck, Patrol patrol) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        this.previousScreen = previousScreen;

        this.danceMan = new DanceManager(120f);
        this.danceMan.subscribeToBeat(this);
        SoundFX.playDanceoffMusic();

        this.patrol = patrol;
        this.firetruck = firetruck;
        this.firefighter = new Dancer((int) firetruck.getHP());
        this.ETDancer = new Dancer((int) patrol.getHP());

        this.targetBoxTexture = new Texture(Gdx.files.internal("sprites/dance/targetBox.png"), true);
        this.targetBoxTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        this.arrowsOrigin = new Vector2(camera.viewportWidth/2-ARROW_SIZE/2, camera.viewportHeight/3);
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
    public void render(float delta)
    {

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
        if (!hasShownTutorial && !((GameScreen)previousScreen).gameState.hasDanceTutorialShown()) {
            this.hasShownTutorial = true;
            ((GameScreen)previousScreen).gameState.setDanceTutorialShown();
            this.game.setScreen(new ControlsScreen(game, this, "dance"));
        } else {
            Gdx.input.setInputProcessor(new DanceScreenInputHandler(this));
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

    private void checkIfOver() {
        if (firefighter.getHealth() <= 0 || ETDancer.getHealth() <= 0) {
            this.firetruck.setHP(firefighter.getHealth());
            this.patrol.setHP(ETDancer.getHealth());
            GUI gui = new GUI(game, (GameScreen) previousScreen);
            Gdx.input.setInputProcessor(new GameInputHandler((GameScreen) previousScreen, gui));
            gui.idleInfoButton();
            SoundFX.stopMusic();
            if (SoundFX.music_enabled) SoundFX.playGameMusic();
            this.game.setScreen(previousScreen);
        }
    }

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
        this.game.shapeRenderer.rect(x-width/2, y, width, height, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        this.game.shapeRenderer.rect(x-width/2 + offset, y + offset, (width - 2*offset) * percentage, height - 2*offset, Color.RED, Color.RED, Color.RED, Color.RED);
    }


    public static float phaseLerp(float phase) {
        return (float) Math.pow(2, 10f * (phase-1));
    }

    public static float scaleDamage(float combo) {
        return (float) (20 * (Math.pow(1.2, combo)-1f));
    }

    public void onBeat() {
        this.ETDancer.updateJive();
    }

    @Override
    public void offBeat() {
        if (firefighter.getTimeInState() > danceMan.getPhase()/2) {
            this.firefighter.setState(DanceMove.WAIT);
        }
        this.ETDancer.updateJive();
    }

    @Override
    public void moveResult(DanceResult result) {
        if (result.equals(DanceResult.WRONG)){
            this.firefighter.damage(20);
            this.ETDancer.startJive();
        }
    }

    public void setLastMove(DanceMove move){
        this.lastResult = this.danceMan.takeMove(move);
        this.firefighter.setState(move);
    }

    public void useCombo(){
        int combo = danceMan.getCombo();
        this.ETDancer.damage((int)scaleDamage(combo));
        this.danceMan.killCombo();
    }

}
