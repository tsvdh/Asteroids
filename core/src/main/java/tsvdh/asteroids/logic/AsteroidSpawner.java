package tsvdh.asteroids.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import static tsvdh.asteroids.Main.BUFFER_SIZE;
import static tsvdh.asteroids.Main.WORLD_SIZE;

public class AsteroidSpawner {

    private static final float SIZE = 100;

    private final Random rng;

    public AsteroidSpawner() {
        rng = new Random();
    }

    public void spawn(Collection<Asteroid> asteroids, Map<String, Texture> textures) {
        if (asteroids.size() >= 10)
            return;

        var asteroid = new Asteroid(textures, (int) SIZE);
        Vector2 spawnPos = getSpawn();
        asteroid.setPos(spawnPos);
        asteroid.setMovement(getDir(spawnPos).setLength(50));
        asteroids.add(asteroid);
    }

    private Vector2 getSpawn() {
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

    private Vector2 getDir(Vector2 spawn) {
        return new Vector2(rng.nextFloat(-1, 1), rng.nextFloat(-1, 1)).nor();
        // float x = rng.nextFloat(10, 90);
        // float y = rng.nextFloat(10, 90);
        // return new Vector2(x, y).sub(spawn).nor();
    }
}
