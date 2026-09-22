package tsvdh.asteroids.game.tower_defense.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.game.tower_defense.Miner;
import tsvdh.asteroids.util.text_manager.AbsoluteTextManager;

import java.util.Map;

public class MineBuilding extends Building implements Miner {

    private float mineSpeed;

    public MineBuilding(Map<String, Texture> textures, Vector2 pos, AbsoluteTextManager textManager) {
        super(textures, pos, textManager);
        mineSpeed = 0.5f;
    }

    @Override
    public String getTextureName() {
        return "assets/buildings/miner.png";
    }

    @Override
    public float mine() {
        return mineSpeed * Gdx.graphics.getDeltaTime();
    }

    @Override
    protected int getInitialHealth() {
        return 1;
    }
}
