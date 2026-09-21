package tsvdh.asteroids.game.tower_defense;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.logic.Asteroid;

import java.util.Map;

public class ToughAsteroid extends Asteroid {

    private int health;

    public ToughAsteroid(Map<String, Texture> textures, Vector2 pos, int type, int health) {
        super(textures, pos, type);
        this.health = health;
    }

    @Override
    public void destroy() {
        health--;
        if (health == 0)
            super.destroy();
    }
}
