package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruckType;

/**
 * The actual level which defines many different variables within
 * the game which change depending on the difficulty to change
 * the easy or hard the game is to play
 */
public enum DifficultyLevel {

  // These are place holders for when we have the new map

   // Medium fortress locations: new Vector2(13.5f,3.5f), new Vector2(4.3f, 8.3f),
   // new Vector2(35.5f,5), new Vector2(31,10), new Vector2(15,20), new Vector2(22.1f,13.3f)

   // Hard fortress locations: new Vector2(12,23.5f), new Vector2(30.5f, 22.5f), new Vector2(16.5f,3.5f),
   //    new Vector2(32f,1.5f), new Vector2(41.95f, 23.5f), new Vector2(44f,11f)

    Easy(new TmxMapLoader().load("maps/Easy.tmx"), 20, 12, new Vector2(10,3),
            0, 80f, 4,4, 6f,
            FireTruckType.RubyEasy, FireTruckType.SapphireEasy, FireTruckType.AmethystEasy, FireTruckType.EmeraldEasy, 2),

    Medium(new TmxMapLoader().load("maps/Medium.tmx"), 40, 24, new Vector2(13.5f,3.5f),
            1, 60f, 10,2, 10f,
            FireTruckType.RubyMedium, FireTruckType.SapphireMedium, FireTruckType.AmethystMedium, FireTruckType.EmeraldMedium, 4),

    Hard(new TmxMapLoader().load("maps/Hard.tmx"), 48, 29, new Vector2(12,23.5f),
            3, 50f, 20,1,15f,
            FireTruckType.RubyHard, FireTruckType.SapphireHard, FireTruckType.AmethystHard, FireTruckType.EmeraldHard, 7);


    final TiledMap map;
    final int mapWidth;
    final int mapHeight;

    final Vector2 revsLocation;
    Vector2 walmgateLocation;
    Vector2 railwayLocation;
    Vector2 cliffordLocation;
    Vector2 museumLocation;
    Vector2 centralHallLocation;

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
    DifficultyLevel(TiledMap map, int mapWidth, int mapHeight, Vector2 revsLocation, int startDifficultyLevel,
                    float difficultyChangeInterval, int timeTillPowerup, int fortressesDestroyedBeforeBoss,
                    float timeTillNextFreeze, FireTruckType ruby, FireTruckType sapphire,
                    FireTruckType amethyst, FireTruckType emerald, int numPatrols) {
        this.map = map;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
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

    public TiledMap getMap() { return this.map; }

    public int getMapWidth() { return this.mapWidth; }

    public int getMapHeight() { return this.mapHeight; }

    public Vector2 getRevsLocation() { return this.revsLocation; }

    public Vector2 getWalmgateLocation() { return this.walmgateLocation; }

    public Vector2 getRailwayLocation() { return this.railwayLocation; }

    public Vector2 getCliffordLocation() { return this.cliffordLocation; }

    public Vector2 getMuseumLocation() { return this.museumLocation; }

    public Vector2 getCentralHallLocation() { return this.centralHallLocation; }

    public int getStartDifficultyLevel() { return  this.startDifficultyLevel; }

    public float getDifficultyChangeInterval() { return  this.difficultyChangeInterval; }

    public int getTimeTillPowerup() { return this.timeTillPowerup; }

    public int getFortressesDestroyedBeforeBoss() {return this.fortressesDestroyedBeforeBoss; }

    public float getTimeTillNextFreeze() { return this.timeTillNextFreeze;}

    public int getNumPatrols() {
        return this.numPatrols;
    }

    public FireTruckType getRuby() { return this.ruby; }

    public FireTruckType getSapphire() { return this.sapphire; }

    public FireTruckType getAmethyst() { return this.amethyst; }

    public FireTruckType getEmerald() { return this.emerald; }

}
