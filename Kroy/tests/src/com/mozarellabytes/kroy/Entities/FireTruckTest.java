package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.GdxTestRunner;
import com.mozarellabytes.kroy.Screens.GameScreen;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

import static com.mozarellabytes.kroy.Entities.FireTruckType.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(GdxTestRunner.class)
public class FireTruckTest {

    @Mock
    GameScreen gameScreenMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private FireTruck fireTruck;

    @Before
    public void setUp() {
        fireTruck = new FireTruck(gameScreenMock, new Vector2(10,10), RubyHard);
    }

    @Test
    public void differentSpeedTest() {
        assertNotEquals(SapphireHard.getSpeed(), RubyHard.getSpeed());
    }

    @Test
    public void speedTruckShouldMove3TilesIn25FramesTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), RubyHard);
        when(gameScreenMock.isRoad(anyInt(), anyInt())).thenReturn(true);

        fireTruck.setMoving(true);
        fireTruck.addTileToPathSegment(new Vector2(10,10));
        fireTruck.addTileToPathSegment(new Vector2(10,11));
        fireTruck.addTileToPathSegment(new Vector2(11,11));

        fireTruck.addPathSegmentToRoute();
        fireTruck.generatePathFromLastSegments();

        for (int i=0; i<25; i++) {
            fireTruck.move();
        }
        Vector2 expectedPosition = new Vector2(11, 11);
        assertEquals(expectedPosition, fireTruck.getPosition());
    }

    @Test
    public void oceanTruckShouldNotMove3TilesIn25FramesTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), SapphireHard);
        when(gameScreenMock.isRoad(anyInt(), anyInt())).thenReturn(true);

        fireTruck.setMoving(true);
        fireTruck.addTileToPathSegment(new Vector2(10,10));
        fireTruck.addTileToPathSegment(new Vector2(10,11));
        fireTruck.addTileToPathSegment(new Vector2(11,11));

        fireTruck.addPathSegmentToRoute();
        fireTruck.generatePathFromLastSegments();

        for (int i=0; i<25; i++) {
            fireTruck.move();
        }
        Vector2 expectedPosition = new Vector2(11, 11);
        assertNotEquals(expectedPosition, fireTruck.getPosition());
    }

    @Test
    public void oceanTruckShouldMove3TilesIn70FramesTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), SapphireHard);
        when(gameScreenMock.isRoad(anyInt(), anyInt())).thenReturn(true);

        fireTruck.setMoving(true);
        fireTruck.addTileToPathSegment(new Vector2(10,10));
        fireTruck.addTileToPathSegment(new Vector2(10,11));
        fireTruck.addTileToPathSegment(new Vector2(11,11));

        fireTruck.addPathSegmentToRoute();
        fireTruck.generatePathFromLastSegments();

        for (int i=0; i<70; i++) {
            fireTruck.move();
        }
        Vector2 expectedPosition = new Vector2(11, 11);
        assertEquals(expectedPosition, fireTruck.getPosition());
    }

    @Test
    public void differentMaxVolumeTest() {
        assertNotEquals(SapphireHard.getMaxReserve(), RubyHard.getMaxReserve());
    }

    @Test
    public void differentAPTest() {
        assertNotEquals(SapphireHard.getAP(), RubyHard.getAP());
    }

    @Test
    public void checkTrucksFillToDifferentLevels() {
        FireTruck fireTruck2 = new FireTruck(gameScreenMock, new Vector2(10,10), SapphireHard);
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        FireStation fireStation = new FireStation(8, 10, 100);
        fireStation.spawn(fireTruck);
        fireStation.spawn(fireTruck2);
        for (int i=0; i<2000; i++) {
            fireTruck.attack(fortress);
            fireTruck.updateSpray();
            fireTruck2.attack(fortress);
            fireTruck2.updateSpray();
        }
        float fireTruck1ReserveEmpty = fireTruck.getReserve();
        float fireTruck2ReserveEmpty = fireTruck2.getReserve();

        for (int i=0; i<170; i++) {
            fireStation.restoreTrucks();
        }

        assertEquals(fireTruck1ReserveEmpty, fireTruck2ReserveEmpty, 0.0);
        assertEquals(fireTruck.getReserve(), RubyHard.getMaxReserve(), 0.0);
        assertNotEquals(fireTruck2.getReserve(), SapphireHard.getMaxReserve(), 0.0);
    }

    @Test
    public void differentMaxHPTest() {
        assertNotEquals(RubyHard.getMaxHP(), SapphireHard.getMaxHP());
    }

    @Test
    public void checkTrucksRepairToDifferentLevels() {
        FireTruck fireTruck1 = new FireTruck(gameScreenMock, new Vector2(9,10), RubyHard);
        FireTruck fireTruck2 = new FireTruck(gameScreenMock, new Vector2(10,10), SapphireHard);
        FireStation fireStation = new FireStation(8, 10, 100);

        fireStation.spawn(fireTruck1);
        fireStation.spawn(fireTruck2);

        fireTruck1.repair(RubyHard.getMaxHP()*-1);
        fireTruck2.repair(SapphireHard.getMaxHP()*-1);

        float fireTruck1Health0 = fireTruck1.getHP();
        float fireTruck2Health0 = fireTruck2.getHP();

        for (int i=0; i<310; i++) {
            fireStation.restoreTrucks();
        }

        assertEquals(fireTruck1Health0, fireTruck2Health0, 0.0);
        assertEquals(fireTruck2.getHP(), SapphireHard.getMaxHP(), 0.0);
        assertNotEquals(fireTruck1.getHP(), RubyHard.getMaxHP(), 0.0);
    }

    @Test
    public void differentRangeTest() {
        assertNotEquals(SapphireHard.getRange(), RubyHard.getRange());
    }

    @Test
    public void checkDifferentRangeTest() {
        Fortress fortress = new Fortress(15, 10, FortressType.Clifford);
        FireTruck fireTruck1 = new FireTruck(gameScreenMock, new Vector2(10,10), SapphireEasy);
        FireTruck fireTruck2 = new FireTruck(gameScreenMock, new Vector2(10,10), EmeraldEasy);

        assertNotEquals(fireTruck1.fortressInRange(fortress.getPosition()), fireTruck2.fortressInRange(fortress.getPosition()));
    }

    @Test
    public void truckShouldDecreaseHealthOfFortress() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        float healthBefore = fortress.getHP();
        fireTruck.attack(fortress);

        for (int i=0; i<200; i++) fireTruck.updateSpray();

        float healthAfter = fortress.getHP();
        assertTrue(healthBefore > healthAfter);
    }

    @Test
    public void truckShouldDecreaseReserveWhenAttackingFortress() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        float reserveBefore = fireTruck.getReserve();
        fireTruck.attack(fortress);

        for (int i=0; i<100; i++) fireTruck.updateSpray();

        float reserveAfter = fireTruck.getReserve();
        assertTrue(reserveBefore > reserveAfter);
    }

    @Test
    public void damageFortressWithSpeedByDamageTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        fireTruck.attack(fortress);

        for (int i=0; i<200; i++) fireTruck.updateSpray();

        float fortressHealthAfter = fortress.getHP();
        assertEquals(FortressType.Walmgate.getMaxHP() - RubyHard.getAP(), fortressHealthAfter, 0.0);
    }

    @Test
    public void damageFortressWithSpeedByReserveTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        fireTruck.attack(fortress);

        for (int i=0; i<100; i++) fireTruck.updateSpray();

        float fireTruckReserveAfter = fireTruck.getReserve();
        assertEquals(RubyHard.getMaxReserve() - RubyHard.getAP(), fireTruckReserveAfter, 0.0);
    }

    @Test
    public void damageFortressWithOceanByDamageTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), SapphireHard);
        fireTruck.attack(fortress);

        for (int i=0; i<200; i++) fireTruck.updateSpray();

        float fortressHealthAfter = fortress.getHP();
        assertEquals(FortressType.Walmgate.getMaxHP() - SapphireHard.getAP(), fortressHealthAfter, 0.0);
    }

    @Test
    public void damageFortressWithOceanByReserveTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), SapphireHard);
        fireTruck.attack(fortress);
        for (int i=0; i<100; i++) {
            fireTruck.updateSpray();
        }
        float fireTruckReserveAfter = fireTruck.getReserve();
        assertEquals(SapphireHard.getMaxReserve() - SapphireHard.getAP(), fireTruckReserveAfter, 0.0);
    }

    @Test
    public void moveTest() {
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,10);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,11);
        fireTruck.setMoving(true);
        fireTruck.addTileToPathSegment(new Vector2(10,10));
        fireTruck.addTileToPathSegment(new Vector2(10,11));

        fireTruck.addPathSegmentToRoute();
        fireTruck.generatePathFromLastSegments();

        for (int i=0; i<50; i++) {
            fireTruck.move();
        }
        Vector2 expectedPosition = new Vector2(10, 11);
        assertEquals(expectedPosition, fireTruck.getPosition());
    }

    @Test
    public void cloneQueueTest() {
        Queue<Vector2> queue = new Queue<>();
        queue.addLast(new Vector2(0, 0));
        queue.addLast(new Vector2(1, 2));
        queue.addLast(new Vector2(2, 2));
        queue.addLast(new Vector2(3, 3));

        assertEquals(queue, fireTruck.cloneQueue(queue));
    }

    @Test
    public void undoOneSegmentTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), SapphireHard);
        when(gameScreenMock.isRoad(anyInt(), anyInt())).thenReturn(true);
        when(gameScreenMock.getState()).thenReturn(GameScreen.PlayState.FREEZE);

        fireTruck.addTileToPathSegment(new Vector2(10,10));
        fireTruck.addTileToPathSegment(new Vector2(10,11));
        fireTruck.addTileToPathSegment(new Vector2(11,11));

        fireTruck.addPathSegmentToRoute();

        fireTruck.undoSegment();

        Queue<Queue<Vector2>> pathSegmentsAfterUndo = clone2DQueue(fireTruck.pathSegments);

        Queue<Queue<Vector2>> expectedPathSegments = new Queue<>();
        Queue<Vector2> expectedPathSegment = new Queue<>();
        expectedPathSegment.addLast(new Vector2(10, 10));
        expectedPathSegments.addLast(expectedPathSegment);

        assertEquals(expectedPathSegments, pathSegmentsAfterUndo);
    }

    @Test
    public void undoTwoSegmentsTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), SapphireHard);
        when(gameScreenMock.isRoad(anyInt(), anyInt())).thenReturn(true);
        when(gameScreenMock.getState()).thenReturn(GameScreen.PlayState.FREEZE);

        fireTruck.addTileToPathSegment(new Vector2(10,10));
        fireTruck.addTileToPathSegment(new Vector2(10,11));
        fireTruck.addTileToPathSegment(new Vector2(11,11));

        fireTruck.addPathSegmentToRoute();

        Queue<Queue<Vector2>> pathSegmentsOneSegment = clone2DQueue(fireTruck.pathSegments);

        fireTruck.addTileToPathSegment(new Vector2(11,11));
        fireTruck.addTileToPathSegment(new Vector2(12,11));
        fireTruck.addTileToPathSegment(new Vector2(13,11));

        fireTruck.addPathSegmentToRoute();

        Queue<Queue<Vector2>> pathSegmentsTwoSegments = clone2DQueue(fireTruck.pathSegments);

        fireTruck.undoSegment();

        Queue<Queue<Vector2>> pathSegmentsAfterUndo = clone2DQueue(fireTruck.pathSegments);

        assertTrue(pathSegmentsTwoSegments.size > pathSegmentsAfterUndo.size);
        assertEquals(pathSegmentsOneSegment, pathSegmentsAfterUndo);
    }

    @Test
    public void redoOneSegmentTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), SapphireHard);
        when(gameScreenMock.isRoad(anyInt(), anyInt())).thenReturn(true);
        when(gameScreenMock.getState()).thenReturn(GameScreen.PlayState.FREEZE);

        fireTruck.addTileToPathSegment(new Vector2(10,10));
        fireTruck.addTileToPathSegment(new Vector2(10,11));
        fireTruck.addTileToPathSegment(new Vector2(11,11));
        fireTruck.addPathSegmentToRoute();

        Queue<Queue<Vector2>> pathSegmentsOneSegment = clone2DQueue(fireTruck.pathSegments);

        fireTruck.undoSegment();

        fireTruck.redoSegment();
        Queue<Queue<Vector2>> pathSegmentsAfterRedo = clone2DQueue(fireTruck.pathSegments);

        assertEquals(pathSegmentsOneSegment, pathSegmentsAfterRedo);
    }

    @Test
    public void redoTwoSegmentsTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), SapphireHard);
        when(gameScreenMock.isRoad(anyInt(), anyInt())).thenReturn(true);
        when(gameScreenMock.getState()).thenReturn(GameScreen.PlayState.FREEZE);

        fireTruck.addTileToPathSegment(new Vector2(10,10));
        fireTruck.addTileToPathSegment(new Vector2(10,11));
        fireTruck.addTileToPathSegment(new Vector2(11,11));
        fireTruck.addPathSegmentToRoute();

        Queue<Queue<Vector2>> pathSegmentsOneSegment = clone2DQueue(fireTruck.pathSegments);

        fireTruck.addTileToPathSegment(new Vector2(11,11));
        fireTruck.addTileToPathSegment(new Vector2(12,11));
        fireTruck.addTileToPathSegment(new Vector2(13,11));
        fireTruck.addPathSegmentToRoute();

        Queue<Queue<Vector2>> pathSegmentsTwoSegments = clone2DQueue(fireTruck.pathSegments);

        fireTruck.undoSegment();
        Queue<Queue<Vector2>> pathSegmentsAfterUndo = clone2DQueue(fireTruck.pathSegments);

        fireTruck.redoSegment();
        Queue<Queue<Vector2>> pathSegmentsAfterRedo = clone2DQueue(fireTruck.pathSegments);

        assertEquals(pathSegmentsOneSegment, pathSegmentsAfterUndo);
        assertEquals(pathSegmentsTwoSegments, pathSegmentsAfterRedo);
    }

    @Test
    public void resetPathTests() {
        Queue<Vector2> path = new Queue<>();
        path.addFirst(new Vector2(0, 0));
        path.addFirst(new Vector2(1, 1));
        path.addFirst(new Vector2(2, 3));

        fireTruck.path = path;

        Queue<Vector2> pathSegment = new Queue<>();
        pathSegment.addFirst(new Vector2(0, 0));
        pathSegment.addFirst(new Vector2(1, 1));
        pathSegment.addFirst(new Vector2(2, 3));

        fireTruck.pathSegment = pathSegment;

        fireTruck.resetPath();

        assertTrue(fireTruck.path.isEmpty());
        assertTrue(fireTruck.pathSegment.isEmpty());
    }

    @Test
    public void pathSegmentCanBeAddedToRouteTest() {
        Queue<Vector2> pathSegment = new Queue<>();
        pathSegment.addFirst(new Vector2(0, 0));
        pathSegment.addFirst(new Vector2(1, 1));
        pathSegment.addFirst(new Vector2(2, 3));
        fireTruck.pathSegment = pathSegment;

        assertTrue(fireTruck.canPathSegmentBeAddedToRoute());
    }

    @Test
    public void pathSegmentCannotBeAddedToRouteTest() {
        Queue<Vector2> pathSegment = new Queue<>();
        pathSegment.addFirst(new Vector2(0, 0));
        fireTruck.pathSegment = pathSegment;

        assertFalse(fireTruck.canPathSegmentBeAddedToRoute());
    }

    @Test
    public void isOnBayTileTest() {
        ArrayList<Vector2> bayTiles = new ArrayList<>();
        bayTiles.add(new Vector2(0, 0));
        bayTiles.add(new Vector2(1, 0));

        FireStation stationMock = mock(FireStation.class);
        when(stationMock.getBayTiles()).thenReturn(bayTiles);

        fireTruck.setPosition(new Vector2(0, 0));

        assertTrue(fireTruck.isOnBayTile(stationMock));
    }

    @Test
    public void isNotOnBayTileTest() {
        ArrayList<Vector2> bayTiles = new ArrayList<>();
        bayTiles.add(new Vector2(0, 0));
        bayTiles.add(new Vector2(1, 0));

        FireStation stationMock = mock(FireStation.class);
        when(stationMock.getBayTiles()).thenReturn(bayTiles);

        fireTruck.setPosition(new Vector2(2, 0));

        assertFalse(fireTruck.isOnBayTile(stationMock));
    }

    @Ignore
    public Queue<Queue<Vector2>> clone2DQueue(Queue<Queue<Vector2>> queue2D) {
        Queue<Queue<Vector2>> newQueue = new Queue<>();
        for (Queue<Vector2> queue1D : queue2D) newQueue.addLast(fireTruck.cloneQueue(queue1D));
        return newQueue;
    }

}