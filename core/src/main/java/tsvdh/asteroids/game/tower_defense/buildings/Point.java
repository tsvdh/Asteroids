package tsvdh.asteroids.game.tower_defense.buildings;

import com.badlogic.gdx.math.Vector2;

public record Point(int x, int y) {

    public static final float GRID_SIZE = 100;

    public Vector2 toPos() {
        return new Vector2(x, y)
            .scl(GRID_SIZE)
            .add(new Vector2(GRID_SIZE / 2, GRID_SIZE / 2));
    }

    private static int toPoint(float pos) {
        return (int) (pos / GRID_SIZE);
    }

    public static Point fromPos(Vector2 pos) {
        return new Point(toPoint(pos.x), toPoint(pos.y));
    }
}
