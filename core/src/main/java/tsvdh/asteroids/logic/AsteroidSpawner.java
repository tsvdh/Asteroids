package tsvdh.asteroids.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.Collection;
import java.util.Map;

public class AsteroidSpawner extends Spawner {

    public void spawn(Collection<Asteroid> asteroids, Map<String, Texture> textures) {
        if (asteroids.stream()
                .filter(asteroid -> asteroid.getType() == 0)
                .toList().size() >= 10)
            return;

        Vector2 spawnPos = getBoundaryPos();
        var asteroid = new Asteroid(textures, spawnPos, 0);
        asteroid.setMovement(getRandomDir().setLength(50));
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

    private Vector2 getMovementFrom(Asteroid asteroid) {
        return asteroid.getMovement().cpy().rotateDeg(rng.nextInt(-90, 90));
    }

}
