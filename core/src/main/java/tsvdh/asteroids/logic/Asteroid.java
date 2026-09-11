package tsvdh.asteroids.logic;

import com.badlogic.gdx.graphics.Texture;

import java.util.Map;
import java.util.Random;

public class Asteroid extends RoundGameObject {

    private static final Random textureRng = new Random();

    public Asteroid(Map<String, Texture> textures, int size) {
        super(textures);
        setScale(size);
    }

    @Override
    String getTextureName() {
        return String.format("assets/asteroids/asteroid%s.png", textureRng.nextInt(5) + 1);
    }
}
