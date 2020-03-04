package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.utils.*;
import com.mozarellabytes.kroy.PowerUp.PowerUp;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Descriptors.Desc;
import com.mozarellabytes.kroy.Entities.*;
import com.mozarellabytes.kroy.GUI.GUI;
import com.mozarellabytes.kroy.GameState;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.*;


import java.util.ArrayList;
import java.util.Random;

/**
 * The Screen that our game is played in.
 * Accessed from MenuScreen when the user
 * clicks the Start button, and exits when
 * the player wins or loses the game
 */
public class GameScreen implements Screen {

    /** Instance of our game that allows us the change screens */
    private Kroy game;

    /** Renders our tiled map */
    private OrthogonalTiledMapRenderer mapRenderer;

    /** Camera to set the projection for the screen */
    private OrthographicCamera camera;

    /** Renders shapes such as the health/reserve
     * stat bars above entities */
    private ShapeRenderer shapeMapRenderer;

    /** Stores the layers of our tiled map */
    private MapLayers mapLayers;

    /** Stores the structures layers, stores the background layer */
    private int[] structureLayersIndices;
    private int[] backgroundLayerIndex;

    /** Batch that has dimensions in line with the 40x25 map */
    private Batch mapBatch;

    /** Used for shaking the camera when a bomb hits a truck */
    private CameraShake camShake;

    /** Stores whether the game is running or is paused */
    private PlayState state;

    /**
     * Deals with all the user interface on the screen
     * that does not want to be inline with the map
     * coordinates, e.g. big stat bars, buttons, pause
     * screen
     */
    private GUI gui;

    /**
     * Stores the progress through the game. It keeps
     * track of trucks/fortresses and will end the game
     * once an end game condition has been met
     */
    public GameState gameState;

    /** List of Fortresses currently active on the map */
    private ArrayList<Fortress> fortresses;

    /**
     * List of patrols current active around the map
     */
    private ArrayList<Patrol> patrols;

    /** List of VFX */
    private ArrayList<VFX> vfx;

    /** Where the FireEngines' spawn, refill and repair */
    private FireStation station;

    /** The FireTruck that the user is currently drawing a path for */
    public FireTruck selectedTruck;

    /** The entity that the user has clicked on to show
     * the large stats in the top left corner */
    private Object selectedEntity;

    /** A class keeping track of the current difficulty and the time to the next change */
    private DifficultyControl difficultyControl;

    /** An arraylist of all the entities that have been destroyed */
    private ArrayList<DestroyedEntity> deadEntities;

    public FPSLogger fpsCounter;

    /**
     * Cooldown for the freeze feature to prevent it being too overpowered
     */
    private float freezeCooldown;

    private boolean truckAttack;

    private Timer powerupTimer;
    private ArrayList<PowerUp> powerUps;

    private FileHandle file;

    /** Play when the game is being played
     * Pause when the pause button is clicked */
    public enum PlayState {
        PLAY, PAUSE, FREEZE
    }

    public GameScreen(Kroy game, SavedElement save) {
        setup(game);

        // fire station (including fire trucks)
        station = save.getFireStation();
        station.setGameScreen(this);

        // game state
        gameState = save.getGameState();

        // fortresses
        fortresses = save.getFortresses();

        // difficulty control
        difficultyControl = save.getDifficultyControl();

        // patrols
        patrols = save.getPatrols();
    }

    /**
     * Constructor which has the game passed in
     *
     * @param game LibGdx game
     */
    public GameScreen(Kroy game) {
        setup(game);

        // Entity related stuff
        station = new FireStation(2, 7, 100);
        station.setGameScreen(this);

        spawn(FireTruckType.Emerald);
        spawn(FireTruckType.Amethyst);
        spawn(FireTruckType.Sapphire);
        spawn(FireTruckType.Ruby);

        fortresses.add(new Fortress(12, 23.5f, FortressType.Revs));
        fortresses.add(new Fortress(30.5f, 22.5f, FortressType.Walmgate));
        fortresses.add(new Fortress(16.5f, 3.5f, FortressType.Railway));
        fortresses.add(new Fortress(32f, 1.5f, FortressType.Clifford));
        fortresses.add(new Fortress(41.95f, 23.5f, FortressType.Museum));
        fortresses.add(new Fortress(44f, 11f, FortressType.CentralHall));

        patrols.add(new Patrol(PatrolType.Blue));
        patrols.add(new Patrol(PatrolType.Green));
        patrols.add(new Patrol(PatrolType.Peach));
        patrols.add(new Patrol(PatrolType.Violet));
        patrols.add(new Patrol(PatrolType.Yellow));

        // sets the origin point to which all of the polygon's local vertices are relative to.
        for (FireTruck truck : station.getTrucks()) {
            truck.setOrigin(Constants.TILE_WxH / 2, Constants.TILE_WxH / 2);
        }

        difficultyControl = new DifficultyControl();

    }

    private void setup(Kroy game) {
        this.game = game;
        fpsCounter = new FPSLogger();

        file = Gdx.files.local("saves/save.json");

        state = PlayState.PLAY;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

        TiledMap map = new TmxMapLoader().load("maps/YorkMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.TILE_WxH);
        mapRenderer.setView(camera);

        shapeMapRenderer = new ShapeRenderer();
        shapeMapRenderer.setProjectionMatrix(camera.combined);

        gui = new GUI(game, this);

        Gdx.input.setInputProcessor(new GameInputHandler(this, gui));

        gameState = new GameState();
        camShake = new CameraShake();

        //Orders renderer to start rendering the background, then the player layer, then structures
        mapLayers = map.getLayers();
        backgroundLayerIndex = new int[]{mapLayers.getIndex("background")};

        structureLayersIndices = new int[]{mapLayers.getIndex("structures"),
                mapLayers.getIndex("structures2"),
                mapLayers.getIndex("structures3"),
                mapLayers.getIndex("transparentStructures")};

        mapBatch = mapRenderer.getBatch();

        vfx = new ArrayList<VFX>();

        freezeCooldown = 0f;
        truckAttack = false;
        gui.updateAttackMode(false);

        if (SoundFX.music_enabled) {
            SoundFX.sfx_soundtrack.setVolume(.5f);
            SoundFX.sfx_soundtrack.play();
        }

        powerUps = new ArrayList<PowerUp>();

        powerupTimer = new Timer();
        powerupTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                generatePowerUp();
            }
        }, 1,1);


        // for (com.mozarellabytes.kroy.PowerUp power : powerUps) power.update();

        // arrays to hold entities
        fortresses = new ArrayList<Fortress>();
        patrols = new ArrayList<Patrol>();
        deadEntities = new ArrayList<>(7);

    }

    @Override
    public void show() {
        gui.resetButtons();
    }

    @Override
    public void render(float delta) {
//        fpsCounter.log();

        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render(backgroundLayerIndex);

        mapBatch.begin();


        for (FireTruck truck : station.getTrucks()) {
            truck.drawPath(mapBatch);
            truck.drawSprite(mapBatch);
            //truck.updateBubble(mapBatch);
        }

        if(!gameState.hasStationDestoyed()) {
            station.draw(mapBatch);
        }

        if (!powerUps.isEmpty()) {
            for (PowerUp power : powerUps) {
                if (power.getCanBeRendered()) {
                    power.render(mapBatch);
                }
            }
        }

        for (Fortress fortress : this.fortresses) {
            fortress.draw(mapBatch);
        }

        for (DestroyedEntity deadFortress : deadEntities){
            deadFortress.draw(mapBatch);
        }

        mapBatch.end();

        mapRenderer.render(structureLayersIndices);

        mapBatch.begin();
        for (Patrol patrol : this.patrols) {
            patrol.drawSprite(mapBatch);
        }

        for (VFX vfx : this.vfx) {
            vfx.update(mapBatch);
        }
        mapBatch.end();

        shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (PowerUp power : powerUps){
            power.drawStats(shapeMapRenderer);
        }

        for (FireTruck truck : station.getTrucks()) {
            truck.drawStats(shapeMapRenderer);
        }

        for (Patrol patrol : this.patrols) {
            patrol.drawStats(shapeMapRenderer);
        }

        if (station.getHP() > 0) {
            station.drawStats(shapeMapRenderer);
        }

        for (Fortress fortress : fortresses) {
            fortress.drawStats(shapeMapRenderer);
            for (Bomb bomb : fortress.getBombs()) {
                bomb.drawBomb(shapeMapRenderer);
            }
        }

        shapeMapRenderer.end();

        gui.renderElements();


        switch (state) {
            case PLAY:
                this.update(delta);
                break;
            case PAUSE:
                // render dark background
                SoundFX.stopTruckAttack();

                Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
                shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeMapRenderer.setColor(0, 0, 0, 0.5f);
                shapeMapRenderer.rect(0, 0, camera.viewportWidth, camera.viewportHeight);
                shapeMapRenderer.end();

                gui.renderPauseScreenText();
                break;
            case FREEZE:
                // render dark background
                SoundFX.stopTruckAttack();

                Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
                shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeMapRenderer.setColor(0, 0, 1f, 0.2f);
                shapeMapRenderer.rect(0, 0, camera.viewportWidth, camera.viewportHeight);
                shapeMapRenderer.setColor(0, 0, 0, 0.5f);
                shapeMapRenderer.rect(camera.viewportWidth/4, 26.5f, camera.viewportWidth/2, 3);
                shapeMapRenderer.end();

                gui.renderFreezeScreenText(freezeCooldown);

                // exit this mode
                if (freezeCooldown < 0) {
                    freezeCooldown = Constants.FREEZE_TIME;
                    state = PlayState.PLAY;
                }
        }
        gui.renderButtons();
    }

    /**
     * Manages all of the updates/checks during the game
     *
     * @param delta The time in seconds since the last render
     */
    private void update(float delta) {
        gameState.hasGameEnded(game);
        gameState.firstFortressDestroyed();
        camShake.update(delta, camera, new Vector2(camera.viewportWidth / 2f, camera.viewportHeight / 2f));

        freezeCooldown -= delta;

        station.restoreTrucks();
        station.checkForCollisions();

        gameState.setTrucksInAttackRange(0);

        ArrayList<PowerUp> powerUpsToRemove = new ArrayList<PowerUp>();

        for (PowerUp power : powerUps) {
            power.update();
            if (power.getCanBeDestroyed()) powerUpsToRemove.add(power);
        }

        powerUps.removeAll(powerUpsToRemove);

        for (int i = 0; i < station.getTrucks().size(); i++) {
            FireTruck truck = station.getTruck(i);

            truck.move();
            truck.updateSpray();

            for (PowerUp power : powerUps){
                if (power.getPosition().equals(truck.getPosition()))
                    power.invokePower(truck);
            }

            // manages attacks between trucks and fortresses
            for (Fortress fortress : this.fortresses) {
                if (fortress.withinRange(truck.getVisualPosition()) & !truck.inShield()) {
                    fortress.attack(truck, true, difficultyControl.getDifficultyMultiplier());
                }
                if (truck.fortressInRange(fortress.getPosition())) {
                    gameState.incrementTrucksInAttackRange();
                    if (isTruckAttackEnabled()) truck.attack(fortress);
                    break;
                }
            }

            for (Patrol patrol : this.patrols) {
                Vector2 patrolPos = new Vector2(Math.round(patrol.position.x), Math.round(patrol.position.y));
                if (patrolPos.equals(truck.getTilePosition())) {
                    doDanceOff(truck, patrol);
                }
            }

            // check if truck is destroyed
            if (truck.getHP() <= 0) {
                gameState.removeFireTruck();
                station.destroyTruck(truck);
                if (truck.equals(this.selectedTruck)) {
                    this.selectedTruck = null;
                }
            }
        }

        if (station.getHP() <= 0) {
            if(!(gameState.hasStationDestoyed())){
                gameState.setStationDestoyed();
                deadEntities.add(station.getDestroyedStation());
            }
            patrols.remove(PatrolType.Boss);
        }

        for (int i=0;i<this.patrols.size();i++) {
            Patrol patrol = this.patrols.get(i);
            patrol.updateBossSpray();
            if (patrol.getType().equals(PatrolType.Boss)) {
                if ((patrol.getDoublePosition().equals(PatrolType.Boss.getPoints().get(2)))){
                    patrol.attack(station);
                } else{
                    patrol.move(0.01);
                }
                if(gameState.hasStationDestoyed()){
                    patrols.remove(patrol);
                }
            } else {
                patrol.move(0.05);
            }
            if (patrol.getHP() <= 0) {
                patrols.remove(patrol);
                if((patrol.getType().equals(PatrolType.Boss))&&(!gameState.hasStationDestoyed())){
                    patrols.add(new Patrol(PatrolType.Boss));
                }
            }
        }

        for (int i = 0; i < this.fortresses.size(); i++) {
            Fortress fortress = this.fortresses.get(i);

            boolean hitTruck = fortress.updateBombs();
            if (hitTruck) {
                camShake.shakeIt(.2f);
            }

            // check if fortress is destroyed
            if (fortress.getHP() <= 0) {
                gameState.addFortress();
                if (gameState.firstFortressDestroyed())
                    patrols.add(new Patrol(PatrolType.Boss));
                deadEntities.add(fortress.createDestroyedFortress());
                float x = fortress.getPosition().x;
                float y = fortress.getPosition().y;
                this.vfx.add(new VFX(0, new Vector2(x-1f, y-3f)));
                this.vfx.add(new VFX(0, new Vector2(x-3.5f, y-3)));
                this.vfx.add(new VFX(0, new Vector2(x-3f, y-2f)));
                this.vfx.add(new VFX(0, new Vector2(x-2f, y-2.5f)));
                this.fortresses.remove(fortress);
                if (SoundFX.music_enabled) SoundFX.sfx_fortress_destroyed.play();
            }

        }

        if (gameState.getTrucksInAttackRange() > 0 && SoundFX.music_enabled){
            SoundFX.playTruckAttack();
        } else {
            SoundFX.stopTruckAttack();
        }

        shapeMapRenderer.end();
        shapeMapRenderer.setColor(Color.WHITE);

        difficultyControl.incrementCurrentTime(delta);
        gui.updateDifficultyTime(difficultyControl.getTimeSinceLastDifficultyIncrease());
        gui.updateDifficultyMultiplier(difficultyControl.getDifficultyMultiplier());
        gui.updateFreezeCooldown(freezeCooldown);
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
        mapRenderer.dispose();
        shapeMapRenderer.dispose();
        mapBatch.dispose();
        SoundFX.sfx_soundtrack.stop();
    }

    /**
     * Checks whether the player has clicked on a truck and sets that
     * truck to selected truck and entity
     *
     * @param position  coordinates of where the user clicked
     * @return          <code>true</code> if player clicks on a truck
     *                  <code>false</code> otherwise
     */
    public boolean checkClick(Vector2 position) {
        for (int i = this.station.getTrucks().size() - 1; i >= 0; i--) {
            FireTruck selectedTruck = this.station.getTruck(i);
            Vector2 truckTile = getTile(selectedTruck.getPosition());
            if (position.equals(truckTile) &&!selectedTruck.getMoving()) {
                this.selectedTruck = this.station.getTruck(i);
                this.selectedEntity = this.station.getTruck(i);
                return true;
            }
        }
        return false;
    }

    public boolean isNotPaused() {
        return state != PlayState.PAUSE;
    }

    /**
     * Gets the coordinates of the tile that the truck is closest to
     *
     * @param position  coordinates of truck
     * @return          coordinates of closest tile
     */
    private Vector2 getTile(Vector2 position) {
        return new Vector2((float) Math.round((position.x)), (float) Math.round(position.y));
    }

    /**
     * Checks whether the user has clicked on a the last tile in a
     * truck's trail path and selects the truck as active truck and
     * entity
     *
     * @param position  the coordinates where the user clicked
     * @return          <code>true</code> if player clicks on the
     *                  last tile in a truck's path
     *                  <code>false</code> otherwise
     */
    public boolean checkTrailClick(Vector2 position) {
        for (int i=this.station.getTrucks().size()-1; i>=0; i--) {
            if (!station.getTruck(i).pathSegments.isEmpty() && !station.getTruck(i).pathSegments.last().isEmpty()) {
                if (position.equals(this.station.getTruck(i).pathSegments.last().last())) {
                    this.selectedTruck = this.station.getTruck(i);
                    this.selectedEntity = this.station.getTruck(i);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks whether the tile that the user is trying to add to the
     *  truck's path is on the road. This uses the custom "road"
     * boolean property in the collisions layer within the tiled map
     *
     * @param x x coordinate of tile
     * @param y y coordinate of tile
     * @return  <code>true</code> if the tile is a road
     *          <code>false</code> otherwise
     */
    public boolean isRoad(int x, int y) {
        return ((TiledMapTileLayer) mapLayers.get("collisions")).getCell(x, y).getTile().getProperties().get("road").equals(true);
    }

    /**
     * Changes from GameScreen to Control screen, passing "game" so that when
     * the player exits the control screen, it knows to return to the Game
     */
    public void toControlScreen() {
        game.setScreen(new ControlsScreen(game, this, "game"));
    }

    /** Exits the main game screen and goes to the menu, called when the home
     * button is clicked */
    public void toHomeScreen() {
        game.setScreen(new MenuScreen(game));
        SoundFX.sfx_soundtrack.dispose();
    }

    /**
     * Starts a dance-off between the given firetruck and the given ET
     * @param firetruck
     * @param et
     */
    public void doDanceOff(FireTruck firetruck, Patrol et) {
        SoundFX.stopMusic();
        game.setScreen(new DanceScreen(game, this, firetruck, et));
    }

    /**
     * Creates a new FireEngine, plays a sound and adds it gameState to track
     * @param type Type of truck to be spawned (Ocean, Speed)
     */
    private void spawn(FireTruckType type) {
        if (SoundFX.music_enabled) SoundFX.sfx_truck_spawn.play();
        station.spawn(new FireTruck(this, new Vector2(3 + station.getTrucks().size(),7), type));
        gameState.addFireTruck();
    }

    /**
     * Toggles between PLAY, PAUSE and FREEZE state when the Pause button is clicked
     * or when the SPACE bar is clicked
     */
    public void changeState(PlayState action) {
        switch (action) {
            case PAUSE:
                if (state.equals(PlayState.PLAY)) state = PlayState.PAUSE;
                else state = PlayState.PLAY;
                break;
            case FREEZE:
                if (state.equals(PlayState.FREEZE)) {
                    state = PlayState.PLAY;
                    station.recalculateTruckPaths();
                    freezeCooldown = 10;
                } else if (freezeCooldown < 0) {
                    state = PlayState.FREEZE;
                    freezeCooldown = 10;
                }
                break;
        }
    }

    /**
     * Enables/Disables auto attack
     */
    public void toggleTruckAttack() {
        truckAttack = !truckAttack;
        if (truckAttack && SoundFX.music_enabled && this.gameState.getTrucksInAttackRange() > 0) SoundFX.playTruckAttack();
        gui.updateAttackMode(truckAttack);
    }

    private void generatePowerUp() {
        if (powerUps.size() <= Constants.NUMBER_OF_POWERUPS){
            ArrayList<PowerUp> possiblePowerUp = PowerUp.createNewPowers();
            Random rand = new Random();
            int index = rand.nextInt(possiblePowerUp.size());
            PowerUp powerup = possiblePowerUp.get(index);
            if (!checkIfPowerupInLocation(powerup)) {
                powerup.update();
                powerUps.add(powerup);
            }
        }
    }

    private boolean checkIfPowerupInLocation(PowerUp powerUp){
        for (PowerUp power : powerUps){
            if (power.getPosition().equals(powerUp.getPosition())) return true;
        }
        return false;
    }

    /** The method for giving trucks that have the same end tiles adjacent end tiles
     * so that they do not end up on the same tile
     */
    public void shortenActiveSegment() {
        selectedTruck.pathSegment.removeLast();
    }

    public boolean isTruckAttackEnabled() {
        return this.truckAttack;
    }

    public FireStation getStation() {
        return this.station;
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public ArrayList<Fortress> getFortresses() {
        return this.fortresses;
    }

    public PlayState getState() {
        return this.state;
    }

    public void setSelectedEntity(Object entity) {
        this.selectedEntity = entity;
    }

    public Object getSelectedEntity() {
        return this.selectedEntity;
    }

    /**
     * Save the game to a JSON file which can then be resumed
     */
    public void saveGameState() {
        Json json = new Json(JsonWriter.OutputType.json);

        OrderedMap<String, Object> entitiesMap = new OrderedMap<>();
        entitiesMap.put("FireStation", this.station.getDescriptor());
        entitiesMap.put("FireTrucks", station.getFireTrucksDescriptor());
        entitiesMap.put("Fortresses", this.getFortressesDescriptor());
        entitiesMap.put("Patrols", this.getPatrolsDescriptor());

        OrderedMap<String, Object> map = new OrderedMap<>();
        map.put("Entities", entitiesMap);
        map.put("Difficulty", difficultyControl);
        map.put("GameState", gameState);
        file.writeString(json.prettyPrint(map),false);
    }

    private Desc.Fortress[] getFortressesDescriptor() {
        Desc.Fortress[] fortresses = new Desc.Fortress[this.fortresses.size()];
        for (int i=0; i<fortresses.length; i++) {
            fortresses[i] = this.fortresses.get(i).getSave();
        }
        return fortresses;
    }

    private Desc.Patrol[] getPatrolsDescriptor() {
        Desc.Patrol[] patrols = new Desc.Patrol[this.patrols.size()];
        for (int i=0; i<patrols.length; i++) {
            patrols[i] = this.patrols.get(i).getSave();
        }
        return patrols;
    }

}