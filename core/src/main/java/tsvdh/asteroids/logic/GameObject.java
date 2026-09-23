package tsvdh.asteroids.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

public abstract class GameObject {

    Texture texture;
    Sprite sprite;
    Vector2 pos;
    Vector2 movement;
    float size;
    boolean destroyed = false;
    Vector2 forward;

    public GameObject(Map<String, Texture> textures) {
        texture = textures.get(getTextureName());
        sprite = makeSprite(texture);
        movement = new Vector2();
    }

    public Vector2 getPos() {
        return pos;
    }

    public Vector2 getMovement() {
        return movement;
    }

    public float getSize() {
        return size;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Vector2 getForward() {
        return forward;
    }

    public float getRotation() {
        return forward.angleDeg();
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    protected Sprite makeSprite(Texture texture) {
        Sprite sprite = new Sprite(texture);
        float ratio = sprite.getHeight() / sprite.getWidth();
        sprite.setSize(1, ratio);
        sprite.setOriginCenter();
        return sprite;
    }

    public abstract String getTextureName();

    public void setPos(Vector2 newPos) {
        pos = newPos;
        sprite.setCenter(pos.x, pos.y);
    }

    public void setMovement(Vector2 movement) {
        this.movement = movement;
    }

    public void setSize(float newSize) {
        this.size = newSize;
        sprite.setScale(newSize);
    }

    public void setForward(Vector2 forward) {
        this.forward = forward;
        sprite.setRotation(forward.angleDeg() - 90);
    }

    public void setRotation(float rotation) {
        forward = new Vector2(1, 0).rotateDeg(rotation);
        sprite.setRotation(rotation - 90);
    }

    public void destroy() {
        destroyed = true;
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void logic() {
        pos.add(movement.cpy().scl(Gdx.graphics.getDeltaTime()));
        setPos(pos);
    }
}
