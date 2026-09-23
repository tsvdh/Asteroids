package tsvdh.asteroids.game.tower_defense.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.game.tower_defense.Alien;
import tsvdh.asteroids.logic.GameObject;
import tsvdh.asteroids.logic.Laser;
import tsvdh.asteroids.util.text_manager.AbsoluteTextManager;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;

public class DefenseTurret extends ShootingBuilding {

    private final Map<String, Texture> textures;
    private final Collection<Laser> turretLasers;

    public DefenseTurret(Map<String, Texture> textures, Vector2 pos, AbsoluteTextManager textManager,
                         Collection<Alien> aliens, Collection<Laser> turretLasers) {
        super(textures, pos, textManager, Duration.ofMillis(50), aliens, 300);
        this.textures = textures;
        this.turretLasers = turretLasers;
    }

    @Override
    protected int getInitialHealth() {
        return 3;
    }

    @Override
    protected String getDetailTextureName() {
        return "assets/buildings/defense_turret.png";
    }

    @Override
    void onShot(GameObject target) {
        Vector2 shootDir = target.getPos().cpy().sub(getPos()).nor();

        var laser = new Laser(textures, getPos(), Duration.ofMillis(1000));
        laser.setMovement(shootDir.setLength(500));
        turretLasers.add(laser);
        movingElement.setForward(shootDir);
    }
}
