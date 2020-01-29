package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Minigame.DanceMove;
import com.mozarellabytes.kroy.Minigame.DanceManager;
import com.mozarellabytes.kroy.Minigame.DanceResult;
import com.mozarellabytes.kroy.Utilities.GUI;

/**
 * The screen for the minigame that triggers when a firetruck meets an ET patrol
 */



public class DanceScreen implements Screen {

    /** Instance of our game that allows us the change screens */
    private final Kroy game;

    /** Camera to set the projection for the screen */
    private final OrthographicCamera camera;

    /** Object for handling those funky beats */
    private final DanceManager danceMan;

    private Screen previousScreen;

    private final Texture arrowUpTexture;
    private final Texture arrowDownTexture;
    private final Texture arrowLeftTexture;
    private final Texture arrowRightTexture;
    private final Texture targetBoxTexture;
    private final Texture waitTexture;

    private final int FIREMAN_MAX_HEALTH = 10;
    private int firemanHealth = FIREMAN_MAX_HEALTH;
    private final int ET_MAX_HEALTH = 10;
    private int etHealth = ET_MAX_HEALTH;

    private final int ARROW_DISPLACEMENT = 128;
    private final int ARROW_SIZE = 96;

    private DanceResult lastResult = null;

    public DanceScreen(Kroy game, Screen previousScreen) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        this.previousScreen = previousScreen;

        this.danceMan = new DanceManager(120f);

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

        if (firemanHealth <= 0 || etHealth <= 0) {
            game.setScreen(previousScreen);
        }

        int danceTimer = 0;

        if (danceMan.hasMissedLastBeat()) {
            lastResult = DanceResult.MISSED;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            lastResult = danceMan.takeMove(DanceMove.UP);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            lastResult = danceMan.takeMove(DanceMove.DOWN);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
             lastResult = danceMan.takeMove(DanceMove.LEFT);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
             lastResult = danceMan.takeMove(DanceMove.RIGHT);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            int combo = danceMan.getCombo();
            etHealth -= combo;
            danceMan.killCombo();
            System.out.println("ET Health: " + etHealth);
        }

        Gdx.gl.glClearColor(51/255f, 34/255f, 99/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        Vector2 arrowsOrigin = new Vector2(camera.viewportWidth/2-ARROW_SIZE/2, camera.viewportHeight/3);
        Vector2 resultLocation = new Vector2(0, camera.viewportHeight/4);
        Vector2 comboLocation = new Vector2(0, camera.viewportHeight/7);
        game.batch.begin();
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
        drawHealth(0, 0, danceMan.scorer.getPlayerHealth());

        game.batch.end();
    }

    @Override
    public void show() {

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

    public void drawHealth(int x, int y, int health) {
//        game.shapeRenderer.rect(x + this.selectedW - positionSpacer - outerSpacing - barSpacer, this.selectedY + outerSpacing, whiteW, this.selectedH - outerSpacing*2 - spaceForText, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
//        game.shapeRenderer.rect(x + this.selectedW - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.selectedY + outerSpacing + innerSpacing, whiteW - innerSpacing*2, barHeight, backgroundColour, backgroundColour, backgroundColour, backgroundColour);
//        game.shapeRenderer.rect(this.selectedX + this.selectedW - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.selectedY + outerSpacing + innerSpacing, whiteW - innerSpacing*2, value/maxValue*barHeight, progressColour, progressColour, progressColour, progressColour);
    }
}
