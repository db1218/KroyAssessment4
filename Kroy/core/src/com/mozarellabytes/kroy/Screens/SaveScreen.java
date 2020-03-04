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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mozarellabytes.kroy.Kroy;

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
        Gdx.input.setInputProcessor(stage);

        // create widget groups
        Table table = new Table(); // stores everything in
        VerticalGroup savesList = new VerticalGroup();
        ScrollPane savesScroll = new ScrollPane(savesList, new Skin(Gdx.files.internal("skin/uiskin.json"), new TextureAtlas("skin/uiskin.atlas"))); // shows the game saves
        Table selectedTable = new Table(); // displays the selected save for more information
        HorizontalGroup header = new HorizontalGroup();
        HorizontalGroup footer = new HorizontalGroup();

        // create actors
        Drawable closeImage = new TextureRegionDrawable(new Texture("ui/start_idle.png"));
        Drawable playImage = new TextureRegionDrawable(new Texture("ui/controls_idle.png"));
        closeImage.setMinWidth(167.5f);
        closeImage.setMinHeight(57.5f);
        playImage.setMinWidth(167.5f);
        playImage.setMinHeight(57.5f);
        closeButton = new ImageButton(closeImage);
        playButton = new ImageButton(playImage);

        titleLabel = new Label("Game Saves", new Label.LabelStyle(game.font60, Color.WHITE));
        titleLabel.setAlignment(Align.left);

        for(FileHandle file : Gdx.files.internal("saves/").list()) {

            SavedElement save = new SavedElement(file.nameWithoutExtension());
            Image screenshot = new Image(new Texture("saves/" + save.getTimestamp() + "/screenshot.png"));
            screenshot.setSize(200, 100);
            Table saveItemTable = new Table();
            saveItemTable.row().padBottom(10).minHeight(150);
            saveItemTable.add(screenshot);

            VerticalGroup list = new VerticalGroup();
            Label timestampLabel = new Label(save.getTimestamp(), new Label.LabelStyle(game.font33, Color.WHITE));
            timestampLabel.setAlignment(Align.right);
            Label fireTrucksAliveLabel = new Label("Fire Trucks alive: " + save.getFireTrucks().size(), new Label.LabelStyle(game.font19, Color.WHITE));
            fireTrucksAliveLabel.setAlignment(Align.right);
            Label fortressesRemainingLabel = new Label("Fortresses remaining: " + save.getFortresses().size(), new Label.LabelStyle(game.font19, Color.WHITE));
            fortressesRemainingLabel.setAlignment(Align.right);
            Label fireStationAlive = new Label("Fire Station: " + (save.getFireStation().isAlive() ? "yes" : "no"), new Label.LabelStyle(game.font19, Color.WHITE));
            fireStationAlive.setAlignment(Align.right);

            list.addActor(timestampLabel);
            list.addActor(fireTrucksAliveLabel);
            list.addActor(fortressesRemainingLabel);
            list.addActor(fireStationAlive);

            list.fill().space(5).padRight(20).padLeft(20);

            saveItemTable.add(list);
            savesList.addActor(saveItemTable);
        }

        Label testLabel2 = new Label("Test 2", new Label.LabelStyle(game.font33, Color.WHITE));
        selectedTable.add(testLabel2);

        header.addActor(titleLabel);
        footer.addActor(closeButton);
        footer.addActor(playButton);

        header.expand();
        footer.expand();
        footer.space(20);

        // add header to table
        table.setFillParent(true);
        table.add(header).expandX().pad(40).left().colspan(4);
        table.row().expand();
        table.add(savesScroll).padLeft(40).colspan(1).expandY();
        table.add(selectedTable).padRight(40).colspan(3);
        table.row();
        table.add(footer).expandX().pad(40).right().colspan(4);


        stage.addActor(table);
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
        stage.dispose();
    }
}
