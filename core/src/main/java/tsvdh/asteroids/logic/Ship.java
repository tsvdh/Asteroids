package tsvdh.asteroids.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.game.tower_defense.Damageable;
import tsvdh.asteroids.game.tower_defense.Miner;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;

public class Ship extends RoundGameObject implements Miner, Damageable {

    private static final float SIZE = 30;

    private final Texture thrustTexture;
    private boolean thrust;

    private Instant lastDeath;
    private final Duration shootInterval;
    private Instant lastShot;

    int health;

    public Ship(Map<String, Texture> textures, Vector2 pos, Duration shootInterval, int health) {
        super(textures);
        setSize(SIZE);
        setPos(pos);
        thrustTexture = textures.get("assets/ship_with_thrust.png");
        thrust = false;
        lastDeath = Instant.EPOCH;
        this.shootInterval = shootInterval;
        lastShot = Instant.EPOCH;
        this.health = health;
    }

    @Override
    public String getTextureName() {
        return "assets/ship.png";
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (shouldDraw())
            super.draw(batch);
    }

    public void rotateClockwise() {
        setRotation(getRotation() - 360 * Gdx.graphics.getDeltaTime());
    }

    public void rotateCounterClockwise() {
        setRotation(getRotation() + 360 * Gdx.graphics.getDeltaTime());
    }

    public void thrust() {
        getMovement().add(forward.cpy().scl(5));
        if (!thrust) {
            sprite.setTexture(thrustTexture);
            thrust = true;
        }
    }

    public void noThrust() {
        if (thrust) {
            sprite.setTexture(texture);
            thrust = false;
        }
    }

    @Override
    public void logic() {
        getMovement().scl(1f - 0.1f * Gdx.graphics.getDeltaTime());
        super.logic();
    }

    public void shootLaser(Collection<Laser> shipLasers, Map<String, Texture> textures) {
        if (Duration.between(lastShot, Instant.now()).compareTo(shootInterval) < 0)
            return;

        var laser = new Laser(textures, pos.cpy().add(forward.cpy().setLength(size)), Duration.ofSeconds(1));
        laser.setMovement(forward.cpy().setLength(500));
        shipLasers.add(laser);
        lastShot = Instant.now();
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException();
    }

    public boolean isVulnerable() {
        return Duration.between(lastDeath, Instant.now()).toMillis() >= 2000;
    }

    public boolean shouldDraw() {
        long timeSinceDeath = Duration.between(lastDeath, Instant.now()).toMillis();
        if (timeSinceDeath >= 2000)
            return true;

        int blinkTime = timeSinceDeath < 1000 ? 300 : 150;
        return (timeSinceDeath / blinkTime) % 2 == 0;
    }

    @Override
    public float mine() {
        return 1 * Gdx.graphics.getDeltaTime();
    }

    @Override
    public void damage() {
        health--;
        if (health > 0) {
            setMovement(new Vector2(0, 0));
            forward = new Vector2(0, 1);
            sprite.setRotation(0);
            lastDeath = Instant.now();
        } else {
            super.destroy();
        }
    }

    @Override
    public void damageMax() {
        throw new UnsupportedOperationException();
    }

    public int getHealth() {
        return health;
    }
}
