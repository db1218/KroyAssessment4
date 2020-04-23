package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.GdxTestRunner;
import com.mozarellabytes.kroy.PowerUp.*;
import com.mozarellabytes.kroy.Screens.GameScreen;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(GdxTestRunner.class)
public class PowerUpTest {
    Freeze freezePowerUp;
    Heart heartPowerUp;
    Range rangePowerUp;
    Shield shieldPowerUp;
    Water waterPowerUp;

    @Mock
    GameScreen gameScreenMock;

    @Mock
    Fortress fortressMock;

    @Before
    public void setUp() {
        initMocks(this);
        Vector2 location = new Vector2(0,0);
        freezePowerUp = new Freeze(location);
        heartPowerUp = new Heart(location);
        rangePowerUp = new Range(location);
        shieldPowerUp = new Shield(location);
        waterPowerUp = new Water(location);
    }

    @Test
    public void heartTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(0, 0), FireTruckType.AmethystEasy);

        fireTruck.setHP(5);
        float HPBeforePowerUp = fireTruck.getHP();
        heartPowerUp.invokePower(fireTruck);
        float HPAfterPowerUp = fireTruck.getHP();

        assertTrue(HPAfterPowerUp > HPBeforePowerUp);
        assertEquals(FireTruckType.AmethystEasy.getMaxHP(), HPAfterPowerUp, 0.0);
    }

    @Test
    public void rangeTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(0, 0), FireTruckType.AmethystEasy);

        float rangeBeforePowerUp = fireTruck.getRange();
        rangePowerUp.invokePower(fireTruck);
        float rangeAfterPowerUp = fireTruck.getRange();

        assertTrue(rangeAfterPowerUp > rangeBeforePowerUp);
        assertTrue(rangeAfterPowerUp > FireTruckType.AmethystEasy.getRange());
    }

    @Test
    public void shieldTest() {
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(0, 0), FireTruckType.AmethystEasy);

        float HPBeforeBeingHit = fireTruck.getHP();
        if (!fireTruck.inShield()) fireTruck.fortressDamage(10);
        float HPBeforeUsingPowerUp = fireTruck.getHP();

        shieldPowerUp.invokePower(fireTruck);

        if (!fireTruck.inShield()) fireTruck.fortressDamage(10);
        float HPAfterUsingPowerUp = fireTruck.getHP();

        assertTrue(HPBeforeBeingHit > HPBeforeUsingPowerUp);
        assertEquals(HPAfterUsingPowerUp, HPBeforeUsingPowerUp, 0.0);
    }

    @Test
    public void waterTest() {
        when(fortressMock.getPosition()).thenReturn(new Vector2(0,0));
        FireTruck fireTruck = new FireTruck(gameScreenMock, new Vector2(0, 0), FireTruckType.AmethystEasy);

        float waterBeforeAttack = fireTruck.getReserve();

        fireTruck.attack(fortressMock);
        fireTruck.attack(fortressMock);
        fireTruck.attack(fortressMock);
        fireTruck.attack(fortressMock);
        fireTruck.attack(fortressMock);

        float waterBeforePowerUp = fireTruck.getReserve();

        waterPowerUp.invokePower(fireTruck);
        float waterAfterPowerUp = fireTruck.getReserve();

        assertTrue(waterBeforeAttack > waterBeforePowerUp);
        assertEquals(FireTruckType.AmethystEasy.getMaxReserve(), waterAfterPowerUp, 0.0);
    }

    @Test
    public void updateTest() {
        TextureRegion frameBeforeUpdate = freezePowerUp.getCurrentFrame();
        float timeBeforeUpdate = freezePowerUp.getElapsedTime();

        freezePowerUp.update();

        TextureRegion frameAfterUpdate = freezePowerUp.getCurrentFrame();
        float timeAfterUpdate = freezePowerUp.getElapsedTime();

        assertNotEquals(frameBeforeUpdate, frameAfterUpdate);
        assertTrue(timeAfterUpdate >  timeBeforeUpdate);
    }

    @Test
    public void createNewPowerUpsTest() {
        ArrayList<PowerUp> powerUpLocations = PowerUp.createNewPowers(new Vector2(0,0));
        assertEquals(5, powerUpLocations.size());
    }

}
