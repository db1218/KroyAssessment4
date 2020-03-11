package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.GdxTestRunner;
import com.mozarellabytes.kroy.Screens.GameScreen;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(GdxTestRunner.class)
public class PatrolTest {

    @Mock
    GameScreen gameScreenMock;

    Patrol patrol;

    @Before
    public void setUp() {
        initMocks(this);
        Queue<Vector2> queue = new Queue<>();
        queue.addLast(new Vector2(0,0));
        queue.addLast(new Vector2(10,0));
        queue.addLast(new Vector2(10,10));
        queue.addLast(new Vector2(0,10));
        patrol = new Patrol("Green", 100, 0, 0, queue);
    }

    @Test
    public void definePath() {
        Patrol patrol = new Patrol(PatrolType.Green);

        Queue<Vector2> path = new Queue<>();
        path.addLast(new Vector2(18,2));
        path.addLast(new Vector2(48,2));
        path.addLast(new Vector2(48,20));
        path.addLast(new Vector2(18,20));

        assertEquals(path, patrol.getPath());
    }
}
