package tsvdh.asteroids.game.tower_defense;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.logic.Asteroid;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;

public class AsteroidSpawner extends TowerDefenseSpawner {

    private final Duration spawnInterval;
    private Instant lastSpawn = Instant.EPOCH;

    AsteroidSpawner(float worldSize, Duration spawnInterval) {
        super(worldSize);
        this.spawnInterval = spawnInterval;
    }

    void spawn(Collection<ToughAsteroid> asteroids, Map<String, Texture> textures) {
        if (Duration.between(lastSpawn, Instant.now()).compareTo(spawnInterval) < 0)
            return;

        Vector2 spawnPos = getBoundaryPos();
        var asteroid = new ToughAsteroid(textures, spawnPos, 3, 3);
        asteroid.setDirection(getRandomDir(spawnPos));
        asteroid.setRotation(rng.nextInt(360));
        asteroids.add(asteroid);
        lastSpawn = Instant.now();
    }
}
