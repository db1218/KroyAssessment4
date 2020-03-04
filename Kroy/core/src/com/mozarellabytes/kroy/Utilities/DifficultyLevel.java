package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public enum DifficultyLevel {

    Easy(new TmxMapLoader().load("maps/YorkMap.tmx")),
    Medium(new TmxMapLoader().load("maps/SimpleMap/small_map.tmx"));

    TiledMap map;

    DifficultyLevel(TiledMap map) {
        this.map = map;
    }

    public TiledMap getMap() { return this.map; }

   // TiledMap map = new TmxMapLoader().load("maps/YorkMap.tmx");
}
