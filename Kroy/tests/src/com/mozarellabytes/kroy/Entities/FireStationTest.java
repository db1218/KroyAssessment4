package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.GdxTestRunner;
import com.mozarellabytes.kroy.Screens.GameScreen;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(GdxTestRunner.class)
public class FireStationTest {

    @Mock
    GameScreen gameScreenMock;

    private FireStation station;

    @Before
    public void setUp() {
        initMocks(this);
        station = new FireStation(10, 10, 100);
        station.setGameScreen(gameScreenMock);
    }

    @Test
    public void repairPassTest() {
        station.spawn(new FireTruck(gameScreenMock, new Vector2(11, 10), FireTruckType.RubyHard));
        station.getTruck(0).fortressDamage(50);
        float HPBeforeRepair = station.getTruck(0).getHP();
        station.restoreTrucks();
        float HPAfterRepair = station.getTruck(0).getHP();
        assertTrue(HPAfterRepair > HPBeforeRepair);
    }

    @Test
    public void repairIncorrectPositionTest() {
        station.spawn(new FireTruck(gameScreenMock, new Vector2(20, 10), FireTruckType.RubyHard));
        station.getTruck(0).fortressDamage(50);
        float HPBeforeRepair = station.getTruck(0).getHP();
        station.restoreTrucks();
        float HPAfterRepair = station.getTruck(0).getHP();
        assertFalse(HPAfterRepair > HPBeforeRepair);
    }

    @Test
    public void repairAlreadyFullyRepairedTest() {
        station.spawn(new FireTruck(gameScreenMock, new Vector2(11, 10), FireTruckType.RubyHard));
        float HPBeforeRepair = station.getTruck(0).getHP();
        station.restoreTrucks();
        float HPAfterRepair = station.getTruck(0).getHP();
        assertFalse(HPAfterRepair > HPBeforeRepair);
    }

    @Test
    public void repairFireStationDestroyedTest() {
        station.setHP(0);
        station.spawn(new FireTruck(gameScreenMock, new Vector2(11, 10), FireTruckType.RubyHard));
        station.getTruck(0).setHP(50);
        float HPBeforeRepair = station.getTruck(0).getHP();
        station.restoreTrucks();
        float HPAfterRepair = station.getTruck(0).getHP();
        assertEquals(HPAfterRepair, HPBeforeRepair, 0.0);
    }

    @Test
    public void refillPassTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        station.spawn(new FireTruck(gameScreenMock, new Vector2(11, 10), FireTruckType.RubyHard));
        station.getTruck(0).attack(fortress);
        float HPBeforeRefill = station.getTruck(0).getReserve();
        station.restoreTrucks();
        float HPAfterRefill = station.getTruck(0).getReserve();
        assertTrue(HPAfterRefill > HPBeforeRefill);
    }

    @Test
    public void refillIncorrectPositionTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        station.spawn(new FireTruck(gameScreenMock, new Vector2(20, 10), FireTruckType.RubyHard));
        station.getTruck(0).attack(fortress);
        float HPBeforeRefill = station.getTruck(0).getReserve();
        station.restoreTrucks();
        float HPAfterRefill = station.getTruck(0).getReserve();
        assertFalse(HPAfterRefill > HPBeforeRefill);
    }

    @Test
    public void refillAlreadyFullTest() {
        station.spawn(new FireTruck(gameScreenMock, new Vector2(11, 10), FireTruckType.RubyHard));
        float HPBeforeRefill = station.getTruck(0).getReserve();
        station.restoreTrucks();
        float HPAfterRefill = station.getTruck(0).getReserve();
        assertFalse(HPAfterRefill > HPBeforeRefill);
    }

    @Test
    public void refillFireStationDestroyedTest() {
        station.setHP(0);
        station.spawn(new FireTruck(gameScreenMock, new Vector2(11, 10), FireTruckType.RubyHard));
        station.getTruck(0).setReserve(50);
        float ReserveBeforeRefill = station.getTruck(0).getReserve();
        station.restoreTrucks();
        float ReserveAfterRefill = station.getTruck(0).getReserve();
        assertEquals(ReserveAfterRefill, ReserveBeforeRefill, 0.0);
    }

    @Test
    public void trucksCannotOccupySameTileTest() {
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,11);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,12);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,13);
        when(gameScreenMock.getStation()).thenReturn(station);

        FireTruck fireTruck1 = new FireTruck(gameScreenMock, new Vector2(11, 11), FireTruckType.RubyHard);
        FireTruck fireTruck2 = new FireTruck(gameScreenMock, new Vector2(11, 13), FireTruckType.SapphireHard);

        station.spawn(fireTruck1);
        station.spawn(fireTruck2);

        fireTruck1.setMoving(true);
        fireTruck1.addTileToPathSegment(new Vector2(11, 11));
        fireTruck1.addTileToPathSegment(new Vector2(11, 12));
        fireTruck1.addTileToPathSegment(new Vector2(11, 13));

        fireTruck1.addPathSegmentToRoute();
        fireTruck1.generatePathFromLastSegments();

        for (int i=0; i<100; i++) {
            station.checkForCollisions();
            fireTruck1.move();
        }
        Vector2 expectedPosition = new Vector2(11, 12);

        assertEquals(expectedPosition, fireTruck1.getPosition());
    }

    @Test
    public void trucksHaveSameLastTileTest() {
        FireTruck activeFireTruck = new FireTruck(gameScreenMock, new Vector2(10,10), FireTruckType.RubyEasy);

        when(gameScreenMock.isRoad(anyInt(), anyInt())).thenReturn(true);
        when(gameScreenMock.getSelectedTruck()).thenReturn(activeFireTruck);
        when(gameScreenMock.getStation()).thenReturn(station);

        activeFireTruck.addTileToPathSegment(new Vector2(10, 10));
        activeFireTruck.addTileToPathSegment(new Vector2(10, 11));
        activeFireTruck.addTileToPathSegment(new Vector2(10, 12));
        activeFireTruck.addTileToPathSegment(new Vector2(10, 13));

        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10,15), FireTruckType.SapphireHard);
        fireTruck.addTileToPathSegment(new Vector2(10, 15));
        fireTruck.addTileToPathSegment(new Vector2(10, 14));
        fireTruck.addTileToPathSegment(new Vector2(10, 13));
        fireTruck.addPathSegmentToRoute();

        station.spawn(fireTruck);

        assertTrue(station.doTrucksHaveSameLastTile());
    }

    @Test
    public void trucksDontHaveSameLastTileTest() {
        FireTruck activeFireTruck = new FireTruck(gameScreenMock, new Vector2(10,10), FireTruckType.RubyEasy);

        when(gameScreenMock.isRoad(anyInt(), anyInt())).thenReturn(true);
        when(gameScreenMock.getSelectedTruck()).thenReturn(activeFireTruck);
        when(gameScreenMock.getStation()).thenReturn(station);

        activeFireTruck.addTileToPathSegment(new Vector2(10, 10));
        activeFireTruck.addTileToPathSegment(new Vector2(10, 11));
        activeFireTruck.addTileToPathSegment(new Vector2(10, 12));

        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10,15), FireTruckType.SapphireHard);
        fireTruck.addTileToPathSegment(new Vector2(10, 15));
        fireTruck.addTileToPathSegment(new Vector2(10, 14));
        fireTruck.addTileToPathSegment(new Vector2(10, 13));
        fireTruck.addPathSegmentToRoute();

        station.spawn(fireTruck);

        assertFalse(station.doTrucksHaveSameLastTile());
    }

    @Test
    public void trucksDontHaveSameLastTileAsNoTrucksTest() {
        FireTruck activeFireTruck = new FireTruck(gameScreenMock, new Vector2(10,10), FireTruckType.RubyEasy);

        when(gameScreenMock.isRoad(anyInt(), anyInt())).thenReturn(true);
        when(gameScreenMock.getSelectedTruck()).thenReturn(activeFireTruck);
        when(gameScreenMock.getStation()).thenReturn(station);

        activeFireTruck.addTileToPathSegment(new Vector2(10, 10));
        activeFireTruck.addTileToPathSegment(new Vector2(10, 11));
        activeFireTruck.addTileToPathSegment(new Vector2(10, 12));

        assertFalse(station.doTrucksHaveSameLastTile());
    }

    @Test
    public void trucksShouldNotMovePastEachOtherTest() {
        when(gameScreenMock.isRoad(11,11)).thenReturn(true);
        when(gameScreenMock.isRoad(11,12)).thenReturn(true);
        when(gameScreenMock.isRoad(11,13)).thenReturn(true);
        when(gameScreenMock.isRoad(11,14)).thenReturn(true);
        when(gameScreenMock.getStation()).thenReturn(station);

        FireTruck fireTruck1 = new FireTruck(gameScreenMock, new Vector2(11, 11), FireTruckType.RubyHard);
        FireTruck fireTruck2 = new FireTruck(gameScreenMock, new Vector2(11, 14), FireTruckType.SapphireHard);

        station.spawn(fireTruck1);
        station.spawn(fireTruck2);

        fireTruck1.setMoving(true);
        fireTruck1.addTileToPathSegment(new Vector2(11, 11));
        fireTruck1.addTileToPathSegment(new Vector2(11, 12));
        fireTruck1.addTileToPathSegment(new Vector2(11, 13));
        fireTruck1.addTileToPathSegment(new Vector2(11, 14));

        fireTruck2.setMoving(true);
        fireTruck2.addTileToPathSegment(new Vector2(11, 14));
        fireTruck2.addTileToPathSegment(new Vector2(11, 13));
        fireTruck2.addTileToPathSegment(new Vector2(11, 12));
        fireTruck2.addTileToPathSegment(new Vector2(11, 11));
        for (int i=0; i<100; i++) {
            station.checkForCollisions();
            fireTruck1.move();
            fireTruck2.move();
        }
        Vector2 fireTruck1TargetPosition = new Vector2(11, 14);
        Vector2 fireTruck2TargetPosition = new Vector2(11, 11);
        assertTrue(!fireTruck1.getPosition().equals(fireTruck1TargetPosition) && !fireTruck2.getPosition().equals(fireTruck2TargetPosition));
    }

    @Test
    public void recalculateTruckPathsTest() {
        FireTruck activeFireTruck = new FireTruck(gameScreenMock, new Vector2(10,10), FireTruckType.RubyEasy);

        when(gameScreenMock.isRoad(anyInt(), anyInt())).thenReturn(true);
        when(gameScreenMock.getSelectedTruck()).thenReturn(activeFireTruck);
        when(gameScreenMock.getStation()).thenReturn(station);

        activeFireTruck.addTileToPathSegment(new Vector2(10, 10));
        activeFireTruck.addTileToPathSegment(new Vector2(10, 11));
        activeFireTruck.addTileToPathSegment(new Vector2(10, 12));
        activeFireTruck.addTileToPathSegment(new Vector2(10, 13));

        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10,15), FireTruckType.SapphireHard);
        fireTruck.addTileToPathSegment(new Vector2(10, 15));
        fireTruck.addTileToPathSegment(new Vector2(10, 14));
        fireTruck.addTileToPathSegment(new Vector2(10, 13));
        fireTruck.addPathSegmentToRoute();

        station.spawn(fireTruck);

        fireTruck.generatePathFromAllSegments();
        Queue<Vector2> pathBefore = fireTruck.cloneQueue(fireTruck.path);

        station.recalculateTruckPaths();

        Queue<Vector2> pathAfter = fireTruck.path;

        assertEquals(pathBefore, pathAfter);
    }

    @Test
    public void testGetCentrePosition1() {
        FireStation station = new FireStation(10, 10, 100);
        assertEquals(new Vector2(13, 11.5f), station.getCentrePosition());
    }

    @Test
    public void testGetCentrePosition2() {
        FireStation station = new FireStation(0, 0, 100);
        station.setDimensions(10, 10);
        assertEquals(new Vector2(5, 5), station.getCentrePosition());
    }

    @Test
    public void testIsAlive() {
        station.setHP(5);
        assertTrue(station.isAlive());
    }

    @Test
    public void testIsNotAlive() {
        station.setHP(0);
        assertFalse(station.isAlive());
    }

    @Test
    public void testIsDefinatelyNotAlive() {
        station.setHP(-5);
        assertFalse(station.isAlive());
    }

    @Test
    public void testDestroyTruck() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10,15), FireTruckType.SapphireHard);
        station.spawn(fireTruck);
        station.destroyTruck(fireTruck);
        assertTrue(station.getTrucks().isEmpty());
    }
}