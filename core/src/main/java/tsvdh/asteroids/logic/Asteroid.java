package tsvdh.asteroids.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;
import java.util.Random;

public class Asteroid extends RoundGameObject {

    private static final float[] SIZES = {100, 60, 30};

    private static final Random textureRng = new Random();
    private final int type;

    public Asteroid(Map<String, Texture> textures, Vector2 pos, int type) {
        super(textures);
        setSize(SIZES[type]);
        setPos(pos);
        this.type = type;
    }

    @Override
    String getTextureName() {
        return String.format("assets/asteroids/asteroid%s.png", textureRng.nextInt(5) + 1);
    }

    public int getType() {
        return type;
    }
}
