package tsvdh.asteroids.game.classic;

import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.logic.Spawner;

public abstract class ClassicSpawner extends Spawner {

    private final float worldSize;
    private final float bufferSize;

    protected ClassicSpawner(float worldSize, float bufferSize) {
        super();
        this.worldSize = worldSize;
        this.bufferSize = bufferSize;
    }

    @Override
    protected Vector2 getBoundaryPos() {
        float edge = rng.nextInt(4);
        float edgePos = rng.nextFloat();

        if (edge == 0) {
            return new Vector2(worldSize * edgePos, 0).add(0, -bufferSize);
        }
        else if (edge == 1) {
            return new Vector2(worldSize * edgePos, worldSize).add(0, bufferSize);
        }
        else if (edge == 2) {
            return new Vector2(0, worldSize * edgePos).add(- bufferSize, 0);
        }
        else {
            return new Vector2(worldSize, worldSize * edgePos).add(bufferSize, 0);
        }
    }
}
