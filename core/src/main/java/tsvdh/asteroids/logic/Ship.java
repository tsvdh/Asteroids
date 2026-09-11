package tsvdh.asteroids.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

public class Ship extends RoundGameObject {

    private final Texture thrustTexture;
    private Vector2 forward;
    private boolean thrust;
    private int lives;

    public Ship(Map<String, Texture> textures) {
        super(textures);
        thrustTexture = textures.get("assets/ship_with_thrust.png");
        forward = new Vector2(0, 1);
        thrust = false;
        setScale(3);
        setPos(new Vector2(50, 50));
        lives = 3;
    }

    @Override
    String getTextureName() {
        return "assets/ship.png";
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    public void rotateClockwise() {
        forward.rotateDeg(- 360 * Gdx.graphics.getDeltaTime());
        sprite.rotate(- 360 * Gdx.graphics.getDeltaTime());
    }

    public void rotateCounterClockwise() {
        forward.rotateDeg(360 * Gdx.graphics.getDeltaTime());
        sprite.rotate(360 * Gdx.graphics.getDeltaTime());
    }

    public void thrust() {
        getMovement().add(forward.cpy().scl(1));
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
        getMovement().scl(1f - 0.3f * Gdx.graphics.getDeltaTime());
        super.logic();
    }

    public Laser shootLaser(Map<String, Texture> textures) {
        var laser = new Laser(textures);
        laser.setPos(pos.cpy().add(forward.cpy().setLength(3)));
        laser.setMovement(forward.cpy().setLength(100));
        return laser;
    }

    public boolean notGameOver() {
        return lives > 0;
    }

    @Override
    public void destroy() {
        lives--;
        setPos(new Vector2(50, 50));
        setMovement(new Vector2(0, 0));
        forward = new Vector2(0, 1);
        sprite.setRotation(0);
    }
}
