package tsvdh.asteroids.game.tower_defense;

import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.logic.Spawner;

public class TowerDefenseSpawner extends Spawner {

    private final float worldSize;

    public TowerDefenseSpawner(float worldSize) {
        this.worldSize = worldSize;
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
