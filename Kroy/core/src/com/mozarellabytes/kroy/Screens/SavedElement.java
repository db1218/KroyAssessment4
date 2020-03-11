package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
import com.mozarellabytes.kroy.Utilities.DifficultyLevel;

import java.util.ArrayList;

public class SavedElement {

    private final String timestamp;
    private final String getEnTimestamp;

    // objects
    private final FireStation fireStation;
    private final ArrayList<Fortress> fortresses;
    private final ArrayList<Patrol> patrols;
    private final GameState gameState;
    private final DifficultyControl difficultyControl;
    private final DifficultyLevel difficultyLevel;

    public SavedElement(String timestamp) {
        Json json = new Json();
        this.timestamp = timestamp;
        FileHandle file = Gdx.files.local("saves/" + timestamp + "/data.json");
        OrderedMap<String, Object> data = json.fromJson(OrderedMap.class, file.readString());
        OrderedMap<String, Object> entities = (OrderedMap<String, Object>) data.get("Entities");

        this.getEnTimestamp = (String) data.get("enTimestamp");

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

        difficultyLevel = (DifficultyLevel) data.get("Difficulty Level");

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

    public String getEnTimestamp() {
        return this.getEnTimestamp;
    }

    public String listAliveFireTrucks() {
        StringBuilder list = new StringBuilder();
        for (FireTruck truck : fireStation.getTrucks()) {
            list.append("\n  - ").append(truck.type.getName());
        }
        return list.toString();
    }

    public String listAliveFortresses() {
        StringBuilder list = new StringBuilder();
        for (Fortress fortress : fortresses) {
            list.append("\n  - ").append(fortress.getFortressType().getName());
        }
        return list.toString();
    }

    public DifficultyLevel getDifficultyLevel() {
        return this.difficultyLevel;
    }
}
