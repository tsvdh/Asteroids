package tsvdh.asteroids.game.classic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.logic.Alien;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;

public class AlienSpawner extends ClassicSpawner {

    private Instant lastSpawn;

    protected AlienSpawner(float worldSize, float bufferSize) {
        super(worldSize, bufferSize);
        lastSpawn = Instant.now();
    }

    public void spawn(Collection<Alien> aliens, Map<String, Texture> textures) {
        if (aliens.size() >= 2 || Duration.between(lastSpawn, Instant.now()).toMillis() < 20000)
            return;

        Vector2 spawnPos = getBoundaryPos();
        var alien = new Alien(textures, spawnPos);
        alien.setMovement(getRandomDir().setLength(150));
        aliens.add(alien);
        lastSpawn = Instant.now();
    }


}
