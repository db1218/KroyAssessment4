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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mozarellabytes.kroy.GUI.ClickableGroup;
import com.mozarellabytes.kroy.Kroy;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Screen to find all information about a Save and select
 * it from a list of saves to carry on where you left off
 */
public class SaveScreen implements Screen {

    // objects from other screen
    private final Kroy game;
    private final MenuScreen menuScreen;

    private final Stage stage;
    private final Table selectedTable;

    private SavedElement currentSaveSelected;

    public SaveScreen(Kroy game, MenuScreen menuScreen) {
        this.game = game;
        this.menuScreen = menuScreen;

        // camera and visual objects
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Viewport viewport = new ScreenViewport(camera);
        viewport.apply();

        // create stage
        stage = new Stage(viewport, game.batch);
        stage.setDebugAll(false);
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"), new TextureAtlas("skin/uiskin.atlas"));

        // create widget groups
        Table table = new Table(); // stores everything in
        selectedTable = new Table();

        VerticalGroup savesList = new VerticalGroup();
        ScrollPane savesScroll = new ScrollPane(savesList, skin); // shows the game saves
        savesScroll.setScrollbarsVisible(true);
        HorizontalGroup header = new HorizontalGroup();
        HorizontalGroup footer = new HorizontalGroup();

        // create actors
        Drawable closeImage = new TextureRegionDrawable(new Texture("ui/controls_idle.png"));
        Drawable playImage = new TextureRegionDrawable(new Texture("ui/start_idle.png"));
        closeImage.setMinWidth(167.5f);
        closeImage.setMinHeight(57.5f);
        playImage.setMinWidth(167.5f);
        playImage.setMinHeight(57.5f);
        ImageButton closeButton = new ImageButton(closeImage);
        ImageButton playButton = new ImageButton(playImage);

        // actors
        Label titleLabel = new Label("Game Saves", new Label.LabelStyle(game.font60, Color.WHITE));
        titleLabel.setAlignment(Align.left);

        for (FileHandle file : Gdx.files.internal("saves/").list()) {
            SavedElement save = new SavedElement(file.nameWithoutExtension());
            Image screenshot = new Image(new Texture("saves/" + save.getTimestamp() + "/screenshot.png"));
            ClickableGroup saveItemTable = new ClickableGroup();

            VerticalGroup list = new VerticalGroup();
            Label timestampLabel = new Label(save.getEnTimestamp(), new Label.LabelStyle(game.font33, Color.WHITE));
            timestampLabel.setAlignment(Align.left);
            Label fireTrucksAliveLabel = new Label(" - Fire Trucks alive: " + save.getFireTrucks().size(), new Label.LabelStyle(game.font25, Color.WHITE));
            fireTrucksAliveLabel.setAlignment(Align.left);
            Label fortressesRemainingLabel = new Label(" - Fortresses remaining: " + save.getFortresses().size(), new Label.LabelStyle(game.font25, Color.WHITE));
            fortressesRemainingLabel.setAlignment(Align.left);
            Label fireStationAlive = new Label(" - Fire Station: " + (save.getFireStation().isAlive() ? "yes" : "no"), new Label.LabelStyle(game.font25, Color.WHITE));
            fireStationAlive.setAlignment(Align.left);

            list.addActor(timestampLabel);
            list.addActor(fireTrucksAliveLabel);
            list.addActor(fortressesRemainingLabel);
            list.addActor(fireStationAlive);

            list.fill().space(5).pad(0, 50, 0, 50);

            saveItemTable.setTouchable(Touchable.enabled);
            saveItemTable.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    currentSaveSelected = save;
                    updateCurrentlySelected();
                }
            });
            saveItemTable.row().pad(5);
            saveItemTable.add(screenshot).size(200, 100);
            saveItemTable.add(list);
            savesList.addActor(saveItemTable);
        }

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(menuScreen);
            }
        });

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentSaveSelected != null)
                    game.setScreen(new GameScreen(game, currentSaveSelected));
            }
        });

        header.addActor(titleLabel);
        footer.addActor(closeButton);
        footer.addActor(playButton);

        header.expand();
        footer.expand();
        footer.space(20);

        // add header to table
        table.setFillParent(true);
        table.add(header).colspan(3).expandX().pad(40).left();
        table.row().expandY();
        table.add(savesScroll).colspan(1).padLeft(40);
        table.add(selectedTable).colspan(2).padRight(40);
        table.row();
        table.add(footer).colspan(3).expandX().pad(40).right();

        stage.addActor(table);
    }

    private void updateCurrentlySelected() {
        Image screenshot = new Image(new Texture("saves/" + currentSaveSelected.getTimestamp() + "/screenshot.png"));
        selectedTable.clearChildren();
        selectedTable.add(new Label(currentSaveSelected.getEnTimestamp(), new Label.LabelStyle(game.font60, Color.WHITE))).padBottom(20).row();
        selectedTable.add(screenshot).maxWidth(Gdx.graphics.getWidth()/3f).maxHeight(Gdx.graphics.getHeight()/3f).padBottom(20).row();
        VerticalGroup savesList = new VerticalGroup();
        savesList.space(10);
        savesList.align(Align.left);
        savesList.addActor(new Label("Difficulty Multiplier: " +
                currentSaveSelected.getDifficultyControl().getDifficultyMultiplier(), new Label.LabelStyle(game.font25, Color.WHITE)));
        savesList.addActor(new Label("Fire Trucks: (" + currentSaveSelected.getFireTrucks().size() + ")" +
                currentSaveSelected.listAliveFireTrucks(), new Label.LabelStyle(game.font25, Color.WHITE)));
        savesList.addActor(new Label("Fortresses: (" + currentSaveSelected.getFortresses().size() + ")" +
                currentSaveSelected.listAliveFortresses(), new Label.LabelStyle(game.font25, Color.WHITE)));
        selectedTable.add(savesList);
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

    public void startGame(SavedElement save) {
        game.setScreen(new GameScreen(game, save));
    }
}
