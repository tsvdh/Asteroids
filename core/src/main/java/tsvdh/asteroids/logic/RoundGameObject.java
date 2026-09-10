package tsvdh.asteroids.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

public abstract class RoundGameObject extends GameObject {

    Circle collider;

    public RoundGameObject(Map<String, Texture> textures) {
        super(textures);
        collider = new Circle(0, 0, 0.5f);
    }

    @Override
    public final Shape2D getCollider() {
        return collider;
    }

    @Override
    public void setPos(Vector2 newPos) {
        super.setPos(newPos);
        collider.setPosition(newPos);
    }

    @Override
    public void setScale(float newScale) {
        super.setScale(newScale);
        collider.setRadius(newScale / 2);
    }
}
