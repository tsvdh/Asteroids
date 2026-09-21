package tsvdh.asteroids.logic;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

public abstract class RectangularGameObject extends GameObject {

    Rectangle collider;
    float width;
    float height;
    float sizeRatio;

    public RectangularGameObject(Map<String, Texture> textures) {
        super(textures);
        sizeRatio = (float) texture.getHeight() / texture.getWidth();
        width = 1;
        height = sizeRatio;
        collider = new Rectangle(0, 0, 1, height);
    }

    @Override
    public void setPos(Vector2 newPos) {
        super.setPos(newPos);
        collider.setCenter(newPos);
    }

    @Override
    public void setSize(float newSize) {
        super.setSize(newSize);
        collider.setSize(newSize, newSize * sizeRatio);
    }

    @Override
    public void setForward(Vector2 forward) {
        throw new RuntimeException("Can't rotate rectangular object");
    }

    @Override
    public void setRotation(float rotation) {
        throw new RuntimeException("Can't rotate rectangular object");
    }

    public final Rectangle getCollider() {
        return collider;
    }
}
