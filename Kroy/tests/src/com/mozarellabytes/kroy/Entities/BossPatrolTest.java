package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class BossPatrolTest {

    BossPatrol bossPatrol;

    @Before
    public void setUp() {
        bossPatrol = new BossPatrol(PatrolType.Boss, new Vector2(0, 0), new Vector2(10, 10));
    }

    @Test
    public void testGeneratePathFromLowerLeft() {
        Vector2 source = new Vector2(0,0);
        Vector2 target = new Vector2(10,10);

        Queue<Vector2> expectedPath = new Queue<>();
        expectedPath.addLast(new Vector2(0,0));
        expectedPath.addLast(new Vector2(8,8));

        assertEquals(expectedPath, bossPatrol.generatePath(source, target));
    }

    @Test
    public void testGeneratePathFromUpperLeft() {
        Vector2 source = new Vector2(0,20);
        Vector2 target = new Vector2(10,10);

        Queue<Vector2> expectedPath = new Queue<>();
        expectedPath.addLast(new Vector2(0,20));
        expectedPath.addLast(new Vector2(8,12));

        assertEquals(expectedPath, bossPatrol.generatePath(source, target));
    }

    @Test
    public void testGeneratePathFromLowerRight() {
        Vector2 source = new Vector2(20,0);
        Vector2 target = new Vector2(10,10);

        Queue<Vector2> expectedPath = new Queue<>();
        expectedPath.addLast(new Vector2(20,0));
        expectedPath.addLast(new Vector2(12,8));

        assertEquals(expectedPath, bossPatrol.generatePath(source, target));
    }

    @Test
    public void testGeneratePathFromUpperRight() {
        Vector2 source = new Vector2(20,20);
        Vector2 target = new Vector2(10,10);

        Queue<Vector2> expectedPath = new Queue<>();
        expectedPath.addLast(new Vector2(20,20));
        expectedPath.addLast(new Vector2(12,12));

        assertEquals(expectedPath, bossPatrol.generatePath(source, target));
    }

    @Test
    public void testGeneratePathFromOnTarget() {
        Vector2 source = new Vector2(10,10);
        Vector2 target = new Vector2(10,10);

        Queue<Vector2> expectedPath = new Queue<>();
        expectedPath.addLast(new Vector2(10,10));
        expectedPath.addLast(new Vector2(8,8));

        assertEquals(expectedPath, bossPatrol.generatePath(source, target));
    }

    @Test
    public void testIsInShootingPosition() {
        bossPatrol.setPosition(10, 10);
        bossPatrol.setShootingPosition(new Vector2(10, 10));
        assertTrue(bossPatrol.inShootingPosition());
    }

    @Test
    public void testIsNotInShootingPosition() {
        bossPatrol.setPosition(5, 5);
        bossPatrol.setShootingPosition(new Vector2(10, 10));
        assertFalse(bossPatrol.inShootingPosition());
    }

    @Test
    public void testAttack() {
        FireStation station = new FireStation(10, 10, 100);

        ArrayList<Particle> expectedSpray = new ArrayList<>();
        expectedSpray.add(new Particle(new Vector2(0.5f,0), new Vector2(13, 11.5f), station));

        bossPatrol.attack(station);

        assertEquals(expectedSpray.get(0).getPosition().x, bossPatrol.getSpray().get(0).getPosition().x, 0.1);
        assertEquals(expectedSpray.get(0).getPosition().y, bossPatrol.getSpray().get(0).getPosition().y, 0.1);
    }

    @Test
    public void testUpdateBossSprayNotHit() {
        FireStation station = new FireStation(10, 10, 100);
        float HPBeforeBeingAttacked = station.getHP();
        ArrayList<Particle> spray = new ArrayList<>();
        spray.add(new Particle(new Vector2(0,0), station.getPosition(), station));

        bossPatrol.setSpray(spray);

        bossPatrol.updateBossSpray();

        float HPAfterBeingAttacked = station.getHP();

        assertEquals(HPBeforeBeingAttacked, HPAfterBeingAttacked, 0.0);
        assertEquals(0.5f, bossPatrol.getSpray().get(0).getPosition().x, 0.1);
        assertEquals(0.5f, bossPatrol.getSpray().get(0).getPosition().y, 0.1);
    }

    @Test
    public void testUpdateBossSprayHit() {
        FireStation station = new FireStation(10, 10, 100);
        float HPBeforeBeingAttacked = station.getHP();
        ArrayList<Particle> spray = new ArrayList<>();
        spray.add(new Particle(new Vector2(13,11.5f), station.getPosition(), station));

        bossPatrol.setSpray(spray);

        for (int i=0; i<250; i++)
            bossPatrol.updateBossSpray();

        float HPAfterBeingAttacked = station.getHP();

        assertTrue(HPBeforeBeingAttacked > HPAfterBeingAttacked);
        assertTrue(bossPatrol.getSpray().isEmpty());
    }
}
