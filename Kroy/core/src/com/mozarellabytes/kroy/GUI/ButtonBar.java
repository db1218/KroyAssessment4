package com.mozarellabytes.kroy.GUI;

import com.badlogic.gdx.utils.OrderedMap;
import com.mozarellabytes.kroy.Screens.GameScreen;

public interface ButtonBar {

    void toHomeScreen();

    void toControlScreen();

    void changeSound();

    void saveGameState();

    void setState(GameScreen.PlayState state);

    GameScreen.PlayState getState();

}
