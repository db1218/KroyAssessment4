package com.mozarellabytes.kroy.Utilities;

/**********************************************************************************
                                Added for assessment 4
 **********************************************************************************/

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruckType;

import static com.mozarellabytes.kroy.Entities.FireTruckType.*;

/** Added for assessment 4 to implement the different difficulty levels
 *
 * The actual level which defines many different variables within
 * the game which change depending on the difficulty to change
 * the easy or hard the game is to play
 */
public enum DifficultyLevel {

    Easy(new TmxMapLoader().load("maps/Easy.tmx"),
            40,
            24,
            0,
            80f,
            4,
            5,
            6f,
            RubyEasy,
            SapphireEasy,
            AmethystEasy,
            EmeraldEasy,
            2,
            new Vector2(3, 9),
            new Vector2(22, 13),
            new Vector2(30, 21.5f),
            new Vector2(15.5f, 19.5f),
            new Vector2(35, 4.5f),
            new Vector2(7, 1.5f),
            new Vector2(22, 2)),

    Medium(new TmxMapLoader().load("maps/Medium.tmx"),
            40,
            24,
            1,
            60f,
            10,
            4,
            10f,
            RubyMedium,
            SapphireMedium,
            AmethystMedium,
            EmeraldMedium,
            4,
            new Vector2(24, 5),
            new Vector2(28.5f, 21.5f),
            new Vector2(3.5f, 4.5f),
            new Vector2(8.5f, 20f),
            new Vector2(36f, 8.5f),
            new Vector2(26.5f, 12.5f),
            new Vector2(12.5f, 12.5f)),

    Hard(new TmxMapLoader().load("maps/Hard.tmx"),
            48,
            29,
            3,
            50f,
            20,
            3,
            15f,
            RubyHard,
            SapphireHard,
            AmethystHard,
            EmeraldHard,
            7,
            new Vector2(2, 7),
            new Vector2(12,23.5f),
            new Vector2(30.5f, 22.5f),
            new Vector2(16.5f,3.5f),
            new Vector2(32f,1.5f),
            new Vector2(41.95f, 23.5f),
            new Vector2(44f,11f));

    final TiledMap map;
    final int mapWidth;
    final int mapHeight;

    final Vector2 stationLocation;

    final Vector2 revsLocation;
    final Vector2 walmgateLocation;
    final Vector2 railwayLocation;
    final Vector2 cliffordLocation;
    final Vector2 museumLocation;
    final Vector2 centralHallLocation;

    final int startDifficultyLevel;
    final float difficultyChangeInterval;
    final int timeTillPowerup;
    final int fortressesDestroyedBeforeBoss;
    final float timeTillNextFreeze;
    float additionalAP;
    final int numPatrols;

    final FireTruckType ruby;
    final FireTruckType sapphire;
    final FireTruckType amethyst;
    final FireTruckType emerald;

    /**
     * Constructor for Difficulty Level
     *
     * @param map                           the tiled map for this level
     * @param mapWidth                      the width of this map
     * @param mapHeight                     the height of this map
     * @param revsLocation                  the location of the revolution fortress
     * @param startDifficultyLevel          the starting difficulty multiplier
     * @param difficultyChangeInterval      how often the difficulty increase
     * @param timeTillPowerup               how often power ups spawn
     * @param fortressesDestroyedBeforeBoss how many fortresses destroyed before boss spawns
     * @param timeTillNextFreeze            freeze cool down
     * @param ruby                          the ruby fire truck type
     * @param sapphire                      the sapphire fire truck type
     * @param amethyst                      the amethyst fire truck type
     * @param emerald                       the emerald fire truck type
     * @param numPatrols                    number of patrols roaming the map
     */
    DifficultyLevel(TiledMap map, int mapWidth, int mapHeight, int startDifficultyLevel,
                    float difficultyChangeInterval, int timeTillPowerup, int fortressesDestroyedBeforeBoss,
                    float timeTillNextFreeze, FireTruckType ruby, FireTruckType sapphire,
                    FireTruckType amethyst, FireTruckType emerald, int numPatrols, Vector2 stationLocation,
                    Vector2 revsLocation, Vector2 walmgateLocation, Vector2 railwayLocation, Vector2 cliffordLocation,
                    Vector2 museumLocation, Vector2 centralHallLocation) {

        this.map = map;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        this.stationLocation = stationLocation;

        this.revsLocation = revsLocation;
        this.walmgateLocation = walmgateLocation;
        this.railwayLocation = railwayLocation;
        this.cliffordLocation = cliffordLocation;
        this.museumLocation = museumLocation;
        this.centralHallLocation = centralHallLocation;

        this.startDifficultyLevel = startDifficultyLevel;
        this.difficultyChangeInterval = difficultyChangeInterval;
        this.timeTillPowerup = timeTillPowerup;
        this.fortressesDestroyedBeforeBoss = fortressesDestroyedBeforeBoss;
        this.timeTillNextFreeze = timeTillNextFreeze;
        this.numPatrols = numPatrols;

        this.ruby = ruby;
        this.sapphire = sapphire;
        this.amethyst = amethyst;
        this.emerald = emerald;
    }

    public TiledMap getMap() {
        return this.map;
    }

    public int getMapWidth() {
        return this.mapWidth;
    }

    public int getMapHeight() {
        return this.mapHeight;
    }

    public Vector2 getStationLocation() {
        return this.stationLocation;
    }

    public Vector2 getRevsLocation() {
        return this.revsLocation;
    }

    public Vector2 getWalmgateLocation() {
        return this.walmgateLocation;
    }

    public Vector2 getRailwayLocation() {
        return this.railwayLocation;
    }

    public Vector2 getCliffordLocation() {
        return this.cliffordLocation;
    }

    public Vector2 getMuseumLocation() {
        return this.museumLocation;
    }

    public Vector2 getCentralHallLocation() {
        return this.centralHallLocation;
    }

    public int getStartDifficultyLevel() {
        return this.startDifficultyLevel;
    }

    public float getDifficultyChangeInterval() {
        return this.difficultyChangeInterval;
    }

    public int getTimeTillPowerup() {
        return this.timeTillPowerup;
    }

    public int getFortressesDestroyedBeforeBoss() {
        return this.fortressesDestroyedBeforeBoss;
    }

    public float getTimeTillNextFreeze() {
        return this.timeTillNextFreeze;
    }

    public int getNumPatrols() {
        return this.numPatrols;
    }

    public FireTruckType getRuby() {
        return this.ruby;
    }

    public FireTruckType getSapphire() {
        return this.sapphire;
    }

    public FireTruckType getAmethyst() {
        return this.amethyst;
    }

    public FireTruckType getEmerald() {
        return this.emerald;
    }
}
