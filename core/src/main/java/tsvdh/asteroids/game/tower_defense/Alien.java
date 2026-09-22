package tsvdh.asteroids.game.tower_defense;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.logic.GameObject;
import tsvdh.asteroids.logic.RoundGameObject;

import java.util.Map;

public class Alien extends RoundGameObject implements Damageable {

    private static final float SIZE = 50;
    private static final float SPEED = 100;

    private int health;
    private GameObject target;

    public Alien(Map<String, Texture> textures, Vector2 pos) {
        super(textures);
        setSize(SIZE);
        setPos(pos);
        health = 5;
    }

    @Override
    public String getTextureName() {
        return "assets/alien.png";
    }

    @Override
    public void damage() {
        health--;
        if (health == 0)
            destroy();
    }

    @Override
    public void damageMax() {
        while (health > 0)
            damage();
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }

    @Override
    public void logic() {
        if (target != null)
            setMovement(target.getPos().cpy().sub(getPos()).setLength(SPEED));

        super.logic();
    }
}
