package tsvdh.asteroids.game.tower_defense.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.LinkedList;

public class MineLaserManager {

    record Line(Vector2 start, Vector2 end, Instant creation) {}

    private static final Duration LASER_LIFETIME = Duration.ofMillis(150);

    private final Collection<Line> lines = new LinkedList<>();
    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public MineLaserManager(Viewport viewport) {
        this.viewport = viewport;
    }

    public void addLine(Vector2 start, Vector2 end) {
        lines.add(new Line(start, end, Instant.now()));
    }

    public void draw() {
        lines.removeIf(line -> Duration.between(line.creation, Instant.now()).compareTo(LASER_LIFETIME) >= 0);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        lines.forEach(line -> {
            float opacity = 1 - (float) Duration.between(line.creation, Instant.now()).toMillis() / LASER_LIFETIME.toMillis();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(new Color(1, 1, 1, opacity));
            shapeRenderer.line(line.start, line.end);
            shapeRenderer.end();
        });
    }
}
