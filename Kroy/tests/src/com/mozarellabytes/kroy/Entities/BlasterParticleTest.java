package com.mozarellabytes.kroy.Entities;


import static org.junit.Assert.*;

import com.mozarellabytes.kroy.GdxTestRunner;
import com.mozarellabytes.kroy.Screens.GameScreen;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;



@RunWith(GdxTestRunner.class)
class BlasterParticleTest {
    @Mock
    GameScreen gameScreenMock;

    @Mock
    PatrolType testpatroltype;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    public void isHitTest() {
        Patrol aPatrol = new Patrol(gameScreenMock, testpatroltype);
        FireStation aTarget = new FireStation(1,5);
        BlasterParticle testParticle = new BlasterParticle(aPatrol, aTarget);
        testParticle.setCurrentPositionX(1);
        testParticle.setCurrentPositionY(5);
        assertTrue(testParticle.isHit());

        testParticle.setTargetPositionX(2);
        testParticle.setTargetPositionY(8);
        assertFalse(testParticle.isHit());

    }

}


