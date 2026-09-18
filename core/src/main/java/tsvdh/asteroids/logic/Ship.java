package tsvdh.asteroids.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class Ship extends RoundGameObject {

    private static final float SIZE = 30;

    private final Texture thrustTexture;
    private boolean thrust;

    private Instant lastDeath;

    public Ship(Map<String, Texture> textures, Vector2 pos) {
        super(textures);
        setPos(pos);
        setSize(SIZE);
        thrustTexture = textures.get("assets/ship_with_thrust.png");
        thrust = false;
        lastDeath = Instant.EPOCH;
    }

    @Override
    public String getTextureName() {
        return "assets/ship.png";
    }

    @Override
    public void draw(SpriteBatch batch) {
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

    public Laser shootLaser(Map<String, Texture> textures) {
        var laser = new Laser(textures, pos.cpy().add(forward.cpy().setLength(size)), Duration.ofSeconds(1));
        laser.setMovement(forward.cpy().setLength(500));
        return laser;
    }

    @Override
    public void destroy() {
        setMovement(new Vector2(0, 0));
        forward = new Vector2(0, 1);
        sprite.setRotation(0);
        lastDeath = Instant.now();
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
}
