package tsvdh.asteroids.game.tower_defense.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.game.tower_defense.ToughAsteroid;
import tsvdh.asteroids.util.text_manager.AbsoluteTextManager;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;

public class AsteroidTurret extends Building {

    private final Collection<ToughAsteroid> asteroids;
    private Duration coolDown;
    private Instant lastShot;
    private MineLaserManager mineLaserManager;

    public AsteroidTurret(Map<String, Texture> textures, Vector2 pos, AbsoluteTextManager textManager,
                          Collection<ToughAsteroid> asteroids, MineLaserManager mineLaserManager) {
        super(textures, pos, textManager);
        this.asteroids = asteroids;
        coolDown = Duration.ofMillis(200);
        lastShot = Instant.EPOCH;
        this.mineLaserManager = mineLaserManager;
    }

    @Override
    protected int getInitialHealth() {
        return 2;
    }

    @Override
    public String getTextureName() {
        return "assets/buildings/asteroid_turret.png";
    }

    @Override
    public void logic() {
        super.logic();

        asteroids.forEach(asteroid -> {
            if (asteroid.getPos().cpy().sub(getPos()).len() < 200
                    && Duration.between(lastShot, Instant.now()).compareTo(coolDown) >= 0) {
                asteroid.damage();
                lastShot = Instant.now();
                mineLaserManager.addLine(getPos(), asteroid.getPos().cpy());
            }
        });
    }
}
