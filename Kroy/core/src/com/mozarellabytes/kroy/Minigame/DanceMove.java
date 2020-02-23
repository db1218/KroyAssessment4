package com.mozarellabytes.kroy.Minigame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Possible moves that can be taken by dancers
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

    DanceMove(Texture fireMan, Texture ET, Texture arrow) {
        this.firefighter = fireMan;
        this.ET = ET;
        this.arrow = arrow;
    }

    public Texture getFirefighterTexture(){ return this.firefighter; }
    public Texture getETTexture(){return this.ET; }
    public Texture getArrowTexture(){return this.arrow; }

}
