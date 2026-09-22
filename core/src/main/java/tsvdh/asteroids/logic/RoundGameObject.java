package tsvdh.asteroids.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

public abstract class RoundGameObject extends GameObject {

    Circle collider;
    Rectangle rectangleCollider;

    public RoundGameObject(Map<String, Texture> textures) {
        super(textures);
        collider = new Circle(0, 0, 0.5f);
        rectangleCollider = new Rectangle(0, 0, 1, 1);
        setForward(new Vector2(0, 1));
    }

    public final Circle getCollider() {
        return collider;
    }

    public final Rectangle getRectangleCollider() {
        return rectangleCollider;
    }

    @Override
    public void setPos(Vector2 newPos) {
        super.setPos(newPos);
        collider.setPosition(newPos);
        rectangleCollider.setCenter(newPos);
    }

    @Override
    public void setSize(float newSize) {
        super.setSize(newSize);
        collider.setRadius(newSize / 2);
        rectangleCollider.setSize(newSize);
    }
}
