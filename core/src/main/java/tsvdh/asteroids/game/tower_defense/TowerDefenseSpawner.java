package tsvdh.asteroids.game.tower_defense;

import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.logic.Spawner;

import java.time.Duration;
import java.time.Instant;

public class TowerDefenseSpawner extends Spawner {

    private final float worldSize;
    Duration spawnInterval;
    Instant lastSpawn = Instant.EPOCH;

    public TowerDefenseSpawner(float worldSize,  Duration spawnInterval) {
        this.worldSize = worldSize;
        this.spawnInterval = spawnInterval;
    }

    public void setSpawnInterval(Duration spawnInterval) {
        this.spawnInterval = spawnInterval;
    }

    boolean mustWait() {
        return Duration.between(lastSpawn, Instant.now()).compareTo(spawnInterval) < 0;
    }

    @Override
    protected Vector2 getBoundaryPos() {
        return getBoundaryPos(worldSize, 0);
    }

    private float getRandomPointInDimension() {
        float worldFraction = worldSize / 10;
        return rng.nextFloat(worldFraction, worldSize - worldFraction);
    }

    Vector2 getRandomDir(Vector2 spawnPos) {
        var targetPos = new Vector2(getRandomPointInDimension(), getRandomPointInDimension());
        return targetPos.sub(spawnPos);
    }
}
