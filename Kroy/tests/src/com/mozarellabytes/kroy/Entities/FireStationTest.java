package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.GdxTestRunner;
import com.mozarellabytes.kroy.Screens.GameScreen;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;


import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class FireStationTest {

    @Mock
    GameScreen gameScreenMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private FireStation station;

    @Before
    public void setUp() {
        station = new FireStation(10, 10, 100);
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
    public void trucksCannotOccupySameTileTest() {
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,11);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,12);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,13);

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
    public void trucksShouldNotMovePastEachOtherTest() {
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,11);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,12);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,13);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,14);

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

}