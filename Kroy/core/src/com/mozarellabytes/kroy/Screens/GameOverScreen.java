package com.mozarellabytes.kroy.Screens;

/**********************************************************************************
                                Edited for assessment 4
 **********************************************************************************/

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.mozarellabytes.kroy.InputHandlers.GameOverInputHandler;
import com.mozarellabytes.kroy.Kroy;

/** Edited for assessment 4 by adding new background
 * images for if the player won or lost
 *
 * This screen is shown after the game has ended.
 * It tells the player if they have won or lost.
 */
public class GameOverScreen implements Screen {

    /** The game - to be able to use the fonts from game */
    private final Kroy game;

    /** The texture that makes up the background screen depending on if the player
     * won or lost the game*/
    private final Texture chosenImage;

    /** Camera to set the projection for the screen */
    private final OrthographicCamera camera;

    /** Edited for assessment 4 - added a new background image
     *
     * Constructor for the game screen
     * @param game  LibGdx game
     * @param won <code> true </code> if the game was won
     *            <code> false </code> if th game was lost
     */
    public GameOverScreen(Kroy game, boolean won) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);

        Texture winImage = new Texture(Gdx.files.internal("images/WIN_SCREEN.png"), true);
        winImage.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        Texture loseImage = new Texture(Gdx.files.internal("images/LOSE_SCREEN.png"), true);
        loseImage.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);

        Gdx.input.setInputProcessor(new GameOverInputHandler(game));

        this.chosenImage = (won) ? winImage : loseImage;
    }

    @Override
    public void show() {

    }

    /** Renders the game over screen
     *
     *  @param delta The time in seconds since the last render. */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(51/255f, 34/255f, 99/255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(chosenImage, 0, 0, Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
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
        chosenImage.dispose();
    }


}
