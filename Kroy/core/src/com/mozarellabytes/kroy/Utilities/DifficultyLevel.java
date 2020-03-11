package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Entities.FireTruckType;

public enum DifficultyLevel {

  // These are place holders for when we have the new map

   // Medium fortress locations: new Vector2(13.5f,3.5f), new Vector2(4.3f, 8.3f),
   // new Vector2(35.5f,5), new Vector2(31,10), new Vector2(15,20), new Vector2(22.1f,13.3f)

   // Hard fortress locations: new Vector2(12,23.5f), new Vector2(30.5f, 22.5f), new Vector2(16.5f,3.5f),
   //    new Vector2(32f,1.5f), new Vector2(41.95f, 23.5f), new Vector2(44f,11f)

    Easy(new TmxMapLoader().load("maps/Easy.tmx"), 20, 12, new Vector2(10,3),
            0, 80f, 4,4, 6f,
            FireTruckType.RubyEasy, FireTruckType.SapphireEasy, FireTruckType.AmethystEasy, FireTruckType.EmeraldEasy),

    Medium(new TmxMapLoader().load("maps/Medium.tmx"), 40, 24, new Vector2(13.5f,3.5f),
            1, 60f, 10,2, 10f,
            FireTruckType.RubyMedium, FireTruckType.SapphireMedium, FireTruckType.AmethystMedium, FireTruckType.EmeraldMedium),

    Hard(new TmxMapLoader().load("maps/Hard.tmx"), 48, 29, new Vector2(12,23.5f),
            3, 50f, 20,1,15f,
            FireTruckType.RubyHard, FireTruckType.SapphireHard, FireTruckType.AmethystHard, FireTruckType.EmeraldHard);


    TiledMap map;
    int mapWidth;
    int mapHeight;

    Vector2 revsLocation;
    Vector2 walmgateLocation;
    Vector2 railwayLocation;
    Vector2 cliffordLocation;
    Vector2 museumLocation;
    Vector2 centralHallLocation;

    int startDifficultyLevel;
    float difficultyChangeInterval;
    int timeTillPowerup;
    int fortressesDestroyedBeforeBoss;
    float timeTillNextFreeze;
    float additionalAP;

    FireTruckType ruby;
    FireTruckType sapphire;
    FireTruckType amethyst;
    FireTruckType emerald;

    DifficultyLevel(TiledMap map, int mapWidth, int mapHeight, Vector2 revsLocation, int startDifficultyLevel,
                    float difficultyChangeInterval, int timeTillPowerup, int fortressesDestroyedBeforeBoss,
                    float timeTillNextFreeze, FireTruckType ruby, FireTruckType sapphire,
                    FireTruckType amethyst, FireTruckType emerald) {
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

    public FireTruckType getRuby() { return this.ruby; }

    public FireTruckType getSapphire() { return this.sapphire; }

    public FireTruckType getAmethyst() { return this.amethyst; }

    public FireTruckType getEmerald() { return this.emerald; }

}
