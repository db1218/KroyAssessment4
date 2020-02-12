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
        assertEquals(false, dancer.damage(10));
        assertEquals(90, dancer.getHealth());
        assertEquals(true, dancer.damage(100));
        assertEquals(-10, dancer.getHealth());

        // Negative damage shouldn't happen
        dancer.damage(-100);
        assertEquals(-10, dancer.getHealth());
    }

    @Test
    public void testState() {
        dancer = new Dancer(100);
        assertEquals(DanceMove.NONE, dancer.getState());
        dancer.setState(DanceMove.UP);
        assertEquals(DanceMove.UP, dancer.getState());
        dancer.setState(DanceMove.DOWN);
        assertEquals(DanceMove.DOWN, dancer.getState());
        dancer.setState(DanceMove.LEFT);
        assertEquals(DanceMove.LEFT, dancer.getState());
        dancer.setState(DanceMove.RIGHT);
        assertEquals(DanceMove.RIGHT, dancer.getState());

    }
}
