package tsvdh.asteroids.game.classic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.logic.Asteroid;
import tsvdh.asteroids.logic.Spawner;

import java.util.Collection;
import java.util.Map;

public class AsteroidSpawner extends ClassicSpawner {

    private static final int MAX_ASTEROIDS = 20;

    protected AsteroidSpawner(float worldSize, float bufferSize) {
        super(worldSize, bufferSize);
    }

    public void spawn(Collection<Asteroid> asteroids, Map<String, Texture> textures) {
        boolean bigAsteroidLimit = asteroids.stream()
            .filter(asteroid -> asteroid.getType() == 0)
            .toList().size() >= 10;

        if (asteroids.size() >= MAX_ASTEROIDS || bigAsteroidLimit)
            return;

        Vector2 spawnPos = getBoundaryPos();
        var asteroid = new Asteroid(textures, spawnPos, 0);
        asteroid.setDirection(getRandomDir());
        asteroid.getSprite().setRotation(rng.nextInt(360));
        asteroids.add(asteroid);
    }

    public void spawnFromDestroyed(Asteroid destroyed, Collection<Asteroid> asteroids,
                                   Collection<Asteroid> newAsteroids, Map<String, Texture> textures) {
        if (destroyed.getType() == 2)
            return;

        int newType = destroyed.getType() + 1;
        int newCount = asteroids.size() < MAX_ASTEROIDS ? 2 : 1;
        for (int i = 0; i < newCount; i++) {
            var newAsteroid = new Asteroid(textures, destroyed.getPos().cpy(), newType);
            newAsteroid.setDirection(getDirFrom(destroyed));
            newAsteroid.getSprite().setRotation(rng.nextInt(360));
            newAsteroids.add(newAsteroid);
        }
    }

    private Vector2 getDirFrom(Asteroid asteroid) {
        return asteroid.getMovement().cpy().nor().rotateDeg(rng.nextInt(-90, 90));
    }

}
