package tsvdh.asteroids.game.tower_defense;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.logic.Asteroid;

import java.util.Map;

public class ToughAsteroid extends Asteroid implements Damageable {

    private int health;
    private boolean addsToScore;

    public ToughAsteroid(Map<String, Texture> textures, Vector2 pos, int type, int health) {
        super(textures, pos, type);
        this.health = health;
        addsToScore = true;
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void damage() {
        health--;
        if (health == 0)
            super.destroy();
    }

    @Override
    public void damageMax() {
        addsToScore = false;
        while (health > 0)
            damage();
    }

    public boolean addsToScore() {
        return addsToScore;
    }
}
