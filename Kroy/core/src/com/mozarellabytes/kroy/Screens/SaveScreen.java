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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mozarellabytes.kroy.GUI.ClickableGroup;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.SoundFX;

/**
 * Screen to find all information about a Save and select
 * it from a list of saves to carry on where you left off
 */
public class SaveScreen implements Screen {

    // objects from other screen
    private final Kroy game;

    private final MenuScreen menuScreen;

    private final Stage stage;
    private Table selectedTable;

    private SavedElement currentSaveSelected;

    private ImageButton.ImageButtonStyle closeButtonStyle;
    private ImageButton.ImageButtonStyle deleteButtonStyle;
    private ImageButton.ImageButtonStyle playButtonStyle;

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
        Gdx.input.setInputProcessor(stage);
    }

    private void deleteSave() {
        Gdx.files.local("saves/" + currentSaveSelected.getTimestamp() + "/").deleteDirectory();
        stage.clear();
        show();
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"), new TextureAtlas("skin/uiskin.atlas"));

        // create widget groups
        Table table = new Table(); // stores everything in
        selectedTable = new Table();

        VerticalGroup savesList = new VerticalGroup();
        ScrollPane savesScroll = new ScrollPane(savesList, skin); // shows the game saves
        savesScroll.setScrollbarsVisible(true);
        savesScroll.setScrollingDisabled(true, false);
        HorizontalGroup header = new HorizontalGroup();
        HorizontalGroup footer = new HorizontalGroup();

        // create actors
        Drawable currentDeleteImage = new TextureRegionDrawable(new Texture("ui/delete_idle.png"));
        Drawable currentCloseImage = new TextureRegionDrawable(new Texture("ui/return_idle.png"));
        Drawable currentPlayImage = new TextureRegionDrawable(new Texture("ui/start_idle.png"));
        currentDeleteImage.setMinWidth(167.5f);
        currentDeleteImage.setMinHeight(57.5f);
        currentCloseImage.setMinWidth(167.5f);
        currentCloseImage.setMinHeight(57.5f);
        currentPlayImage.setMinWidth(167.5f);
        currentPlayImage.setMinHeight(57.5f);

        createButtonStyles();

        ImageButton deleteButton = new ImageButton(deleteButtonStyle);
        ImageButton closeButton = new ImageButton(closeButtonStyle);
        ImageButton playButton = new ImageButton(playButtonStyle);

        // actors
        Label titleLabel = new Label("Game Saves", new Label.LabelStyle(game.font60, Color.WHITE));
        titleLabel.setAlignment(Align.left);

        for (FileHandle file : Gdx.files.internal("saves/").list()) {
            SavedElement save = new SavedElement(file.nameWithoutExtension());
            Image screenshot = new Image(new Texture("saves/" + save.getTimestamp() + "/screenshot.png"));
            ClickableGroup saveItemTable = new ClickableGroup();

            VerticalGroup list = new VerticalGroup();
            Label timestampLabel = new Label(save.getEnTimestamp(), new Label.LabelStyle(game.font33, Color.WHITE));
            Label fireTrucksAliveLabel = new Label(" - Fire Trucks alive: " + save.getFireTrucks().size(), new Label.LabelStyle(game.font25, Color.WHITE));
            Label fortressesRemainingLabel = new Label(" - Fortresses remaining: " + save.getFortresses().size(), new Label.LabelStyle(game.font25, Color.WHITE));
            Label fireStationAlive = new Label(" - Fire Station: " + (save.getFireStation().isAlive() ? "alive" : "destroyed"), new Label.LabelStyle(game.font25, Color.WHITE));

            timestampLabel.setAlignment(Align.left);
            fireTrucksAliveLabel.setAlignment(Align.left);
            fortressesRemainingLabel.setAlignment(Align.left);
            fireStationAlive.setAlignment(Align.left);

            list.addActor(timestampLabel);
            list.addActor(fireTrucksAliveLabel);
            list.addActor(fortressesRemainingLabel);
            list.addActor(fireStationAlive);

            list.fill().space(5).expand().padRight(15).padLeft(15);

            saveItemTable.setTouchable(Touchable.enabled);
            saveItemTable.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    currentSaveSelected = save;
                    updateCurrentlySelected();
                }
            });
            saveItemTable.row().pad(5);
            saveItemTable.add(screenshot).maxWidth(Gdx.graphics.getWidth()/7f).maxHeight(Gdx.graphics.getHeight()/7f);
            saveItemTable.add(list);
            savesList.addActor(saveItemTable);
        }

        deleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentSaveSelected != null) {
                    if (SoundFX.music_enabled) SoundFX.sfx_button_clicked.play();
                    deleteSave();
                }
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (SoundFX.music_enabled) SoundFX.sfx_button_clicked.play();
                game.setScreen(menuScreen);
            }
        });

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentSaveSelected != null) {
                    if (SoundFX.music_enabled) SoundFX.sfx_button_clicked.play();
                    game.setScreen(new GameScreen(game, currentSaveSelected));
                }
            }
        });

        selectedTable.add(new Label("Click on a save file then click Start to load it", new Label.LabelStyle(game.font25, Color.WHITE))).fill().expand();

        header.addActor(titleLabel);
        footer.addActor(closeButton);

        header.expand();
        footer.expand();
        footer.space(20);

        // add header to table
        table.setFillParent(true);
        table.add(header).colspan(2).expandX().pad(40).left();
        table.row().expandY();

        // displays message if no saves are found
        if (Gdx.files.internal("saves/").list().length == 0) {
            table.add(new Label("No saves found... click the save icon in game to save a game", new Label.LabelStyle(game.font25, Color.WHITE)));
        } else {
            table.add(savesScroll).colspan(1).padLeft(40);
            table.add(selectedTable).colspan(1).padRight(40);
            footer.addActor(deleteButton);
            footer.addActor(playButton);
        }

        table.row();
        table.add(footer).colspan(2).expandX().pad(40).right();

        stage.addActor(table);
    }

    private void createButtonStyles() {
        closeButtonStyle = new ImageButton.ImageButtonStyle();
        closeButtonStyle.up = new TextureRegionDrawable(new Texture("ui/return_idle.png"));
        closeButtonStyle.down = new TextureRegionDrawable(new Texture("ui/return_clicked.png"));

        deleteButtonStyle = new ImageButton.ImageButtonStyle();
        deleteButtonStyle.up = new TextureRegionDrawable(new Texture("ui/delete_idle.png"));
        deleteButtonStyle.down = new TextureRegionDrawable(new Texture("ui/delete_clicked.png"));

        playButtonStyle = new ImageButton.ImageButtonStyle();
        playButtonStyle.up = new TextureRegionDrawable(new Texture("ui/start_idle.png"));
        playButtonStyle.down = new TextureRegionDrawable(new Texture("ui/start_clicked.png"));
    }

    private void updateCurrentlySelected() {
        Image screenshot = new Image(new Texture("saves/" + currentSaveSelected.getTimestamp() + "/screenshot.png"));
        selectedTable.clearChildren();
        selectedTable.add(new Label(currentSaveSelected.getEnTimestamp(), new Label.LabelStyle(game.font60, Color.WHITE))).padBottom(20).row();
        selectedTable.add(screenshot).maxWidth(Gdx.graphics.getWidth()/3f).maxHeight(Gdx.graphics.getHeight()/3f).padBottom(20).row();
        VerticalGroup savesList = new VerticalGroup();
        savesList.space(10);

        Label difficultyLabel = new Label("Difficulty: " +
                currentSaveSelected.getDifficultyControl().getDifficultyMultiplier() + "x", new Label.LabelStyle(game.font25, Color.WHITE));
        difficultyLabel.setAlignment(Align.left);

        Label fireTrucksLevel = new Label("Fire Trucks: (" + currentSaveSelected.getFireTrucks().size() + ")" +
                currentSaveSelected.listAliveFireTrucks(), new Label.LabelStyle(game.font25, Color.WHITE));
        fireTrucksLevel.setAlignment(Align.left);

        Label fortressesLevel = new Label("Fortresses: (" + currentSaveSelected.getFortresses().size() + ")" +
                currentSaveSelected.listAliveFortresses(), new Label.LabelStyle(game.font25, Color.WHITE));
        fortressesLevel.setAlignment(Align.left);

        savesList.fill().bottom().expand();
        savesList.addActor(difficultyLabel);
        savesList.addActor(fireTrucksLevel);
        savesList.addActor(fortressesLevel);
        selectedTable.add(savesList).expand().maxHeight(Gdx.graphics.getHeight()/3f);
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
