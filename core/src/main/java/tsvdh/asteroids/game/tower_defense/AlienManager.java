package tsvdh.asteroids.game.tower_defense;

import com.badlogic.gdx.graphics.Texture;
import tsvdh.asteroids.game.tower_defense.buildings.AsteroidTurret;
import tsvdh.asteroids.logic.GameObject;
import tsvdh.asteroids.logic.Ship;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AlienManager extends TowerDefenseSpawner {

    private record Intensity(int score, Duration interval, int amount) {

        private static Intensity interpolate(int score) {
            double frac = (double) Math.clamp(score, MIN_INTENSITY.score, MAX_INTENSITY.score) / MAX_INTENSITY.score;

            long min_interval = MIN_INTENSITY.interval.toMillis();
            long max_interval = MAX_INTENSITY.interval.toMillis();

            Duration interval = Duration.ofMillis((long) (min_interval + (max_interval - min_interval) * frac));

            int amount = (int) (MIN_INTENSITY.amount + (MAX_INTENSITY.amount - MIN_INTENSITY.amount) * frac);
            return new Intensity(score, interval, amount);
        }
    }

    private static final Intensity MIN_INTENSITY = new Intensity(0, Duration.ofSeconds(60), 1);
    private static final Intensity MAX_INTENSITY = new Intensity(10000, Duration.ofSeconds(1), 10);

    private final Collection<Alien> aliens;
    private final List<AsteroidTurret> asteroidTurrets;
    private final Ship ship;

    public AlienManager(float worldSize, Collection<Alien> aliens,
                        List<AsteroidTurret> asteroidTurrets, Ship ship) {
        super(worldSize, Duration.ofSeconds(Long.MAX_VALUE));
        this.aliens = aliens;
        this.asteroidTurrets = asteroidTurrets;
        this.ship = ship;
    }

    GameObject selectTarget() {
        if (asteroidTurrets.isEmpty())
            return ship;

        if (rng.nextFloat() < 0.1)
            return ship;

        return asteroidTurrets.get(rng.nextInt(asteroidTurrets.size()));
    }

    void spawn(Map<String, Texture> textures, int score) {
        Intensity curIntensity = Intensity.interpolate(score);
        spawnInterval = curIntensity.interval;

        if (mustWait())
            return;

        for (int i = 0; i < curIntensity.amount; i++) {
            var alien = new Alien(textures, getBoundaryPos());

            alien.setTarget(selectTarget());
            aliens.add(alien);
        }
        lastSpawn = Instant.now();
    }

    void retarget() {
        aliens.forEach(alien -> alien.setTarget(selectTarget()));
    }
}
