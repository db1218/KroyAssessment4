package com.mozarellabytes.kroy.Entities;

import com.mozarellabytes.kroy.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
@RunWith(GdxTestRunner.class)
public class ParticleTest {

    Particle waterParticle;

    Particle blasterParticle;

    @Before
    public void setUp() {
        Fortress fortress = new Fortress(5,5, FortressType.CentralHall);
        FireStation station = new FireStation(0, 0, 100);
        waterParticle = new Particle(station.getPosition(), fortress.getPosition(), fortress);
        blasterParticle = new Particle(fortress.getPosition(), station.getPosition(), station);
    }

    @Test
    public void isHitWaterParticle() {
        waterParticle.setPositionX(1);
        waterParticle.setPositionY(5);

        waterParticle.setTargetPositionX(1);
        waterParticle.setTargetPositionY(5);
        assertTrue(waterParticle.isHit());

        waterParticle.setPositionX(2);
        waterParticle.setPositionY(8);
        assertFalse(waterParticle.isHit());
    }

    @Test
    public void isHitBlasterParticle() {
        blasterParticle.setPositionX(1);
        blasterParticle.setPositionY(5);

        blasterParticle.setTargetPositionX(1);
        blasterParticle.setTargetPositionY(5);
        assertTrue(blasterParticle.isHit());

        blasterParticle.setPositionX(2);
        blasterParticle.setPositionY(8);
        assertFalse(blasterParticle.isHit());
    }
}