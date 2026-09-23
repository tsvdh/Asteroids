package tsvdh.asteroids.game.tower_defense.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.game.tower_defense.ToughAsteroid;
import tsvdh.asteroids.logic.GameObject;
import tsvdh.asteroids.util.text_manager.AbsoluteTextManager;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;

public class AsteroidTurret extends ShootingBuilding {

    private final MineLaserManager mineLaserManager;
    private Duration spinDuration;
    private float rotationSpeed;
    private float curRotation;

    public AsteroidTurret(Map<String, Texture> textures, Vector2 pos, AbsoluteTextManager textManager,
                          Collection<ToughAsteroid> asteroids, MineLaserManager mineLaserManager) {
        super(textures, pos, textManager, Duration.ofMillis(200), asteroids, 300);
        this.mineLaserManager = mineLaserManager;
        spinDuration = Duration.ofMillis(1000);
        rotationSpeed = 5f;
        curRotation = 0;
    }

    @Override
    protected int getInitialHealth() {
        return 2;
    }

    @Override
    protected String getDetailTextureName() {
        return "assets/buildings/asteroid_turret.png";
    }

    @Override
    public void logic() {
        super.logic();

        if (Duration.between(lastShot, Instant.now()).compareTo(spinDuration) >= 0)
            return;

        curRotation = (curRotation + rotationSpeed * 360 * Gdx.graphics.getDeltaTime()) % 360;
        movingElement.setRotation(curRotation);
    }

    @Override
    void onShot(GameObject target) {
        var asteroid = (ToughAsteroid) target;
        asteroid.damage();
        mineLaserManager.addLine(getPos(), asteroid.getPos().cpy());
    }
}
