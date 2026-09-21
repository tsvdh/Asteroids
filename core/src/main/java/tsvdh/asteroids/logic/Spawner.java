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

    protected Vector2 getBoundaryPos(float worldSize, float offset) {
        float edge = rng.nextInt(4);
        float edgePos = rng.nextFloat();

        if (edge == 0) {
            return new Vector2(worldSize * edgePos, 0).add(0, -offset);
        }
        else if (edge == 1) {
            return new Vector2(worldSize * edgePos, worldSize).add(0, offset);
        }
        else if (edge == 2) {
            return new Vector2(0, worldSize * edgePos).add(- offset, 0);
        }
        else {
            return new Vector2(worldSize, worldSize * edgePos).add(offset, 0);
        }
    }
}
