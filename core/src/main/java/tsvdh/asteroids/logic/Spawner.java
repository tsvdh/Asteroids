package tsvdh.asteroids.logic;

import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import static tsvdh.asteroids.Main.BUFFER_SIZE;
import static tsvdh.asteroids.Main.WORLD_SIZE;

public abstract class Spawner {

    final Random rng;

    Spawner() {
        this.rng = new Random();
    }

    Vector2 getBoundaryPos() {
        float edge = rng.nextInt(4);
        float edgePos = rng.nextFloat();

        if (edge == 0) {
            return new Vector2(WORLD_SIZE * edgePos, 0).add(0, -BUFFER_SIZE);
        }
        else if (edge == 1) {
            return new Vector2(WORLD_SIZE * edgePos, WORLD_SIZE).add(0, BUFFER_SIZE);
        }
        else if (edge == 2) {
            return new Vector2(0, WORLD_SIZE * edgePos).add(- BUFFER_SIZE, 0);
        }
        else {
            return new Vector2(WORLD_SIZE, WORLD_SIZE * edgePos).add(BUFFER_SIZE, 0);
        }
    }

    Vector2 getRandomDir() {
        return new Vector2(rng.nextFloat(-1, 1), rng.nextFloat(-1, 1)).nor();
    }
}
