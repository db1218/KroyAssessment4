/*
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
        Patrol testPatrol = new Patrol(gameScreenMock, PatrolType.Blue) ;
        testPatrol.setTargetX(5);
        testPatrol.setPositionX(3);
        testPatrol.definePath();
        assertEquals(4, testPatrol.getPosition().x);

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
}*/
