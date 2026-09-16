package tsvdh.asteroids.logic;

import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public abstract class Spawner {

    protected final Random rng;

    protected Spawner() {
        this.rng = new Random();
    }

    protected abstract Vector2 getBoundaryPos();

    protected Vector2 getRandomDir() {
        return new Vector2(rng.nextFloat(-1, 1), rng.nextFloat(-1, 1)).nor();
    }
}
