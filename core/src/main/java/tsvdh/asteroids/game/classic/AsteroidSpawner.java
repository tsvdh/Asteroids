package tsvdh.asteroids.game.classic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.logic.Asteroid;
import tsvdh.asteroids.logic.Spawner;

import java.util.Collection;
import java.util.Map;

class AsteroidSpawner extends ClassicSpawner {

    private static final int MAX_ASTEROIDS = 20;

    AsteroidSpawner(float worldSize, float bufferSize) {
        super(worldSize, bufferSize);
    }

    void spawn(Collection<Asteroid> asteroids, Map<String, Texture> textures) {
        boolean bigAsteroidLimit = asteroids.stream()
            .filter(asteroid -> asteroid.getType() == 0)
            .toList().size() >= 10;

        if (asteroids.size() >= MAX_ASTEROIDS || bigAsteroidLimit)
            return;

        Vector2 spawnPos = getBoundaryPos();
        var asteroid = new Asteroid(textures, spawnPos, 0);
        asteroid.setDirection(getRandomDir());
        asteroid.setRotation(rng.nextInt(360));
        asteroids.add(asteroid);
    }

    void spawnFromDestroyed(Asteroid destroyed, Collection<Asteroid> asteroids,
                                   Collection<Asteroid> newAsteroids, Map<String, Texture> textures) {
        if (destroyed.getType() == 2)
            return;

        int newType = destroyed.getType() + 1;
        int newCount = asteroids.size() < MAX_ASTEROIDS ? 2 : 1;
        Vector2 prevDir = null;

        for (int i = 0; i < newCount; i++) {
            var newAsteroid = new Asteroid(textures, destroyed.getPos().cpy(), newType);

            Vector2 newDir;

            if (prevDir == null)
                newDir = getDirFrom(destroyed);
            else {
                float angle;
                do {
                    newDir = getDirFrom(destroyed);
                    angle = newDir.angleDeg(prevDir);
                    if (angle > 180)
                        angle = 360 - angle;
                }
                while (angle < 20);
            }
            prevDir = newDir;

            newAsteroid.setDirection(newDir);
            newAsteroid.setRotation(rng.nextInt(360));
            newAsteroids.add(newAsteroid);
        }
    }

    private Vector2 getDirFrom(Asteroid asteroid) {
        return asteroid.getMovement().cpy().nor().rotateDeg(rng.nextInt(-90, 90));
    }

}
