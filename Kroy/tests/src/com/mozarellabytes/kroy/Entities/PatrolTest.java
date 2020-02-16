package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.GdxTestRunner;
import com.mozarellabytes.kroy.Screens.GameScreen;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.*;
@RunWith(GdxTestRunner.class)
public class PatrolTest {

    //@Mock
    //PatrolType testPatrolType;

    @Mock
    GameScreen gameScreenMock;



    @Test
    public void definePath() {
        PatrolType testPatrolType=new PatrolType((new Texture(Gdx.files.internal("sprites/Patrol/greenPatrol.png")), 250, 1, "Green Patrol", 7, 0.16f, 100, new Vector2(17, 2), new Vector2(47, 2), new Vector2(47, 20), new Vector2(17, 20)));
        Patrol testPatrol = new Patrol(gameScreenMock, testPatrolType) ;
        testPatrol.setTargetX(5);
        testPatrol.setPositionX(3);
        testPatrol.definePath();
        assertEquals(4, testPatrol.getPositionX());

        testPatrol.setTargetY(5);
        testPatrol.setPositionY(3);
        testPatrol.definePath();
        assertEquals(2, testPatrol.getPositionY());

        testPatrol.setTargetY(5);
        testPatrol.setPositionY(3);
        testPatrol.definePath();
        assertEquals(2, testPatrol.getPositionY());

        testPatrol.setTargetY(5);
        testPatrol.setPositionY(3);
        testPatrol.definePath();
        assertEquals(4, testPatrol.getPositionY());


    }
}