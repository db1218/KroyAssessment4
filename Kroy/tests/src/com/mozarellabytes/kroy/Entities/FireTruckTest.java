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
import static com.mozarellabytes.kroy.Entities.FireTruckType.*;
import static org.junit.Assert.*;

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
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,10);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,11);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,11);
        fireTruck.setMoving(true);
        fireTruck.addTileToPathSegment(new Vector2(10,10));
        fireTruck.addTileToPathSegment(new Vector2(10,11));
        fireTruck.addTileToPathSegment(new Vector2(11,11));
        for (int i=0; i<25; i++) {
            fireTruck.move();
        }
        Vector2 expectedPosition = new Vector2(11, 11);
        assertEquals(expectedPosition, fireTruck.getPosition());
    }

    @Test
    public void oceanTruckShouldNotMove3TilesIn25FramesTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), SapphireHard);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,10);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,11);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,11);
        fireTruck.setMoving(true);
        fireTruck.addTileToPathSegment(new Vector2(10,10));
        fireTruck.addTileToPathSegment(new Vector2(10,11));
        fireTruck.addTileToPathSegment(new Vector2(11,11));
        for (int i=0; i<25; i++) {
            fireTruck.move();
        }
        Vector2 expectedPosition = new Vector2(11, 11);
        assertNotEquals(expectedPosition, fireTruck.getPosition());
    }

    @Test
    public void oceanTruckShouldMove3TilesIn50FramesTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), SapphireHard);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,10);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(10,11);
        Mockito.doReturn(true).when(gameScreenMock).isRoad(11,11);
        fireTruck.setMoving(true);
        fireTruck.addTileToPathSegment(new Vector2(10,10));
        fireTruck.addTileToPathSegment(new Vector2(10,11));
        fireTruck.addTileToPathSegment(new Vector2(11,11));
        for (int i=0; i<50; i++) {
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

        for (int i=0; i<2000; i++) {
            fireStation.restoreTrucks();
        }

        boolean checkEmptyReservesAreSame = fireTruck1ReserveEmpty == fireTruck2ReserveEmpty;
        boolean checkSpeedTruckIsFull = fireTruck.getReserve() == RubyHard.getMaxReserve();
        boolean checkOceanTruckIsNotFull = fireTruck2.getReserve() !=  SapphireHard.getMaxReserve();

        assertTrue(checkEmptyReservesAreSame && checkSpeedTruckIsFull && checkOceanTruckIsNotFull);

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

        for (int i=0; i<3000; i++) {
            fireStation.restoreTrucks();
        }

        boolean checkHealth0IsSame = fireTruck1Health0 == fireTruck2Health0;
        boolean checkOceanTruckIsFullyRepaired = fireTruck2.getHP() == SapphireHard.getMaxHP();
        boolean checkSpeedTruckIsNotFullyRepaired = fireTruck1.getHP() !=  RubyHard.getMaxHP();

        assertTrue(checkHealth0IsSame && checkOceanTruckIsFullyRepaired && checkSpeedTruckIsNotFullyRepaired);

    }

    @Test
    public void differentRangeTest() {
        assertNotEquals(SapphireHard.getRange(), RubyHard.getRange());
    }

    @Test
    public void checkDifferentRangeTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Clifford);
        fireTruck.fortressInRange(fortress.getPosition());
        assertNotEquals(fireTruck.fortressInRange(fortress.getPosition()), fireTruck.fortressInRange(fortress.getPosition()));
    }

    @Test
    public void truckShouldDecreaseHealthOfFortress() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        float healthBefore = fortress.getHP();
        fireTruck.attack(fortress);
        for (int i=0; i<200; i++) {
            fireTruck.updateSpray();
        }
        float healthAfter = fortress.getHP();
        assertTrue(healthBefore > healthAfter);
    }

    @Test
    public void truckShouldDecreaseReserveWhenAttackingFortress() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        float reserveBefore = fireTruck.getReserve();
        fireTruck.attack(fortress);
        for (int i=0; i<100; i++) {
            fireTruck.updateSpray();
        }
        float reserveAfter = fireTruck.getReserve();
        assertTrue(reserveBefore > reserveAfter);
    }

    @Test
    public void damageFortressWithSpeedByDamageTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        fireTruck.attack(fortress);
        for (int i=0; i<200; i++) {
            fireTruck.updateSpray();
        }
        float fortressHealthAfter = fortress.getHP();
        assertEquals(FortressType.Walmgate.getMaxHP() - RubyHard.getAP(), fortressHealthAfter, 0.0);
    }

    @Test
    public void damageFortressWithSpeedByReserveTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        fireTruck.attack(fortress);
        for (int i=0; i<100; i++) {
            fireTruck.updateSpray();
        }
        float fireTruckReserveAfter = fireTruck.getReserve();
        assertEquals(RubyHard.getMaxReserve() - RubyHard.getAP(), fireTruckReserveAfter, 0.0);
    }

    @Test
    public void damageFortressWithOceanByDamageTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), SapphireHard);
        fireTruck.attack(fortress);
        for (int i=0; i<200; i++) {
            fireTruck.updateSpray();
        }
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
        for (int i=0; i<50; i++) {
            fireTruck.move();
        }
        Vector2 expectedPosition = new Vector2(10, 11);
        assertEquals(expectedPosition, fireTruck.getPosition());
    }

}