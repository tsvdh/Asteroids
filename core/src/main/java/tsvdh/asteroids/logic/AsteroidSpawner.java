package tsvdh.asteroids.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

public class AsteroidSpawner {

    private final Random rng;

    public AsteroidSpawner() {
        rng = new Random();
    }

    public void spawn(Collection<Asteroid> asteroids, Map<String, Texture> textures) {
        if (asteroids.size() >= 10)
            return;

        var asteroid = new Asteroid(textures, 10);
        Vector2 spawnPos = getSpawn();
        asteroid.setPos(spawnPos);
        asteroid.setMovement(getDir(spawnPos).setLength(5 * Gdx.graphics.getDeltaTime()));
        asteroids.add(asteroid);
    }

    private Vector2 getSpawn() {
        float edge = rng.nextInt(4);
        float edgePos = rng.nextFloat();

        if (edge == 0) {
            return new Vector2(100 * edgePos, 0).add(0, -5);
        }
        else if (edge == 1) {
            return new Vector2(100 * edgePos, 100).add(0, 5);
        }
        else if (edge == 2) {
            return new Vector2(0, 100 * edgePos).add(-5, 0);
        }
        else {
            return new Vector2(100, 100 * edgePos).add(5, 0);
        }
    }

    private Vector2 getDir(Vector2 spawn) {
        float x = rng.nextFloat(10, 90);
        float y = rng.nextFloat(10, 90);
        return new Vector2(x, y).sub(spawn).nor();
    }
}
