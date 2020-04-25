package com.mozarellabytes.kroy.Minigame;

/**********************************************************************************
                                Edited for assessment 4
 **********************************************************************************/

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Enum containing the available dance moves and their appropriate textures. This is
 * used in DanceScreen to render the firefighter, ET and arrows to the screen, in
 * DanceChoreographer to generate the moves for the routine and in DanceManager to
 * check if the player has pressed the key corresponding to the current move
 */
public enum DanceMove {

    UP(new Texture(Gdx.files.internal("sprites/dance/firefighterDown.png")), null, new Texture(Gdx.files.internal("sprites/dance/arrowUp.png"))),
    DOWN(new Texture(Gdx.files.internal("sprites/dance/firefighterUp.png")), null, new Texture(Gdx.files.internal("sprites/dance/arrowDown.png"))),
    LEFT(new Texture(Gdx.files.internal("sprites/dance/firefighterLeft.png")), new Texture(Gdx.files.internal("sprites/dance/ETLeft.png")), new Texture(Gdx.files.internal("sprites/dance/arrowLeft.png"))),
    RIGHT(new Texture(Gdx.files.internal("sprites/dance/firefighterRight.png")), new Texture(Gdx.files.internal("sprites/dance/ETRight.png")), new Texture(Gdx.files.internal("sprites/dance/arrowRight.png"))),
    WAIT(new Texture(Gdx.files.internal("sprites/dance/firefighterWait.png")), null, null),
    NONE(new Texture(Gdx.files.internal("sprites/dance/firefighterNone.png")), new Texture(Gdx.files.internal("sprites/dance/ETNone.png")), null);

    private final Texture firefighter;
    private final Texture ET;
    private final Texture arrow;

    /** Constructor for DanceMove
     *
     * @param fireMan the fire fighter's texture for this move
     * @param ET the ET's texture for this move
     * @param arrow the arrow texture for this move
     */
    DanceMove(Texture fireMan, Texture ET, Texture arrow) {
        this.firefighter = fireMan;
        this.ET = ET;
        this.arrow = arrow;
    }

    public Texture getFirefighterTexture(){ return this.firefighter; }
    public Texture getETTexture(){return this.ET; }
    public Texture getArrowTexture(){return this.arrow; }


}
