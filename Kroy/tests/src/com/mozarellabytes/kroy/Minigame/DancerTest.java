package com.mozarellabytes.kroy.Minigame;

import com.mozarellabytes.kroy.GdxTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class DancerTest {

    private Dancer dancer;

    @Test
    public void testCorrectHealth() {
        dancer = new Dancer(100);
        assertEquals(100, dancer.getHealth());
        dancer = new Dancer(20);
        assertEquals(20, dancer.getHealth());
        dancer = new Dancer(0);
        assertEquals(0, dancer.getHealth());
        dancer = new Dancer(-50);
        assertEquals(-50, dancer.getHealth());
    }

    @Test
    public void testDamage() {
        dancer = new Dancer(100);
        assertFalse(dancer.damage(10));
        assertEquals(90, dancer.getHealth());
        assertTrue(dancer.damage(100));
        assertEquals(-10, dancer.getHealth());
    }

    @Test
    public void testJiveFirstMove() {
        dancer = new Dancer(100);
        dancer.startJive();
        dancer.updateJive();
        assertEquals(DanceMove.NONE, dancer.getState());
    }

    @Test
    public void testJiveSecondMove() {
        dancer = new Dancer(100);
        dancer.startJive();
        dancer.updateJive();
        dancer.updateJive();
        assertEquals(DanceMove.RIGHT, dancer.getState());
    }

    @Test
    public void testJiveThirdMove() {
        dancer = new Dancer(100);
        dancer.startJive();
        dancer.updateJive();
        dancer.updateJive();
        dancer.updateJive();
        assertEquals(DanceMove.LEFT, dancer.getState());
    }

    @Test
    public void testJiveForthMove() {
        dancer = new Dancer(100);
        dancer.startJive();
        dancer.updateJive();
        dancer.updateJive();
        dancer.updateJive();
        dancer.updateJive();
        assertEquals(DanceMove.NONE, dancer.getState());
    }

    @Test
    public void testJiveCannotStart() {
        dancer = new Dancer(100);
        dancer.updateJive();
        dancer.updateJive();
        assertEquals(dancer.getState(), DanceMove.NONE);
    }

    @Test
    public void testJiveNotStopped() {
        dancer = new Dancer(100);
        dancer.startJive();
        dancer.updateJive();
        dancer.updateJive();
        dancer.updateJive();
        dancer.updateJive();
        dancer.updateJive();
        dancer.updateJive();
        dancer.updateJive();
        assertTrue(dancer.isJiving());
    }

    @Test
    public void testJiveStopped() {
        dancer = new Dancer(100);
        dancer.startJive();
        dancer.updateJive();
        dancer.updateJive();
        dancer.updateJive();
        dancer.updateJive();
        dancer.updateJive();
        dancer.updateJive();
        dancer.updateJive();
        dancer.updateJive();
        assertFalse(dancer.isJiving());
    }
}
