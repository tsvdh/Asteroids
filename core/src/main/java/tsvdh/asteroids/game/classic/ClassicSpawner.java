package tsvdh.asteroids.game.classic;

import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.logic.Spawner;

public abstract class ClassicSpawner extends Spawner {

    private final float worldSize;
    private final float bufferSize;

    ClassicSpawner(float worldSize, float bufferSize) {
        super();
        this.worldSize = worldSize;
        this.bufferSize = bufferSize;
    }

    @Override
    protected Vector2 getBoundaryPos() {
        return getBoundaryPos(worldSize, bufferSize);
    }
}
