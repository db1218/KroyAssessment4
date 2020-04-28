package com.mozarellabytes.kroy.GUI;

/**********************************************************************************
                                Added for assessment 4
 **********************************************************************************/

import com.mozarellabytes.kroy.Screens.GameScreen;

/** Interface that screens need to implement if they wish to have
 * buttons to go to the home screen, the control screen, to turn
 * the sound on and off, to pause the game or to save the game
 * */

public interface ButtonBar {

    void toHomeScreen();

    void toControlScreen();

    void changeSound();

    void saveGameState();

    void setState(GameScreen.PlayState state);

    GameScreen.PlayState getState();

}
