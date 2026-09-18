package tsvdh.asteroids.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;
import java.util.Random;

public class Asteroid extends RoundGameObject {

    private static final float[] SIZES = {100, 60, 30};
    private static final float[] SPEEDS = {50, 100, 150};
    private static final int[] SCORES = {10, 20, 40};

    private static final Random textureRng = new Random();
    private final int type;

    public Asteroid(Map<String, Texture> textures, Vector2 pos, int type) {
        super(textures);
        setSize(SIZES[type]);
        setPos(pos);
        this.type = type;
    }

    @Override
    public String getTextureName() {
        return String.format("assets/asteroids/asteroid%s.png", textureRng.nextInt(5) + 1);
    }

    public int getType() {
        return type;
    }

    @Override
    public void setMovement(Vector2 movement) {
        throw new RuntimeException("Not allowed, use 'setDirection' instead");
    }

    public void setDirection(Vector2 dir) {
        movement = dir.setLength(SPEEDS[type]);
    }

    public int getScore() {
        return SCORES[type];
    }
}
