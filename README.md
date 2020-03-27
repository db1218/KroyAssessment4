# SEPR Assessment 4
![Java CI](https://github.com/db1218/KroyAssessment4/workflows/Java%20CI/badge.svg?branch=master)

[TOC]

## Introduction

The year is 2042 and York has been invaded by evil extra-terrestrials (ETs) from planet Kroy, who have set up fortresses in key locations (e.g. York Minster, Clifford Tower) around the city. While ETs are more technologically advanced and outgun humans, they have a major - and very convenient - weakness: they evaporate when they come in contact with water. As the leader of the Resistance, you have taken over York's old Fire Station and you are now in control of its fire engines. Your mission is to use the fire engines you control to flood the ET fortresses, and liberate York.

## Assessments

Kroy is a game developed by `Mozzarella Bytes`, a group of six computer science students undertaking a Software Engineering Project module.

### Assessment 1

*Requirements specification and design*

The assessment started with gathering requirements from our client and designing, which involved producing the following documents: *Requirements*, *Architecture*, *Method selection and Planning*, *Risk assessment and Mitigation*. We also produced a website to act as the public face of the game.

https://db1218.github.io/KroyAssessment4/assessment1/

### Assessment 2 

*Preliminary Implementation*

Next, we implemented core parts of the game, including the Map, Fire Station, Fire Engines, and Fortresses. We updated the previously listed deliverables including the now concrete architecture as well as producing testing and implementation documents.

We decided on a static top-down view game, where the player can control the route of the fire engines by "drawing" the route by which the fire engine will take using the mouse. When within range, the player can automatically spray a Fortress, dealing damage to it.

At the end of this assessment, we pitched our game to the other teams as each team had to choose another team's product to extend and improve upon for the next assessment.

https://db1218.github.io/KroyAssessment4/assessment2/

### Assessment 3

*Extension and Integration*

We chose to undertake `Salt and SEPR`'s game to extend. Their game is a dynamic top-down view, where the camera follows around a single truck that you are controlling with the keyboard. You can then aim and spray water with the mouse.

For this assessment we had to finish the game by making sure all initial requirements from the client had been satisfied. This mainly included implementing an embedded minigame, enemy patrols and the destruction of the fire station. We also had to update all of their deliverable documents in-line with the changes and new features we had implemented.

We also took over the group's website, which can be found updated here: https://sepr-docs.web.app/

Alike the previous assessment, we pitched our iteration of `Salt and SEPR`'s game to the other teams, who then had to choose a game to work on for the final assessment.

### Assessment 4

*Requirements Change*

The final assessment. We decided we wanted to complete our original game, and thankfully we had four iterations of our game to choose from as 4/9 groups chose our game in Assessment 3. After choosing `Team3`'s iteration due to their fun minigame,  innovative new features and well-written code, the client communicated some new features wanted for the game:

- Powerups fire engines can obtain
- Support for different levels of difficulty
- Ability for the player to save the game and resume later

As per with the previous assessments, we updated the deliverable documents and website in-line with changes we had made. *Evaluation and Testing* and *Project review* reports were also produced.

https://db1218.github.io/KroyAssessment4/assessment4/

## Features

### Maps

There are three maps currently, one for each difficulty. Each one is a tiled grid with roads by which Fire Engines can navigate along. The map also contains a number of Fortresses and a Fire Station.

### Fire Engines

Controlled by the player to attack fortresses. Each one has a unique set of properties, strengths and weaknesses. To get within range of a fortress, a Fire Engine follows a path drawn by the user along the roads. Once within range of a fortress, the Fire Engine can use its limited water supply to spray fortresses, dealing damage to them. Be wary though, these fortresses fight back and once the fire engine's health reaches zero it is destroyed and cannot respawn. Once all your Fire engines have been destroyed, you will lose the game.

### Fire Station

You can replenish the water and health of a fire engine by parking it back in the fire station. However, after you have destroyed a specified number of fortresses, the enemy will work out where your fire station is and head towards it. Upon reaching it, the fire station will be attacked until it is destroyed, at which fire engines can no longer be repaired or refilled.

### Fortresses

Setup in locations around the city, fortresses have the ability to attack nearby fire engines. Over time, a fortress can heal and repair, therefore it is a good idea to target one fortress at a time. As time progresses. fortresses get stronger and deal more damage to fire engines. Once all Fortresses have been destroyed, York will be liberated and you will win the game!

### Patrols

Patrols roam the city searching for fire engines. Once they come into contact with a fire truck, they will enter into a "dance off" to the death.

### Minigame

A Fire engine and Patrol will go head-to-head to determine who can bust the best moves on the dance floor. The player will by tasked to follow a dance routine using the arrow keys. If followed correctly, the player builds up a multiplier, which can be used to deal an increasing amount of damage to the patrol. If the player gets the routine wrong, the multiplier is dropped and the patrol deals damage. The minigame finishes when the fire engine or patrol is defeated and the result of the battle can be seen back in the main game.

### Sound Effects

To make the game come to life there are sound effects and some banging sound tracks throughout the game. These sounds can be toggled on or off.

### Freeze

The freeze function can be used throughout the game to allow the player to draw fire engine paths without being disturbed by fortresses or patrols. A path consists a set of points, where the shortest path between adjacent points determines the path the fire engine will take. These are called segments and can be undone and redone when in freeze mode to give the player full control of where each fire engine is going.

### Powerups

These appear randomly around the map and once driven over grants the player special abilities. These include `Freeze` sets freeze cooldown to zero, `Heart` replenishes the fire engine's health, `Range` temporarily increases the fire engine's range, `Shield` temporarily makes the fire engine immune to damage and `Water` which replenishes the fire engine's water supply.

If they are not used within a certain amount of time, they will de-spawn.

### GUI

#### Properties

In the top left corner, the properties of a selected entity can be viewed. To select an entity, you can simply click on it. Entities include `Fire Station` , `Fire Engines`,  `Patrols` and `Fortreesses`. You can always see the health of each entity, but also the unique stats for each individual entity.

#### Statuses

Below the properties is some information boxes. `Damage Increase` shows the time until the next increase of damage multiplier.`Damage Multiplier` shows the current fortress damage multiplier. `Freeze Cooldown`/`Freeze Avaliable` shows the current state of the freeze function, either how long left until it is available or that it is available. Finally, `Truck Attack` tells the user if the fire engines can attack fortresses within range or not.

#### Buttons

There are several buttons in the top right corner: `Save`, `Controls`, `Pause`, `Mute` and `Main Menu`.

### Difficulties

We offer three difficulty levels for the player to complete. Along with a unique map, the fortresses will deal more damage, the powerups will spawn less often, the freeze will have a longer cooldown, the number of patrols will increase, the fire station will be destroyed sooner and weaker/stronger fire engine stats. 

### Saves

If the player wants to save their progress, they can click the save button and the current state of the game will be saved. Later, the player can access this game save from the main menu and continue from right where they left off. This feature can support unlimited saves and saves can be browsed through the menu, where a screenshot and core progress information can be viewed. If a save is no longer required, it can be deleted.

## Controls 

### General

| Input | Name          | Functionality                                                |
| ----- | ------------- | ------------------------------------------------------------ |
| M     | Toggle sound  | Toggles the music and sound effects                          |
| C     | View controls | View a controls screen to remind you of basic controls during the game |
| S     | Save game     | Save the state of the game at that point in time             |
| ESC   | Pause game    | Puts the game in a pause state where nothing happens         |

### Main game

| Input       | Name                  | Functionality                                                |
| ----------- | --------------------- | ------------------------------------------------------------ |
| Mouse click | Select entities       | Click on the entities to view more information about them in the top left corner of the screen |
| Mouse drag  | Draw Fire Engine path | Starting at a Fire Engine or the end of a Fire Engine's path, you can drag and draw the path that the fire engine will follow |
| SPACE       | Freeze                | Freeze temporarily stops everything, allowing you to draw Fire Engine paths undisturbed. Once the freeze runs out, the chaos continues. |
| LEFT_ARROW  | Undo segment          | When in freeze, you create a new segment of path each time you release the mouse. You can undo the segment. |
| RIGHT_ARROW | Redo segment          | If you want to "undo an undo", use redo to make the segment reappear |
| A           | Toggle spray          | When enabled, Fire Engines will spray fortresses when within range. Otherwise they will not attack. |

### Minigame

| Input      | Name           | Functionality                                                |
| ---------- | -------------- | ------------------------------------------------------------ |
| ARROW_KEYS | Dance moves    | The player will be tasked to enter a sequence of arrow keys, these are entered with the keyboard arrow keys. |
| SPACE      | Use Multiplier | Multiplier can be used to deal damage to the patrol          |