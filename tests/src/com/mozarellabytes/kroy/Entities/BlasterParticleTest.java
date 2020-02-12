package com.mozarellabytes.kroy.Entities;


import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Screens.GameScreen;

import static org.junit.jupiter.api.Assertions.*;


class BlasterParticleTest {

    @org.junit.jupiter.api.Test
    public void createTargetPositionTest() {
        FireTruck aPatrol = new FireTruck(GameScreen, Vector2, FireTruckType);
        FireStation aTarget = new FireStation(1,1);
        BlasterParticle testParticle = new BlasterParticle(aPatrol, aTarget);
        Vector2 targetPosition = testParticle.createTargetPosition(aPatrol);
        assertEquals(targetPosition, testParticle.getTarget());


    }

    @org.junit.jupiter.api.Test
    public void updatePositionTest() {
        FireTruck aPatrol = new FireTruck(GameScreen, Vector2, FireTruckType);
        FireStation aTarget = new FireStation(1,1);
        BlasterParticle testParticle = new BlasterParticle(aPatrol, aTarget);
        Vector2 updatedPosition = testParticle.updatePosition();
        assertEquals(updatedPosition, testParticle.getPosition());


    }

    @org.junit.jupiter.api.Test
    public void isHitTest() {
        FireTruck aPatrol = new FireTruck(GameScreen, Vector2, FireTruckType);
        FireStation aTarget = new FireStation(1,1);
        BlasterParticle testParticle = new BlasterParticle(aPatrol, aTarget);
        Vector2 updatedPosition = testParticle.updatePosition();
        assertTrue(testParticle.isHit());


    }

    @org.junit.jupiter.api.Test
    public void getTargetTest() {
        FireTruck aPatrol = new FireTruck(GameScreen, Vector2, FireTruckType);
        FireStation aTarget = new FireStation(1,1);
        BlasterParticle testParticle = new BlasterParticle(aPatrol, aTarget);
        assertEquals(testParticle.getTarget(), aTarget);

    }

    @org.junit.jupiter.api.Test
    public void getSizeTest() {
        FireTruck aPatrol = new FireTruck(GameScreen, Vector2, FireTruckType);
        FireStation aTarget = new FireStation(1,1);
        BlasterParticle testParticle = new BlasterParticle(aPatrol, aTarget);
        assertEquals(testParticle.size(), testParticle.getSize());

    }

    @org.junit.jupiter.api.Test
    public void getColourTest() {
        FireTruck aPatrol = new FireTruck(GameScreen, Vector2, FireTruckType);
        FireStation aTarget = new FireStation(1,1);
        BlasterParticle testParticle = new BlasterParticle(aPatrol, aTarget);
        assertEquals(testParticle.colour(), testParticle.getColour());

    }

    @org.junit.jupiter.api.Test
    public void getPositionTest() {
        FireTruck aPatrol = new FireTruck(GameScreen, Vector2, FireTruckType);
        FireStation aTarget = new FireStation(1,1);
        BlasterParticle testParticle = new BlasterParticle(aPatrol, aTarget);
        assertEquals(testParticle.currentPosition(), testParticle.getPosition());

    }
}

