
package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.GdxTestRunner;
import com.mozarellabytes.kroy.Screens.GameScreen;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;

import static com.mozarellabytes.kroy.Entities.FortressType.*;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class FortressTest {

    @Mock
    GameScreen gameScreenMock;

    @Test
    public void differentRangeTest() {
        assertTrue(Clifford.getRange() != Revs.getRange() &&
                Revs.getRange() != Walmgate.getRange() &&
                Walmgate.getRange() != CentralHall.getRange() &&
                CentralHall.getRange() != Museum.getRange() &&
                Museum.getRange() != Railway.getRange());
    }

    @Test
    public void differentMaxHPTest() {
        assertTrue(Clifford.getMaxHP() != Revs.getMaxHP() &&
                Revs.getMaxHP() != Walmgate.getMaxHP() &&
                Walmgate.getMaxHP() != CentralHall.getMaxHP() &&
                CentralHall.getMaxHP() != Museum.getMaxHP() &&
                Museum.getMaxHP() != Railway.getMaxHP());
    }

    @Test
    public void differentFireRateTest() {
        assertTrue(Clifford.getDelay() != Revs.getDelay() &&
                Revs.getDelay() != Walmgate.getDelay() &&
                Walmgate.getDelay() != CentralHall.getDelay() &&
                CentralHall.getDelay() != Museum.getDelay() &&
                Museum.getDelay() != Railway.getDelay());
    }

    @Test
    public void differentAPTest() {
        assertTrue(Clifford.getAP() != Revs.getAP() &&
                Revs.getAP() != Walmgate.getAP() &&
                Walmgate.getAP() != CentralHall.getAP() &&
                CentralHall.getAP() != Museum.getAP() &&
                Museum.getAP() != Railway.getAP());
    }

    @Test
    public void attackTruckFromWalmgateFortressDamageTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), FireTruckType.RubyHard);
        fireTruck.setTimeOfLastAttack(System.currentTimeMillis() - 5000);
        fortress.attack(fireTruck, false,1);
        fortress.updateBombs();
        assertEquals(140.5f, fireTruck.getHP(), 0.0);
    }

    @Test
    public void attackTruckFromCliffordFortressDamageTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Clifford);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), FireTruckType.RubyHard);
        fireTruck.setTimeOfLastAttack(System.currentTimeMillis() - 5000);
        fortress.attack(fireTruck, false,1);
        fortress.updateBombs();
        assertEquals(142.5, fireTruck.getHP(), 0.0);
    }

    @Test
    public void attackTruckFromRevolutionFortressDamageTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Revs);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), FireTruckType.RubyHard);
        fireTruck.setTimeOfLastAttack(System.currentTimeMillis() - 5000);
        fortress.attack(fireTruck, false,1);
        fortress.updateBombs();
        assertEquals(142.75, fireTruck.getHP(), 0.0);
    }

    @Test
    public void attackTruckFromCentralHallFortressDamageTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.CentralHall);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), FireTruckType.RubyHard);
        fireTruck.setTimeOfLastAttack(System.currentTimeMillis() - 5000);
        fortress.attack(fireTruck, false,1);
        fortress.updateBombs();
        assertEquals(142.25, fireTruck.getHP(), 0.0);
    }

    @Test
    public void attackTruckFromMuseumFortressDamageTest() {
        Fortress fortress = new Fortress(10, 10, Museum);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), FireTruckType.RubyHard);
        fireTruck.setTimeOfLastAttack(System.currentTimeMillis() - 5000);
        fortress.attack(fireTruck, false,1);
        fortress.updateBombs();
        assertEquals(140.0, fireTruck.getHP(), 0.0);
    }

    @Test
    public void attackTruckFromRailwayFortressDamageTest() {
        Fortress fortress = new Fortress(10, 10, Railway);
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(10, 10), FireTruckType.RubyHard);
        fireTruck.setTimeOfLastAttack(System.currentTimeMillis() - 5000);
        fortress.attack(fireTruck, false,1);
        fortress.updateBombs();
        assertEquals(139.0, fireTruck.getHP(), 0.0);
    }

    @Test
    public void attackTruckFromWalmgateFortressBeforeRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate); // range = 8
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(17.9f, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromWalmgateFortressOnRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate); // range = 8
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(18, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromWalmgateFortressAfterRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Walmgate); // range = 8
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(18.1f, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertFalse(withinRange);
    }

    @Test
    public void attackTruckFromCliffordFortressBeforeRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Clifford); // range = 6.5
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(16.4f, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromCliffordFortressOnRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Clifford); // range = 6.5
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(16.5f, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromCliffordFortressAfterRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Clifford); // range = 6.5
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(16.6f, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertFalse(withinRange);
    }

    @Test
    public void attackTruckFromRevolutionFortressBeforeRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Revs); // range = 7
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(16, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromRevolutionFortressOnRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Revs); // range = 7
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(17, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromRevolutionFortressAfterRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Revs); // range = 7
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(18, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertFalse(withinRange);
    }

    @Test
    public void attackTruckFromCentralHallFortressBeforeRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.CentralHall); // range = 7.5
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(17.4f, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromCentralHallFortressOnRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.CentralHall); // range = 7.5
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(17.5f, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromCentralHallFortressAfterRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.CentralHall); // range = 7.5
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(17.6f, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertFalse(withinRange);
    }

    @Test
    public void attackTruckFromMuseumFortressBeforeRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Museum); // range = 5.5
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(15.4f, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromMuseumFortressOnRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Museum); // range = 5.5
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(15.5f, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromMuseumFortressAfterRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Museum); // range = 5.5
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(15.6f, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertFalse(withinRange);
    }

    @Test
    public void attackTruckFromRailwayFortressBeforeRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Railway); // range = 8.5
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(18.4f, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromRailwayFortressOnRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Railway); // range = 8.5
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(18.5f, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertTrue(withinRange);
    }

    @Test
    public void attackTruckFromRailwayFortressAfterRangeBoundaryTest() {
        Fortress fortress = new Fortress(10, 10, FortressType.Railway); // range = 8.5
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(18.6f, 10), FireTruckType.RubyHard);
        boolean withinRange = fortress.withinRange(fireTruck.getPosition());
        assertFalse(withinRange);
    }

    @Test
    public void bombHasReachedTileHitTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(0,0), FireTruckType.AmethystEasy);
        float fireTruckHealthBefore = fireTruck.getHP();
        Fortress fortress = new Fortress(10, 10, FortressType.Revs);
        ArrayList<Bomb> bombs = new ArrayList<>();

        bombs.add(new Bomb(fortress, fireTruck, false, 2.0f));

        fortress.setBombs(bombs);

        for (int i=0; i<20; i++) fortress.updateBombs();

        float fireTruckHealthAfter = fireTruck.getHP();

        assertTrue(fireTruckHealthBefore > fireTruckHealthAfter);
        assertTrue(bombs.isEmpty());
    }

    @Test
    public void bombHasReachedTileMissTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(0,0), FireTruckType.AmethystEasy);
        float fireTruckHealthBefore = fireTruck.getHP();
        Fortress fortress = new Fortress(10, 10, FortressType.Revs);
        ArrayList<Bomb> bombs = new ArrayList<>();

        bombs.add(new Bomb(fortress, fireTruck, false, 2.0f));

        fireTruck.setPosition(new Vector2(15, 5));
        fortress.setBombs(bombs);

        for (int i=0; i<20; i++) fortress.updateBombs();

        float fireTruckHealthAfter = fireTruck.getHP();

        assertEquals(fireTruckHealthBefore, fireTruckHealthAfter, 0.0);
        assertTrue(bombs.isEmpty());
    }

}
