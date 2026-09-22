package tsvdh.asteroids.game.classic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.logic.Laser;
import tsvdh.asteroids.logic.RoundGameObject;
import tsvdh.asteroids.logic.Ship;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

class Alien extends RoundGameObject {

    private static final float SIZE = 75;

    private Instant lastShot;

    public Alien(Map<String, Texture> textures, Vector2 pos) {
        super(textures);
        setSize(SIZE);
        setPos(pos);
        lastShot = Instant.now();
    }

    @Override
    public String getTextureName() {
        return "assets/alien.png";
    }

    public Optional<Laser> shootLaser(Map<String, Texture> textures, Ship ship) {
        if (Duration.between(lastShot, Instant.now()).toMillis() < 2000)
            return Optional.empty();

        Vector2 dir = ship.getPos().cpy().sub(getPos()).nor();
        var laser = new Laser(textures, getPos().cpy().add(dir.cpy().setLength(getSize())),
                              Duration.ofSeconds(2));
        laser.setMovement(dir.cpy().setLength(250));
        lastShot = Instant.now();
        return Optional.of(laser);
    }
}
