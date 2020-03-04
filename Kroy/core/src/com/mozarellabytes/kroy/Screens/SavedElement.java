package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.OrderedMap;
import com.mozarellabytes.kroy.Descriptors.Desc;
import com.mozarellabytes.kroy.Entities.FireStation;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Entities.Fortress;
import com.mozarellabytes.kroy.Entities.Patrol;
import com.mozarellabytes.kroy.GameState;
import com.mozarellabytes.kroy.Utilities.DifficultyControl;

import java.util.ArrayList;

public class SavedElement {

    private String timestamp;
    private Texture screenshot;
    private Label title;
    private Label progressStats;
    private OrderedMap<String, Object> data;
    private FileHandle file;

    // objects
    private FireStation fireStation;
    private ArrayList<Fortress> fortresses;
    private ArrayList<Patrol> patrols;
    private GameState gameState;
    private DifficultyControl difficultyControl;

    public SavedElement(String timestamp) {
        Json json = new Json();
        this.timestamp = timestamp;
        file = Gdx.files.local("saves/" + timestamp + ".json");
        data = json.fromJson(OrderedMap.class, file.readString());
        OrderedMap<String, Object> entities = (OrderedMap<String, Object>) data.get("Entities");

        // game state
        gameState = (GameState) data.get("GameState");

        // fire station
        Desc.FireStation fireStationDesc = (Desc.FireStation) entities.get("FireStation");
        fireStation = new FireStation(fireStationDesc.x, fireStationDesc.y, fireStationDesc.health);

        // fire trucks
        Array fireTruckArray = (Array) entities.get("FireTrucks");
        for (int i=0; i<fireTruckArray.size; i++) {
            Desc.FireTruck desc = json.fromJson(Desc.FireTruck.class, fireTruckArray.get(i).toString());
            fireStation.spawn(new FireTruck(desc.x, desc.y, desc.type, desc.health, desc.reserve, desc.rotation));
        }

        // fortresses
        fortresses = new ArrayList<>();
        Array fortressArray = (Array) entities.get("Fortresses");
        for (int i=0; i<fortressArray.size; i++) {
            Desc.Fortress desc = json.fromJson(Desc.Fortress.class, fortressArray.get(i).toString());
            fortresses.add(new Fortress(desc.x, desc.y, desc.type, desc.health));
        }

        // patrols
        patrols = new ArrayList<>();
        Array patrolArray = (Array) entities.get("Patrols");
        for (int i=0; i<patrolArray.size; i++) {
            Desc.Patrol desc = json.fromJson(Desc.Patrol.class, patrolArray.get(i).toString());
            patrols.add(new Patrol(desc.type, desc.health, desc.x, desc.y, desc.path));
        }

        difficultyControl = (DifficultyControl) data.get("Difficulty");

    }

    public FireStation getFireStation() {
        return fireStation;
    }

    public ArrayList<FireTruck> getFireTrucks() {
        return this.fireStation.getTrucks();
    }

    public ArrayList<Fortress> getFortresses() {
        return fortresses;
    }

    public GameState getGameState() {
        return gameState;
    }

    public DifficultyControl getDifficultyControl() {
        return this.difficultyControl;
    }

    public ArrayList<Patrol> getPatrols() {
        return this.patrols;
    }

    public String getTimestamp() {
        return this.timestamp;
    }
}
