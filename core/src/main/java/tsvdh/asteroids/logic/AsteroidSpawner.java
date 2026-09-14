package tsvdh.asteroids.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import static tsvdh.asteroids.Main.BUFFER_SIZE;
import static tsvdh.asteroids.Main.WORLD_SIZE;

public class AsteroidSpawner {

    private final Random rng;

    public AsteroidSpawner() {
        rng = new Random();
    }

    public void spawn(Collection<Asteroid> asteroids, Map<String, Texture> textures) {
        if (asteroids.size() >= 10)
            return;

        Vector2 spawnPos = getSpawn();
        var asteroid = new Asteroid(textures, spawnPos, 0);
        asteroid.setMovement(getDir().setLength(50));
        asteroid.getSprite().setRotation(rng.nextInt(360));
        asteroids.add(asteroid);
    }

    public void spawnFromDestroyed(Asteroid asteroid, Collection<Asteroid> newAsteroids, Map<String, Texture> textures) {
        if (asteroid.getType() == 2)
            return;

        int newType = asteroid.getType() + 1;
        for (int i = 0; i < 2; i++) {
            var newAsteroid = new Asteroid(textures, asteroid.getPos().cpy(), newType);
            newAsteroid.setMovement(getMovementFrom(asteroid));
            asteroid.getSprite().setRotation(rng.nextInt(360));
            newAsteroids.add(newAsteroid);
        }
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

    private Vector2 getDir() {
        return new Vector2(rng.nextFloat(-1, 1), rng.nextFloat(-1, 1)).nor();
    }

    private Vector2 getMovementFrom(Asteroid asteroid) {
        return asteroid.getMovement().cpy().rotateDeg(rng.nextInt(-90, 90));
    }

}
