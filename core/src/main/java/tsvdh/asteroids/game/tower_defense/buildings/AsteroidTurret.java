package tsvdh.asteroids.game.tower_defense.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.game.tower_defense.ToughAsteroid;
import tsvdh.asteroids.logic.GameObject;
import tsvdh.asteroids.util.text_manager.AbsoluteTextManager;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;

public class AsteroidTurret extends ShootingBuilding {

    private final MineLaserManager mineLaserManager;

    public AsteroidTurret(Map<String, Texture> textures, Vector2 pos, AbsoluteTextManager textManager,
                          Collection<ToughAsteroid> asteroids, MineLaserManager mineLaserManager) {
        super(textures, pos, textManager, Duration.ofMillis(200), asteroids, 300);
        this.mineLaserManager = mineLaserManager;
    }

    @Override
    protected int getInitialHealth() {
        return 2;
    }

    @Override
    public String getTextureName() {
        return "assets/buildings/asteroid_turret.png";
    }

    @Override
    void onShot(GameObject target) {
        var asteroid = (ToughAsteroid) target;
        asteroid.damage();
        mineLaserManager.addLine(getPos(), asteroid.getPos().cpy());
    }
}
