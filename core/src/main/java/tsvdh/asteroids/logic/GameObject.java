package tsvdh.asteroids.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

public abstract class GameObject {

    Texture texture;
    Sprite sprite;
    Vector2 pos;
    Vector2 movement;
    float scale;

    public GameObject(Map<String, Texture> textures) {
        texture = getTexture(textures);
        sprite = getSprite(texture);
        pos = new Vector2();
        movement = new Vector2();
        scale = 1;
    }

    public Vector2 getPos() {
        return pos;
    }

    public Vector2 getMovement() {
        return movement;
    }

    public float getScale() {
        return scale;
    }

    Sprite getSprite(Texture texture) {
        Sprite sprite = new Sprite(texture);
        float ratio = sprite.getHeight() / sprite.getWidth();
        sprite.setSize(1, ratio);
        sprite.setOriginCenter();
        return sprite;
    }

    abstract Texture getTexture(Map<String, Texture> textures);

    public abstract Shape2D getCollider();

    public void setPos(Vector2 newPos) {
        pos = newPos;
        sprite.setCenter(pos.x, pos.y);
    }

    public void setMovement(Vector2 movement) {
        this.movement = movement;
    }

    public void setScale(float newScale) {
        this.scale = newScale;
        this.sprite.setScale(newScale);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    private static float handleOutOfBounds(float val) {
        float bufferSize = 5;
        float worldSize = 100;
        float totalDist = worldSize + 2 * bufferSize;
        if (val < -bufferSize)
            val += totalDist;
        if (val > worldSize + bufferSize)
            val -= totalDist;
        return val;
    }

    public void logic() {
        pos.add(movement);
        pos.x = handleOutOfBounds(pos.x);
        pos.y = handleOutOfBounds(pos.y);
        setPos(pos);
    }
}
