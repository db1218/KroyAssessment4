package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.utils.*;
import com.mozarellabytes.kroy.GUI.ButtonBar;
import com.mozarellabytes.kroy.InputHandlers.GameInputHandler;
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
import com.mozarellabytes.kroy.Entities.*;
import com.mozarellabytes.kroy.GUI.GUI;
import com.mozarellabytes.kroy.GameState;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * The Screen that our game is played in.
 * Accessed from MenuScreen when the user
 * clicks the Start button, and exits when
 * the player wins or loses the game
 */
public class GameScreen implements Screen, ButtonBar {

    /** Instance of our game that allows us the change screens */
    private Kroy game;
    private GameInputHandler inputHandler;

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
    private BossPatrol bossPatrol;

    /** List of VFX */
    private ArrayList<VFX> vfx;

    /** Where the FireEngines' spawn, refill and repair */
    private final FireStation station;

    /** The FireTruck that the user is currently drawing a path for */
    private FireTruck selectedTruck;

    /** The entity that the user has clicked on to show
     * the large stats in the top left corner */
    private Object selectedEntity;

    /** A class keeping track of the current difficulty and the time to the next change */
    private final DifficultyControl difficultyControl;

    private DifficultyLevel difficultyLevel;

    /** An arraylist of all the entities that have been destroyed */
    private ArrayList<DestroyedEntity> deadEntities;

    public FPSLogger fpsCounter;

    /**
     * Cooldown for the freeze feature to prevent it being too overpowered
     */
    private float freezeCooldown;

    private boolean truckAttack;
    private Warning warn;

    private ArrayList<PowerUp> powerUps;
    private ArrayList<Vector2> powerUpLocations;

    private DifficultyLevel level;

    private OrderedMap<String, Object> saveMap;

    /** Play when the game is being played
     * Pause when the pause button is clicked */
    public enum PlayState {
        PLAY, PAUSE, FREEZE
    }

    /**
     * Constructor which generates the game from
     * a save file
     * @param game  LibGDX game
     * @param save  save file to generate game from
     */
    public GameScreen(Kroy game, SavedElement save) {
        // fire station (including fire trucks)
        station = save.getFireStation();
        station.setGameScreen(this);

        setup(game, save.getDifficultyLevel());

        // game state
        gameState = save.getGameState();

        // fortresses
        fortresses = save.getFortresses();

        // difficulty control
        difficultyControl = save.getDifficultyControl();

        // patrols
        patrols = save.getPatrols();
        bossPatrol = save.getBossPatrol();
    }

    /**
     * Constructor which has the game passed in
     *
     * @param game  LibGdx game
     * @param level difficulty of the game
     */
    public GameScreen(Kroy game, DifficultyLevel level) {
        // Entity related stuff
        station = new FireStation(level.getStationLocation().x, level.getStationLocation().y, 100);
        station.setGameScreen(this);

        setup(game, level);

        spawn(level.getRuby());
        spawn(level.getSapphire());
        spawn(level.getAmethyst());
        spawn(level.getEmerald());

        fortresses.add(new Fortress(level.getRevsLocation().x, level.getRevsLocation().y, FortressType.Revs));
        fortresses.add(new Fortress(level.getWalmgateLocation().x, level.getWalmgateLocation().y, FortressType.Walmgate));
        fortresses.add(new Fortress(level.getRailwayLocation().x, level.getRailwayLocation().y, FortressType.Railway));
        fortresses.add(new Fortress(level.getCliffordLocation().x, level.getCliffordLocation().y, FortressType.Clifford));
        fortresses.add(new Fortress(level.getMuseumLocation().x, level.getMuseumLocation().y, FortressType.Museum));
        fortresses.add(new Fortress(level.getCentralHallLocation().x, level.getCentralHallLocation().y, FortressType.CentralHall));

        generatePatrols();

        difficultyControl = new DifficultyControl(level);

    }

    /**
     * Initialise common objects independent on if a new Game
     * is being made, or loaded from a save state
     *
     * @param game  LibGDX game
     * @param level difficulty of game
     */
    private void setup(Kroy game, DifficultyLevel level) {
        this.game = game;
        this.level = level;
        fpsCounter = new FPSLogger();

        state = PlayState.PLAY;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, level.getMapWidth(), level.getMapHeight());

        difficultyLevel = level;

        TiledMap map = level.getMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.TILE_WxH);
        mapRenderer.setView(camera);

        shapeMapRenderer = new ShapeRenderer();
        shapeMapRenderer.setProjectionMatrix(camera.combined);

        gui = new GUI(game, this);

        inputHandler = new GameInputHandler(this, gui);

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

        vfx = new ArrayList<>();

        freezeCooldown = 0f;
        truckAttack = true;
        gui.updateAttackMode(true);

        powerUps = new ArrayList<>();

        Timer powerupTimer = new Timer();
        powerupTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                canCreatePowerUp();
            }
        }, level.getTimeTillPowerup(),level.getTimeTillPowerup());

        powerUpLocations = new ArrayList<>();
        generatePowerUpLocations(level);

        // arrays to hold entities
        fortresses = new ArrayList<>();
        patrols = new ArrayList<>();
        deadEntities = new ArrayList<>(7);
    }

    @Override
    public void show() {
        SoundFX.decideMusic(this);
        Gdx.input.setInputProcessor(inputHandler);
        gui.getButtons().resetButtons();
    }

    @Override
    public void render(float delta) {
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render(backgroundLayerIndex);

        mapBatch.begin();


        for (FireTruck truck : station.getTrucks()) {
            truck.drawPath(mapBatch);
            truck.drawSprite(mapBatch);
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

        for (Patrol patrol : this.patrols) patrol.drawSprite(mapBatch);

        if (bossPatrol != null) bossPatrol.drawSprite(mapBatch);

        for (VFX vfx : this.vfx) vfx.render(mapBatch);

        mapBatch.end();

        shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (PowerUp power : powerUps) power.drawStats(shapeMapRenderer);

        for (FireTruck truck : station.getTrucks()) truck.drawStats(shapeMapRenderer);

        for (Patrol patrol : this.patrols) patrol.drawStats(shapeMapRenderer);

        if (bossPatrol != null) bossPatrol.drawSpray(shapeMapRenderer);

        if (station.isAlive()) station.drawStats(shapeMapRenderer);

        for (Fortress fortress : fortresses) {
            fortress.drawStats(shapeMapRenderer);
            for (Bomb bomb : fortress.getBombs()) bomb.drawBomb(shapeMapRenderer);
        }

        shapeMapRenderer.end();

        gui.renderElements();

        switch (state) {
            case PLAY:
                this.update(delta);
                break;
            case PAUSE:
                if (!gameState.getHitPatrol()) {
                    // render dark background
                    SoundFX.stopTruckAttack();
                    Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
                    shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeMapRenderer.setColor(0, 0, 0, 0.5f);
                    shapeMapRenderer.rect(0, 0, camera.viewportWidth, camera.viewportHeight);
                    shapeMapRenderer.end();

                    gui.renderPauseScreenText();
                } else {
                    mapBatch.begin();
                    this.warn.render(mapBatch);
                    mapBatch.end();
                }
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
                freezeCooldown -= delta;

                // exit this mode
                if (freezeCooldown < 0) {
                    changeState(PlayState.FREEZE);
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

        camShake.update(delta, camera, new Vector2(camera.viewportWidth / 2f, camera.viewportHeight / 2f));

        freezeCooldown -= delta;

        station.restoreTrucks();
        station.checkForCollisions();

        gameState.setTrucksInAttackRange(0);

        for (VFX vfx : this.vfx) vfx.update(delta);

        for (int i=0;i<this.patrols.size();i++) {
            Patrol patrol = this.patrols.get(i);
            patrol.move(0.05);
            if (patrol.getHP() <= 0) {
                patrols.remove(patrol);
            }
        }

        for (int i = 0; i < station.getTrucks().size(); i++) {
            FireTruck truck = station.getTruck(i);

            truck.move();
            truck.updateSpray();

            // checks power up collision with fire truck
            for (PowerUp power : powerUps) {
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

            // checks patrol collision with fire trucks
            for (Patrol patrol : this.patrols) {
                if (patrol.collidesWithTruck(truck, station)) {
                    patrolCollision(patrol, truck);
                }
            }

            if (bossPatrol != null){
                if (bossPatrol.collidesWithTruck(truck, station)) {
                    patrolCollision(bossPatrol, truck);
                }
            }

            checkIfTruckDestroyed(truck);
        }

        if (!station.isAlive()) {
            if(!(gameState.hasStationDestoyed())){
                gameState.setStationDestoyed();
                if (SoundFX.music_enabled) SoundFX.sfx_fortress_destroyed.play();
                deadEntities.add(station.getDestroyedStation());
                clearSelectedEntity(bossPatrol);
                clearSelectedEntity(station);
                bossPatrol = null;
            }
        }

        if (bossPatrol != null) {
            if (bossPatrol.getHP() <= 0) {
                bossPatrol = null;
            } else {
                bossPatrol.updateBossSpray();
                if ((bossPatrol.inShootingPosition())) bossPatrol.attack(station);
                else bossPatrol.move(0.01);
            }
        } else {
            if (!gameState.hasStationDestoyed() && gameState.getNumDestroyedFortresses() == level.getFortressesDestroyedBeforeBoss())
                bossPatrol = new BossPatrol(PatrolType.Boss, fortresses.get(0).getPosition(), station.getCentrePosition());
        }

        for (int i = 0; i < this.fortresses.size(); i++) {
            Fortress fortress = this.fortresses.get(i);

            boolean hitTruck = fortress.updateBombs();
            if (hitTruck) {
                camShake.shakeIt(.2f, 2f);
            }

            // check if fortress is destroyed
            if (fortress.getHP() <= 0) {
                gameState.addFortress();
                if (gameState.getNumDestroyedFortresses() == level.getFortressesDestroyedBeforeBoss())
                    bossPatrol = new BossPatrol(PatrolType.Boss, fortresses.get(0).getPosition(), station.getCentrePosition());
                deadEntities.add(fortress.createDestroyedFortress());
                float x = fortress.getPosition().x;
                float y = fortress.getPosition().y;
                this.vfx.add(new VFX(0, new Vector2(x-1f, y-3f)));
                this.vfx.add(new VFX(0, new Vector2(x-3.5f, y-3)));
                this.vfx.add(new VFX(0, new Vector2(x-3f, y-2f)));
                this.vfx.add(new VFX(0, new Vector2(x-2f, y-2.5f)));
                clearSelectedEntity(fortress);
                this.fortresses.remove(fortress);
                if (SoundFX.music_enabled) SoundFX.sfx_fortress_destroyed.play();
            }

        }

        updatePowerUps();

        if (gameState.getTrucksInAttackRange() > 0 && SoundFX.music_enabled && truckAttack && !gameState.isDancing){
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
        mapBatch.dispose();
        mapRenderer.dispose();
        shapeMapRenderer.dispose();
    }

    /**
     * Spawns initial patrols
     */
    private void generatePatrols() {
        for (int i=0; i<level.getNumPatrols(); i++) {
            patrols.add(generateRandomPatrol());
        }
    }

    /**
     * Generate a random patrol with a unique name
     * @return  new patrol
     */
    private Patrol generateRandomPatrol() {
        PatrolType type = generateRandomPatrolType();
        int patrolOfType = 0;
        for (Patrol patrol : patrols) if (patrol.getType().equals(type)) patrolOfType++;
        if (patrolOfType == 0)
            return new Patrol(type, level.getMapWidth(), level.getMapHeight(), type.name() + " Patrol 1");
        return new Patrol(type, level.getMapWidth(), level.getMapHeight(), type.name() + " Patrol " + (patrolOfType + 1));
    }

    /**
     * Generates a random patrol type
     *
     * @return  patrol type
     */
    private PatrolType generateRandomPatrolType() {
        PatrolType[] types = new PatrolType[]{PatrolType.Blue, PatrolType.Green, PatrolType.Peach, PatrolType.Violet, PatrolType.Yellow};
        return types[new Random().nextInt(types.length)];
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

    /**
     * Check if a fire truck has been destroyed
     *
     * @param truck fire truck to check
     */
    void checkIfTruckDestroyed(FireTruck truck) {
        if (truck.getHP() <= 0) {
            gameState.removeFireTruck();
            clearSelectedEntity(truck);
            station.destroyTruck(truck);
            if (truck.equals(this.selectedTruck)) {
                this.selectedTruck = null;
            }
        }
    }

    /**
     * Resets the selected entity, called when the entity dies
     * and only clears to selected entity if it has just died
     *
     * @param entity    to check if selected, if so clear
     */
    private void clearSelectedEntity(Object entity) {
        selectedEntity = (selectedEntity == entity) ? null : selectedEntity;
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
        return ((TiledMapTileLayer) mapLayers.get("collisions")).getCell(x, y) != null;
    }

    /**
     * Changes from GameScreen to Control screen, passing "game" so that when
     * the player exits the control screen, it knows to return to the Game
     */
    public void toControlScreen() {
        game.setScreen(new ControlsScreen(game, this, "game"));
    }

    @Override
    public void changeSound() { SoundFX.toggleMusic(this); }

    /** Exits the main game screen and goes to the menu, called when the home
     * button is clicked */
    public void toHomeScreen() {
        SoundFX.sfx_truck_attack.stop();
        game.setScreen(new MenuScreen(game));
    }

    /**
     * Starts a dance-off between the given firetruck and the given ET
     * @param firetruck firetruck to be converted into fireman
     * @param patrol        patrol to be used in the dance game
     */
    public void doDanceOff(FireTruck firetruck, Patrol patrol) {
        gameState.isDancing = true;
        if (selectedTruck != null) selectedTruck.clearPathSegment();
        game.setScreen(new DanceScreen(game, gameState,this, firetruck, patrol));
    }

    /**
     * Creates a new FireEngine, plays a sound and adds it gameState to track
     * @param type Type of truck to be spawned (Ocean, Speed)
     */
    private void spawn(FireTruckType type) {
        if (SoundFX.music_enabled) SoundFX.sfx_truck_spawn.play(0.1f);
        station.spawn(new FireTruck(this, new Vector2(station.getPosition().x + 1 + station.getTrucks().size(), station.getPosition().y), type));
        gameState.addFireTruck();
    }

    /**
     * Toggles between PLAY, PAUSE and FREEZE state when the Pause button is clicked
     * or when the SPACE bar is clicked
     *
     * @param action    the new state of the game
     */
    public void changeState(PlayState action) {
        switch (action) {
            case FREEZE:
                if (state.equals(PlayState.FREEZE)) {
                    state = PlayState.PLAY;
                    station.recalculateTruckPaths();
                    freezeCooldown = level.getTimeTillNextFreeze();
                } else if (freezeCooldown < 0) {
                    state = PlayState.FREEZE;
                    freezeCooldown = Constants.FREEZE_TIME;
                }
                break;
        }
    }

    /**
     * Enables/Disables auto attack
     */
    public void toggleTruckAttack() {
        truckAttack = !truckAttack;
        gui.updateAttackMode(truckAttack);
    }


    /**
     * Populate powerUpLocations with all the tiles that
     * are roads therefore a power up can spawn there
     *
     * @param level used to determine the tile map dimensions
     *              which depends on the map and therefore
     *              difficulty level current being played
     */
    private void generatePowerUpLocations(DifficultyLevel level) {
        ArrayList<Vector2> bayTiles = station.getBayTiles();
        for (int width = 0; width < level.getMapWidth(); width++){
            for (int height = 0 ; height < level.getMapHeight(); height++){
                Vector2 tile = new Vector2(width, height);
                if (isRoad(width, height) && !bayTiles.contains(tile)){
                    powerUpLocations.add(tile);
                }
            }
        }
    }


    /**
     * Checks if a new power up can spawn
     */
    private void canCreatePowerUp() {
        if (powerUps.size() < Constants.NUMBER_OF_POWERUPS) {
            Vector2 powerupLocation = generateRandomLocation();
            if (!checkIfPowerupInLocation(powerupLocation)) {
                createPowerUp(powerupLocation);
            }
        }
    }

    /**
     * Generates a random power up location
     *
     * @return  location
     */
    private Vector2 generateRandomLocation() {
        return powerUpLocations.get(new Random().nextInt(powerUpLocations.size()));
    }

    /**
     * Checks if a powerup is already in a location that a new
     * powerup is trying to spawn at
     *
     * @param newPowerUpLocation    attempted location to spawn at
     * @return  <code>true</code>   tile is occupied
     *          <code>false</code>  tile is not occupied
     */
    private boolean checkIfPowerupInLocation(Vector2 newPowerUpLocation) {
        for (PowerUp power : powerUps) {
            if (power.getPosition().equals(newPowerUpLocation)) return true;
        }
        return false;
    }

    /**
     * Create random power up
     *
     * @param location  to spawn power up
     */
    private void createPowerUp(Vector2 location) {
        ArrayList<PowerUp> possiblePowerUp = PowerUp.createNewPowers(location);
        Random rand = new Random();
        int index = rand.nextInt(possiblePowerUp.size());
        PowerUp powerup = possiblePowerUp.get(index);
        powerup.update();
        powerUps.add(powerup);
    }


    /** This updates the active power ups, if the power up has been
     * active for longer then the duration of the power up then it
     * is removed */
    public void updatePowerUps() {
        ArrayList<PowerUp> powerUpsToRemove = new ArrayList<>();

        for (PowerUp power : powerUps) {
            power.update();
            if (power.getCanBeDestroyed()) powerUpsToRemove.add(power);
        }

        powerUps.removeAll(powerUpsToRemove);
    }

    /** The method for giving trucks that have the same end tiles adjacent end tiles
     * so that they do not end up on the same tile
     */
    public void shortenActiveSegment() {
        selectedTruck.pathSegment.removeLast();
    }

    /**
     * When save is initiated in the minigame screen, the minigame
     * state is added to the save file too
     *
     * @param minigameMap   patrol and firetruck map
     */
    public void saveGameFromMinigame(OrderedMap<String, String> minigameMap) {
        saveMap = new OrderedMap<>();
        saveMap.put("Minigame", minigameMap);
        saveGameState();
    }

    /**
     * Save the game to a JSON file which can then be resumed later
     */
    public void saveGameState() {
        Json json = new Json(JsonWriter.OutputType.json);
        String timestamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String enTimestamp = new SimpleDateFormat("dd MMM YYYY HH:mm:ss").format(new Date());

        OrderedMap<String, Object> entitiesMap = new OrderedMap<>();
        entitiesMap.put("FireStation", this.station.getDescriptor());
        entitiesMap.put("FireTrucks", station.getFireTrucksDescriptor());
        entitiesMap.put("Fortresses", this.getFortressesDescriptor());
        entitiesMap.put("Patrols", this.getPatrolsDescriptor());
        if (bossPatrol != null) entitiesMap.put("Boss Patrol", bossPatrol.getDescriptor());

        if (saveMap == null) saveMap = new OrderedMap<>();

        saveMap.put("Timestamp", timestamp);
        saveMap.put("enTimestamp", enTimestamp);
        saveMap.put("Entities", entitiesMap);
        saveMap.put("Difficulty", difficultyControl);
        saveMap.put("Difficulty Level", difficultyLevel);
        saveMap.put("GameState", gameState);

        FileHandle file = Gdx.files.local("saves/" + timestamp + "/data.json");
        file.writeString(json.prettyPrint(saveMap),false);

        takeScreenshot(timestamp);
    }

    /**
     * Take screenshot of the screen, what the user can see
     * when the save button is clicked for the save screen
     * @param filename  to save to
     */
    private void takeScreenshot(String filename) {
        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);

        // this loop makes sure the whole screenshot is opaque and looks exactly like what the user is seeing
        for(int i = 4; i < pixels.length; i += 4)
            pixels[i - 1] = (byte) 255;

        Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
        PixmapIO.writePNG(Gdx.files.local("saves/" + filename + "/screenshot.png"), pixmap);
        pixmap.dispose();
    }

    /**
     * Get the list of fortress descriptor to be saved
     * @return  fortress descriptors
     */
    private Desc.Fortress[] getFortressesDescriptor() {
        Desc.Fortress[] fortresses = new Desc.Fortress[this.fortresses.size()];
        for (int i=0; i<fortresses.length; i++) {
            fortresses[i] = this.fortresses.get(i).getSave();
        }
        return fortresses;
    }

    /**
     * Get the list of patrol descriptors to be saved
     * @return  patrol descriptors
     */
    private Desc.Patrol[] getPatrolsDescriptor() {
        Desc.Patrol[] patrols = new Desc.Patrol[this.patrols.size()];
        for (int i=0; i<patrols.length; i++) {
            patrols[i] = this.patrols.get(i).getDescriptor();
        }
        return patrols;
    }


    /**
     * Called when a truck collides with a patrol.
     * Pauses the game, creates a warning sign at the collision location
     * and enters the minigame after a 3 second pause.
     *
     * @param patrol    patrol to check if collided with truck
     * @param truck     truck to check if collided with patrol
     */
    private void patrolCollision(Patrol patrol, FireTruck truck) {
        gameState.setHitPatrol(true);
        gameState.isDancing = true;
        this.warn = new Warning(truck.getPosition());
        if (SoundFX.music_enabled) {
            SoundFX.sfx_soundtrack.stop();
            SoundFX.sfx_patrolCollision.play();
        }
        setState(PlayState.PAUSE);
        Timer.schedule(new Timer.Task() {

            @Override
            public void run() {
                setState(PlayState.PLAY);
                doDanceOff(truck, patrol);
            }

        }, 3);
    }

    public boolean isNotPaused() {
        return state != PlayState.PAUSE;
    }

    public void setState(PlayState state){
        this.state = state;
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

    public ArrayList<Patrol> getPatrols() {
        return this.patrols;
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

    public void setFreezeCooldown(float time) { freezeCooldown = time; }

    public BossPatrol getBossPatrol() {
        return this.bossPatrol;
    }

    public FireTruck getSelectedTruck() {
        return this.selectedTruck;
    }

    public void setSelectedTruck(FireTruck fireTruck) {
        this.selectedTruck = fireTruck;
    }

    public ArrayList<PowerUp> getPowerUps() {
        return this.powerUps;
    }

}