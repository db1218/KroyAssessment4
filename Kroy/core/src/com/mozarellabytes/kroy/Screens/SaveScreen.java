package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mozarellabytes.kroy.Kroy;

import java.util.ArrayList;

/**
 * Screen to find all information about a Save and select
 * it from a list of saves to carry on where you left off
 */
public class SaveScreen implements Screen {

    // objects from other screen
    private final Kroy game;
    private final MenuScreen menuScreen;

    // camera and visual objects
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;
    private final Stage stage;

    // actors
    private final Label titleLabel;
    private final ImageButton closeButton;
    private final ImageButton playButton;

    public SaveScreen(Kroy game, MenuScreen menuScreen) {
        this.game = game;
        this.menuScreen = menuScreen;

        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        viewport = new ScreenViewport(camera);
        viewport.apply();

        // create stage
        stage = new Stage(viewport, game.batch);
        stage.setDebugAll(true);

        // create widget groups
        Table mainTable = new Table(); // stores everything in
        VerticalGroup savesTable = new VerticalGroup(); // shows the game saves
        ScrollPane savesScroll = new ScrollPane(savesTable); // shows the game saves
        Table selectedTable = new Table(); // displays the selected save for more information
        HorizontalGroup header = new HorizontalGroup();
        HorizontalGroup footer = new HorizontalGroup();

        // create actors
        Drawable closeImage = new TextureRegionDrawable(new Texture(Gdx.files.internal("ui/start_idle.png")));
        Drawable playImage = new TextureRegionDrawable(new Texture(Gdx.files.internal("ui/controls_idle.png")));
        closeImage.setMinWidth(167.5f);
        closeImage.setMinHeight(57.5f);
        playImage.setMinWidth(167.5f);
        playImage.setMinHeight(57.5f);
        closeButton = new ImageButton(closeImage);
        playButton = new ImageButton(playImage);

        titleLabel = new Label("Game Saves", new Label.LabelStyle(game.font60, Color.WHITE));

        Label testLabel2 = new Label("Test 2", new Label.LabelStyle(game.font33, Color.WHITE));

        titleLabel.setAlignment(Align.left);

        for(FileHandle file : Gdx.files.internal("saves/").list()) {
            SavedElement save = new SavedElement(file.nameWithoutExtension());
            Table saveItemTable = new Table();
            saveItemTable.row().expand();
            saveItemTable.add(new ImageButton(closeImage)).colspan(1);
            saveItemTable.add(new Label(file.name(), new Label.LabelStyle(game.font33, Color.WHITE))).colspan(2);
            savesTable.addActor(saveItemTable);
        }

        savesTable.expand();

        selectedTable.add(testLabel2);

        header.addActor(titleLabel);
        footer.addActor(closeButton);
        footer.addActor(playButton);

        header.expand();
        footer.expand();
        footer.space(20);

        // add header to table
        mainTable.setFillParent(true);
        mainTable.add(header).expandX().pad(40).left().colspan(4);
        mainTable.row().expand();
        mainTable.add(savesTable).padLeft(40).colspan(1);
        mainTable.add(selectedTable).padRight(40).colspan(3);
        mainTable.row();
        mainTable.add(footer).expandX().pad(40).right().colspan(4);


        stage.addActor(mainTable);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {

    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(51/255f, 34/255f, 99/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {

    }
}
