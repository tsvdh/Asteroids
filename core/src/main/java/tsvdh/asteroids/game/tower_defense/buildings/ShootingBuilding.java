package tsvdh.asteroids.game.tower_defense.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.logic.GameObject;
import tsvdh.asteroids.util.text_manager.AbsoluteTextManager;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;

abstract class ShootingBuilding extends Building {

    private final Duration coolDown;
    private Instant lastShot;
    private final Collection<? extends GameObject> targets;
    private final float range;

    ShootingBuilding(Map<String, Texture> textures, Vector2 pos, AbsoluteTextManager textManager,
                     Duration coolDown, Collection<? extends GameObject> targets, float range) {
        super(textures, pos, textManager);
        this.coolDown = coolDown;
        this.lastShot = Instant.EPOCH;
        this.targets = targets;
        this.range = range;
    }

    abstract void onShot(GameObject target);

    private float getDistTo(GameObject object) {
        return getPos().cpy().sub(object.getPos()).len();
    }

    @Override
    public void logic() {
        super.logic();

        if (Duration.between(lastShot, Instant.now()).compareTo(coolDown) < 0)
            return;

        targets.stream()
               .filter(target -> target.getPos().cpy().sub(getPos()).len() < range)
               .min((t1, t2) -> Float.compare(getDistTo(t1), getDistTo(t2)))
               .ifPresent(target -> {
                   onShot(target);
                   lastShot = Instant.now();
               });
    }
}
