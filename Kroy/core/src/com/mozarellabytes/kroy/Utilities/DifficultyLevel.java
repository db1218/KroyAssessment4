package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public enum DifficultyLevel {

    Easy(new TmxMapLoader().load("maps/Easy.tmx"), 20, 12),
    Medium(new TmxMapLoader().load("maps/Medium.tmx"), 40, 24);

    TiledMap map;
    int width;
    int height;

    DifficultyLevel(TiledMap map, int width, int height) {
        this.map = map;
        this.width = width;
        this.height = height;
    }

    public TiledMap getMap() { return this.map; }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

   // TiledMap map = new TmxMapLoader().load("maps/YorkMap.tmx");
}
