package tsvdh.asteroids.logic;

import com.badlogic.gdx.graphics.Texture;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class Laser extends RoundGameObject {

    private final Instant creationInstant;

    public Laser(Map<String, Texture> textures) {
        super(textures);
        setScale(0.5f);
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
