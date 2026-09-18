package tsvdh.asteroids.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class Laser extends RoundGameObject {

    private static final float SIZE = 5;

    private final Instant creationInstant;
    private final Duration lifeTime;

    public Laser(Map<String, Texture> textures, Vector2 pos, Duration lifeTime) {
        super(textures);
        setSize(SIZE);
        setPos(pos);
        creationInstant = Instant.now();
        this.lifeTime = lifeTime;
    }

    @Override
    public String getTextureName() {
        return "assets/laser.png";
    }

    @Override
    public void logic() {
        super.logic();
        if (Duration.between(creationInstant, Instant.now()).toMillis() > lifeTime.toMillis())
            destroyed = true;
    }
}
