package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.math.Vector2;
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
        waterParticle.createTargetPosition(new Vector2(1,5));

        for (int i=0; i<100; i++) waterParticle.updatePosition();

        assertTrue(waterParticle.isHit());

        waterParticle.setPositionX(2);
        waterParticle.setPositionY(8);
        assertFalse(waterParticle.isHit());
    }

    @Test
    public void isHitBlasterParticle() {
        blasterParticle.createTargetPosition(new Vector2(1,5));

        for (int i=0; i<100; i++) blasterParticle.updatePosition();

        assertTrue(blasterParticle.isHit());

        blasterParticle.setPositionX(2);
        blasterParticle.setPositionY(8);
        assertFalse(blasterParticle.isHit());
    }
}