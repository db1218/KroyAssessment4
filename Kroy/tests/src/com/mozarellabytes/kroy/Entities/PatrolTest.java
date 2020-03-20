package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.GdxTestRunner;
import com.mozarellabytes.kroy.Screens.GameScreen;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(GdxTestRunner.class)
public class PatrolTest {

    Patrol patrol;

    @Mock
    FireTruck truckMock;

    @Mock
    FireStation stationMock;

    @Before
    public void setUp() {
        initMocks(this);
        Queue<Vector2> queue = new Queue<>();
        queue.addLast(new Vector2(0,0));
        queue.addLast(new Vector2(10,0));
        queue.addLast(new Vector2(10,10));
        queue.addLast(new Vector2(0,10));
        patrol = new Patrol("Green", 100, 0, 0, queue, "Green Patrol 1");
    }

    @Test
    public void generatePath() {
        Patrol patrol = new Patrol(PatrolType.Blue, 10, 10, "Blue Patrol 1");
        assertEquals(4, patrol.getPath().size);
    }

    @Test
    public void testCycleQueue() {
        Queue<Vector2> queue = new Queue<>();
        queue.addLast(new Vector2(0,0));
        queue.addLast(new Vector2(10,0));
        Patrol patrol = new Patrol("Green", 100, 0, 0, queue, "Green Patrol 1");
        patrol.cycleQueue();

        Queue<Vector2> expectedQueue = new Queue<>();
        expectedQueue.addLast(new Vector2(10,0));
        expectedQueue.addLast(new Vector2(0,0));

        assertEquals(expectedQueue, patrol.getPath());
    }

    @Test
    public void testMoveRight() {
        Queue<Vector2> queue = new Queue<>();
        queue.addLast(new Vector2(0,0));
        queue.addLast(new Vector2(10,0));
        Patrol patrol = new Patrol("Green", 100, 0, 0, queue, "Green Patrol 1");
        patrol.move(0);
        patrol.move(0.2f);
        assertEquals(new Vector2(0.2f, 0), patrol.getPosition());
    }

    @Test
    public void testMoveLeft() {
        Queue<Vector2> queue = new Queue<>();
        queue.addLast(new Vector2(0,0));
        queue.addLast(new Vector2(-10,0));
        Patrol patrol = new Patrol("Green", 100, 0, 0, queue, "Green Patrol 1");
        patrol.move(0);
        patrol.move(0.2f);
        assertEquals(new Vector2(-0.2f, 0), patrol.getPosition());
    }

    @Test
    public void testMoveUp() {
        Queue<Vector2> queue = new Queue<>();
        queue.addLast(new Vector2(0,0));
        queue.addLast(new Vector2(0,10));
        Patrol patrol = new Patrol("Green", 100, 0, 0, queue, "Green Patrol 1");
        patrol.move(0);
        patrol.move(0.2f);
        assertEquals(new Vector2(0, 0.2f), patrol.getPosition());
    }

    @Test
    public void testMoveDown() {
        Queue<Vector2> queue = new Queue<>();
        queue.addLast(new Vector2(0,0));
        queue.addLast(new Vector2(0,-10));
        Patrol patrol = new Patrol("Green", 100, 0, 0, queue, "Green Patrol 1");
        patrol.move(0);
        patrol.move(0.2f);
        assertEquals(new Vector2(0, -0.2f), patrol.getPosition());
    }

    @Test
    public void testGeneratePath() {
        Patrol patrol = new Patrol(PatrolType.Green);
        patrol.generatePath(10, 10);
        assertEquals(4, patrol.getPath().size);
    }

    @Test
    public void testShouldCollideWithTruckStationDead() {
        when(stationMock.isAlive()).thenReturn(false);
        when(truckMock.isOnBayTile(mock(FireStation.class))).thenReturn(true);
        when(truckMock.getTilePosition()).thenReturn(new Vector2(0,0));
        assertTrue(patrol.collidesWithTruck(truckMock, stationMock));
    }

    @Test
    public void testShouldCollideWithTruckNotOnBayTile() {
        when(stationMock.isAlive()).thenReturn(true);
        when(truckMock.isOnBayTile(stationMock)).thenReturn(false);
        when(truckMock.getTilePosition()).thenReturn(new Vector2(0,0));
        assertTrue(patrol.collidesWithTruck(truckMock, stationMock));
    }

    @Test
    public void testShouldNotCollideWithTruckPositionPatrol() {
        when(stationMock.isAlive()).thenReturn(true);
        when(truckMock.isOnBayTile(mock(FireStation.class))).thenReturn(true);
        when(truckMock.getTilePosition()).thenReturn(new Vector2(0,0));
        patrol.setPosition(1.0f, 0);
        assertFalse(patrol.collidesWithTruck(truckMock, stationMock));
    }

    @Test
    public void testShouldNotCollideWithTruckPositionTruck() {
        when(stationMock.isAlive()).thenReturn(true);
        when(truckMock.isOnBayTile(mock(FireStation.class))).thenReturn(true);
        when(truckMock.getTilePosition()).thenReturn(new Vector2(1.0f,0));
        assertFalse(patrol.collidesWithTruck(truckMock, stationMock));
    }

}
