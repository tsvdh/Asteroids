package tsvdh.asteroids.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class Laser extends RoundGameObject {

    private static final float SIZE = 5;

    private final Instant creationInstant;

    public Laser(Map<String, Texture> textures, Vector2 pos) {
        super(textures);
        setSize(SIZE);
        setPos(pos);
        creationInstant = Instant.now();
    }

    @Override
    String getTextureName() {
        return "assets/laser.png";
    }

    @Override
    public void logic() {
        super.logic();
        if (Duration.between(creationInstant, Instant.now()).toMillis() > 1000)
            destroyed = true;
    }
}
