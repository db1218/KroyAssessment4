package com.mozarellabytes.kroy.Minigame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

/**
 * Possible moves that can be taken by dancers
 */
public enum DanceMove {

    UP(new Texture(Gdx.files.internal("sprites/dance/firefighter5.png")), null, new Texture(Gdx.files.internal("sprites/dance/arrowUp.png"))),
    DOWN(new Texture(Gdx.files.internal("sprites/dance/firefighter6.png")), null, new Texture(Gdx.files.internal("sprites/dance/arrowDown.png"))),
    LEFT(new Texture(Gdx.files.internal("sprites/dance/firefighter3.png")), new Texture(Gdx.files.internal("sprites/dance/et2.png")), new Texture(Gdx.files.internal("sprites/dance/arrowLeft.png"))),
    RIGHT(new Texture(Gdx.files.internal("sprites/dance/firefighter4.png")), new Texture(Gdx.files.internal("sprites/dance/et3.png")), new Texture(Gdx.files.internal("sprites/dance/arrowRight.png"))),
    WAIT(new Texture(Gdx.files.internal("sprites/dance/firefighter2.png")), null, null),
    NONE(new Texture(Gdx.files.internal("sprites/dance/firefighter1.png")), new Texture(Gdx.files.internal("sprites/dance/et1.png")), null);

    private final Texture fireMan;
    private final Texture ET;
    private final Texture arrow;

    DanceMove(Texture fireMan, Texture ET, Texture arrow) {
        this.fireMan = fireMan;
        this.ET = ET;
        this.arrow = arrow;
    }

    public Texture getFireManTexture(){ return this.fireMan; }
    public Texture getETTexture(){return this.ET; }
    public Texture getArrowTexture(){return this.arrow; }

}
