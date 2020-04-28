package com.mozarellabytes.kroy.Utilities;

/**********************************************************************************
                            Added for assessment 4
 **********************************************************************************/

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.OrderedMap;
import com.mozarellabytes.kroy.Entities.*;
import com.mozarellabytes.kroy.GameState;

import java.util.ArrayList;

/** Added for assessment 4 to implement save functionality
 *
 * SavedElement contains all the information needed to save
 * and load the state of a game to and from a file
 */
public class SavedElement {

    // time stamps
    private final String timestamp;
    private final String getEnTimestamp;

    // objects to save
    private final FireStation fireStation;
    private final ArrayList<Fortress> fortresses;
    private final ArrayList<Patrol> patrols;
    private final BossPatrol bossPatrol;
    private final GameState gameState;
    private final DifficultyControl difficultyControl;
    private final DifficultyLevel difficultyLevel;

    private final FireTruck minigameFireTruck;
    private final Patrol minigamePatrol;

    /**
     * Constructor for creating a SavedElement
     * @param timestamp when save occurred
     */
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
            patrols.add(new Patrol(desc.type, desc.health, desc.x, desc.y, desc.path, desc.name));
        }

        Desc.Patrol bossDesc = (Desc.Patrol) entities.get("Boss Patrol");
        if (bossDesc == null) bossPatrol = null;
        else bossPatrol = new BossPatrol(bossDesc.type, bossDesc.health, bossDesc.x, bossDesc.y, bossDesc.path, bossDesc.name);

        difficultyControl = (DifficultyControl) data.get("Difficulty");

        difficultyLevel = (DifficultyLevel) data.get("Difficulty Level");

        // if player was in minigame
        if (data.get("Minigame") != null) {
            OrderedMap<String, Object> minigameMap = (OrderedMap<String, Object>) data.get("Minigame");
            minigameFireTruck = findFireTruck((String) minigameMap.get("FireTruck"));
            minigamePatrol = findPatrol((String) minigameMap.get("Patrol"));
        } else {
            minigameFireTruck = null;
            minigamePatrol = null;
        }

    }

    /**
     * Finds a patrol by their name
     *
     * @param patrolName    name input
     * @return              patrol output
     */
    private Patrol findPatrol(String patrolName) {
        for (Patrol patrol : getPatrols())
            if (patrol.getName().equals(patrolName))
                return patrol;
        throw new RuntimeException("Cannot find patrol: " + patrolName);
    }

    /**
     * Finds a fire truck by their name
     *
     * @param fireTruckName name input
     * @return              fire truck output
     */
    private FireTruck findFireTruck(String fireTruckName) {
        for (FireTruck fireTruck : getFireTrucks())
            if (fireTruck.getType().getName().equals(fireTruckName))
                return fireTruck;
        throw new RuntimeException("Cannot find truck: " + fireTruckName);
    }

    /**
     * Gets a list of trucks as a string
     *
     * @return  list
     */
    public String listAliveFireTrucks() {
        StringBuilder list = new StringBuilder();
        for (FireTruck truck : fireStation.getTrucks()) {
            list.append("\n  - ").append(truck.type.getName());
        }
        return list.toString();
    }

    /**
     * Gets a list of fortresses as a string
     *
     * @return  list
     */
    public String listAliveFortresses() {
        StringBuilder list = new StringBuilder();
        for (Fortress fortress : fortresses) {
            list.append("\n  - ").append(fortress.getType().getName());
        }
        return list.toString();
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

    public BossPatrol getBossPatrol() {
        return this.bossPatrol;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getEnTimestamp() {
        return this.getEnTimestamp;
    }

    public DifficultyLevel getDifficultyLevel() {
        return this.difficultyLevel;
    }

    public boolean wasInMinigame() {
        return minigameFireTruck != null || minigamePatrol != null;
    }

    public FireTruck getMinigameFireTruck() {
        return minigameFireTruck;
    }

    public Patrol getMinigamePatrol() {
        return minigamePatrol;
    }
}
