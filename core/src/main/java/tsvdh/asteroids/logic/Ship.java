package tsvdh.asteroids.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

public class Ship extends RoundGameObject {

    private Texture thrustTexture;
    private Vector2 forward;
    private boolean thrust;

    public Ship(Map<String, Texture> textures) {
        super(textures);
        thrustTexture = textures.get("assets/ship_with_thrust.png");
        forward = Vector2.Y;
        thrust = false;
        setScale(3);
        setPos(new Vector2(50, 50));
    }

    @Override
    protected Texture getTexture(Map<String, Texture> textures) {
        return textures.get("assets/ship.png");
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
        getMovement().add(forward.cpy().scl(1 * Gdx.graphics.getDeltaTime()));
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
        getMovement()
            .scl(1f - 0.3f * Gdx.graphics.getDeltaTime())
            .clamp(0, 20);

        super.logic();
    }
}
