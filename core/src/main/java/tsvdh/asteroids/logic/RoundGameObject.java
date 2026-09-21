package tsvdh.asteroids.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

public abstract class RoundGameObject extends GameObject {

    Circle collider;

    public RoundGameObject(Map<String, Texture> textures) {
        super(textures);
        collider = new Circle(0, 0, 0.5f);
        setForward(new Vector2(0, 1));
    }

    public final Circle getCollider() {
        return collider;
    }

    @Override
    public void setPos(Vector2 newPos) {
        super.setPos(newPos);
        collider.setPosition(newPos);
    }

    @Override
    public void setSize(float newSize) {
        super.setSize(newSize);
        collider.setRadius(newSize / 2);
    }
}
