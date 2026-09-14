package tsvdh.asteroids.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

import static tsvdh.asteroids.Main.BUFFER_SIZE;
import static tsvdh.asteroids.Main.WORLD_SIZE;

public abstract class GameObject {

    Texture texture;
    Sprite sprite;
    Vector2 pos;
    Vector2 movement;
    float size;
    boolean destroyed = false;

    public GameObject(Map<String, Texture> textures) {
        texture = textures.get(getTextureName());
        sprite = getSprite(texture);
        movement = new Vector2();
    }

    public Vector2 getPos() {
        return pos;
    }

    public Vector2 getMovement() {
        return movement;
    }

    public float getScale() {
        return size;
    }

    public float getSize() {
        return size;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    Sprite getSprite(Texture texture) {
        Sprite sprite = new Sprite(texture);
        float ratio = sprite.getHeight() / sprite.getWidth();
        sprite.setSize(1, ratio);
        sprite.setOriginCenter();
        return sprite;
    }

    abstract String getTextureName();

    public void setPos(Vector2 newPos) {
        pos = newPos;
        sprite.setCenter(pos.x, pos.y);
    }

    public void setMovement(Vector2 movement) {
        this.movement = movement;
    }

    public void setSize(float newSize) {
        this.size = newSize;
        this.sprite.setScale(newSize);
    }

    public void destroy() {
        destroyed = true;
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    private static float handleOutOfBounds(float val) {
        float boundsToBoundsLength = WORLD_SIZE + 2 * BUFFER_SIZE;
        if (val < -BUFFER_SIZE)
            val += boundsToBoundsLength;
        if (val > WORLD_SIZE + BUFFER_SIZE)
            val -= boundsToBoundsLength;
        return val;
    }

    public void logic() {
        pos.add(movement.cpy().scl(Gdx.graphics.getDeltaTime()));
        pos.x = handleOutOfBounds(pos.x);
        pos.y = handleOutOfBounds(pos.y);
        setPos(pos);
    }
}
