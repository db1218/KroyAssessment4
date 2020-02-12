package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.mozarellabytes.kroy.Utilities.GUI;
import com.mozarellabytes.kroy.Utilities.GameInputHandler;

/**
 * The screen for the minigame that triggers when a firetruck meets an ET patrol
 */



public class DanceScreen implements Screen, BeatListener {

    /** Instance of our game that allows us the change screens */
    private final Kroy game;

    /** Camera to set the projection for the screen */
    private final OrthographicCamera camera;

    /** Object for handling those funky beats */
    private final DanceManager danceMan;

    private Screen previousScreen;
    private boolean hasShownTutorial = false;

    private final Texture arrowUpTexture;
    private final Texture arrowDownTexture;
    private final Texture arrowLeftTexture;
    private final Texture arrowRightTexture;
    private final Texture targetBoxTexture;
    private final Texture waitTexture;
    private final Texture firemanNoneTexture;
    private final Texture firemanWaitTexture;
    private final Texture firemanLeftTexture;
    private final Texture firemanRightTexture;
    private final Texture firemanUpTexture;
    private final Texture firemanDownTexture;
    private final Texture etNoneTexture;
    private final Texture etLeftTexture;
    private final Texture etRightTexture;

    private FireTruck firetruck;
    private Patrol patrol;
    private Dancer firefighter;
    private Dancer etDancer;

    private final int ARROW_DISPLACEMENT = 128;
    private final int ARROW_SIZE = 96;

    private DanceResult lastResult = null;

    public DanceScreen(Kroy game, Screen previousScreen, FireTruck firetruck, Patrol patrol) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        this.previousScreen = previousScreen;

        this.danceMan = new DanceManager(120f);
        danceMan.subscribeToBeat(this);

        System.out.println("Firetruck health: " + firetruck.getHP() + " ET health: " + patrol.getHP());
        this.patrol = patrol;
        this.firetruck = firetruck;
        this.firefighter = new Dancer((int) firetruck.getHP());
        this.etDancer = new Dancer((int) patrol.getHP());

        arrowUpTexture = new Texture(Gdx.files.internal("sprites/dance/arrowUp.png"), true);
        arrowUpTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        arrowDownTexture = new Texture(Gdx.files.internal("sprites/dance/arrowDown.png"), true);
        arrowDownTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        arrowLeftTexture = new Texture(Gdx.files.internal("sprites/dance/arrowLeft.png"), true);
        arrowLeftTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        arrowRightTexture = new Texture(Gdx.files.internal("sprites/dance/arrowRight.png"), true);
        arrowRightTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        targetBoxTexture = new Texture(Gdx.files.internal("sprites/dance/targetBox.png"), true);
        targetBoxTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        waitTexture = new Texture(Gdx.files.internal("sprites/dance/wait.png"), true);
        waitTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        firemanNoneTexture = new Texture(Gdx.files.internal("sprites/dance/firefighter1.png"), true);
        firemanNoneTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        firemanWaitTexture = new Texture(Gdx.files.internal("sprites/dance/firefighter2.png"), true);
        firemanWaitTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        firemanLeftTexture = new Texture(Gdx.files.internal("sprites/dance/firefighter3.png"), true);
        firemanLeftTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        firemanRightTexture = new Texture(Gdx.files.internal("sprites/dance/firefighter4.png"), true);
        firemanRightTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        firemanUpTexture = new Texture(Gdx.files.internal("sprites/dance/firefighter5.png"), true);
        firemanUpTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        firemanDownTexture = new Texture(Gdx.files.internal("sprites/dance/firefighter6.png"), true);
        firemanDownTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        etNoneTexture = new Texture(Gdx.files.internal("sprites/dance/et1.png"), true);
        etNoneTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        etLeftTexture = new Texture(Gdx.files.internal("sprites/dance/et2.png"), true);
        etLeftTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        etRightTexture = new Texture(Gdx.files.internal("sprites/dance/et3.png"), true);
        etRightTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);


        System.out.println("Got to the dance Screen");
    }

    /**
     * Manages all of the updates/checks during the game
     *
     * @param delta The time in seconds since the last render
     */
    @Override
    public void render(float delta)
    {
        danceMan.update(delta);

        if (firefighter.getHealth() <= 0 || etDancer.getHealth() <= 0) {
            this.firetruck.setHP(firefighter.getHealth());
            this.patrol.setHP(etDancer.getHealth());
            GUI gui = new GUI(game, (GameScreen) previousScreen);
            Gdx.input.setInputProcessor(new GameInputHandler((GameScreen) previousScreen, gui));
            gui.idleInfoButton();
            game.setScreen(previousScreen);
        }

        firefighter.addTimeInState(delta);
        etDancer.addTimeInState(delta);

        if (danceMan.hasMissedLastBeat()) {
            if (firefighter.getTimeInState() > danceMan.getPhase()/4) {
                lastResult = DanceResult.MISSED;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            lastResult = danceMan.takeMove(DanceMove.UP);
            firefighter.setState(DanceMove.UP);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            lastResult = danceMan.takeMove(DanceMove.DOWN);
            firefighter.setState(DanceMove.DOWN);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
             lastResult = danceMan.takeMove(DanceMove.LEFT);
            firefighter.setState(DanceMove.LEFT);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
             lastResult = danceMan.takeMove(DanceMove.RIGHT);
            firefighter.setState(DanceMove.RIGHT);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            int combo = danceMan.getCombo();
            etDancer.damage((int)scaleDamage(combo));
            danceMan.killCombo();
            System.out.println("Firetruck health: " + firefighter.getHealth() + " ET health: " + etDancer.getHealth());
        }

        Gdx.gl.glClearColor(51/255f, 34/255f, 99/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        Vector2 arrowsOrigin = new Vector2(camera.viewportWidth/2-ARROW_SIZE/2, camera.viewportHeight/3);
        Vector2 resultLocation = new Vector2(0, camera.viewportHeight/4);
        Vector2 comboLocation = new Vector2(0, camera.viewportHeight/7);
        Vector2 firemanLocation = new Vector2(camera.viewportWidth/4-256, camera.viewportHeight/5);
        Vector2 etLocation = new Vector2((3*camera.viewportWidth)/4-256, camera.viewportHeight/5);
        game.batch.begin();
        // Draw firefighter
        switch (firefighter.getState()) {
            case NONE:
                game.batch.draw(firemanNoneTexture, firemanLocation.x, firemanLocation.y, 512, 512);
                break;
            case WAIT:
                game.batch.draw(firemanWaitTexture, firemanLocation.x, firemanLocation.y, 512, 512);
                break;
            case UP:
                game.batch.draw(firemanUpTexture, firemanLocation.x, firemanLocation.y, 512, 512);
                break;
            case DOWN:
                game.batch.draw(firemanDownTexture, firemanLocation.x, firemanLocation.y, 512, 512);
                break;
            case LEFT:
                game.batch.draw(firemanLeftTexture, firemanLocation.x, firemanLocation.y, 512, 512);
                break;
            case RIGHT:
                game.batch.draw(firemanRightTexture, firemanLocation.x, firemanLocation.y, 512, 512);
                break;
        }

        // Draw et
        switch (etDancer.getState()) {
            case NONE:
                game.batch.draw(etNoneTexture, etLocation.x, etLocation.y, 512, 512);
                break;
            case WAIT:
                break;
            case UP:
                break;
            case DOWN:
                break;
            case LEFT:
                game.batch.draw(etLeftTexture, etLocation.x, etLocation.y, 512, 512);
                break;
            case RIGHT:
                game.batch.draw(etRightTexture, etLocation.x, etLocation.y, 512, 512);
                break;
        }

        // Render arrows
        int i = 0;
        for (DanceMove d : danceMan.getMoveList()) {
            Color c =  game.batch.getColor();
            if (i == 0) {
                //Set transparency for the bottom move
                game.batch.setColor(c.r, c.b, c.g, 1f-phaseLerp(danceMan.getPhase()));
            }
            switch (d) {
                case UP:
            game.batch.draw(arrowUpTexture, arrowsOrigin.x, arrowsOrigin.y + i * ARROW_DISPLACEMENT - phaseLerp(danceMan.getPhase()) * ARROW_DISPLACEMENT, ARROW_SIZE, ARROW_SIZE);
                break;
                case DOWN:
            game.batch.draw(arrowDownTexture, arrowsOrigin.x, arrowsOrigin.y + i * ARROW_DISPLACEMENT - phaseLerp(danceMan.getPhase()) * ARROW_DISPLACEMENT, ARROW_SIZE, ARROW_SIZE);
                break;
                case LEFT:
            game.batch.draw(arrowLeftTexture, arrowsOrigin.x, arrowsOrigin.y + i * ARROW_DISPLACEMENT - phaseLerp(danceMan.getPhase()) * ARROW_DISPLACEMENT, ARROW_SIZE, ARROW_SIZE);
                break;
                case RIGHT:
            game.batch.draw(arrowRightTexture, arrowsOrigin.x, arrowsOrigin.y + i * ARROW_DISPLACEMENT - phaseLerp(danceMan.getPhase()) * ARROW_DISPLACEMENT, ARROW_SIZE, ARROW_SIZE);
                break;
                case WAIT:
//            game.batch.draw(waitTexture, arrowsOrigin.x, arrowsOrigin.y + i * ARROW_DISPLACEMENT - phaseLerp(danceMan.getPhase()) * ARROW_DISPLACEMENT, ARROW_SIZE, ARROW_SIZE);
                break;

            }
            game.batch.setColor(c.r, c.b, c.g, 1f);
            i++;
        }
        game.batch.draw(targetBoxTexture, arrowsOrigin.x, arrowsOrigin.y, ARROW_SIZE, ARROW_SIZE);


        if (lastResult != null) {
            game.font120.draw(game.batch, lastResult.toString(), resultLocation.x, resultLocation.y,camera.viewportWidth, 1, false);
        }

        game.font60.draw(game.batch, danceMan.getCombo() + "x", comboLocation.x, comboLocation.y, camera.viewportWidth, 1, false);
        game.batch.end();
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setProjectionMatrix(camera.combined);
        drawHealthbar(camera.viewportWidth/4, camera.viewportHeight/5, firefighter.getHealth()/firetruck.type.getMaxHP());
        drawHealthbar((camera.viewportWidth * 3)/4, camera.viewportHeight/5, etDancer.getHealth()/patrol.type.getMaxHP());
        game.shapeRenderer.end();
    }

    @Override
    public void show() {
        if (!hasShownTutorial) {
            hasShownTutorial = true;
            game.setScreen(new ControlsScreen(game, this, "dance"));
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

    public float phaseLerp(float phase) {
        return (float) Math.pow(2, 10f * (phase-1));
    }

//        game.shapeRenderer.rect(x + this.selectedW - positionSpacer - outerSpacing - barSpacer, this.selectedY + outerSpacing, whiteW, this.selectedH - outerSpacing*2 - spaceForText, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
//        game.shapeRenderer.rect(x + this.selectedW - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.selectedY + outerSpacing + innerSpacing, whiteW - innerSpacing*2, barHeight, backgroundColour, backgroundColour, backgroundColour, backgroundColour);
//        game.shapeRenderer.rect(this.selectedX + this.selectedW - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.selectedY + outerSpacing + innerSpacing, whiteW - innerSpacing*2, value/maxValue*barHeight, progressColour, progressColour, progressColour, progressColour);

    public float scaleDamage(float combo) {
        return (float) (20 * (Math.pow(1.2, combo)-1f));
    }

    public void onBeat() {
        etDancer.updateJive();
    }

    @Override
    public void offBeat() {
        System.out.println("Offbeat");
        if (firefighter.getTimeInState() > danceMan.getPhase()/2) {
            firefighter.setState(DanceMove.WAIT);
        }
        etDancer.updateJive();
    }

    @Override
    public void moveResult(DanceResult result) {
        if (result == DanceResult.WRONG) {
            firefighter.damage(20);
            etDancer.setJiving(true);
            System.out.println("Firetruck health: " + firefighter.getHealth() + " ET health: " + etDancer.getHealth());
        };
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
        game.shapeRenderer.rect(x-width/2, y, width, height, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        game.shapeRenderer.rect(x-width/2 + offset, y + offset, (width - 2*offset) * percentage, height - 2*offset, Color.RED, Color.RED, Color.RED, Color.RED);
    }
}
