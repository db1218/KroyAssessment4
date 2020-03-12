package com.mozarellabytes.kroy.Descriptors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;

public class Desc {
    public static class FireTruck {
        public String type, rotation;
        public int x, y;
        public float health, reserve;
    }

    public static class FireStation {
        public float x, y, health;
    }

    public static class Fortress {
        public String type;
        public float x, y, health;
    }

    public static class Patrol {
        public String type, name;
        public float x, y, health;
        public Queue<Vector2> path;
    }
}

