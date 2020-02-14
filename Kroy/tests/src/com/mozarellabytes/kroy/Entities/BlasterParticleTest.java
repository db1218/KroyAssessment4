package com.mozarellabytes.kroy.Entities;


import static org.junit.jupiter.api.Assertions.*;


import com.mozarellabytes.kroy.Screens.GameScreen;
import org.junit.Rule;
import org.mockito.Mock;

import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;



class BlasterParticleTest {
    @Mock
    GameScreen gameScreenMock;

    @Mock
    PatrolType testpatroltype;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @org.junit.jupiter.api.Test
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


