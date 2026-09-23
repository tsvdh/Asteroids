package tsvdh.asteroids.game.tower_defense.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tsvdh.asteroids.game.tower_defense.Miner;
import tsvdh.asteroids.util.text_manager.AbsoluteTextManager;

import java.util.Map;

public class MineBuilding extends Building implements Miner {

    private float mineSpeed;
    private float rotationSpeed;
    private float curRotation;

    public MineBuilding(Map<String, Texture> textures, Vector2 pos, AbsoluteTextManager textManager) {
        super(textures, pos, textManager);
        mineSpeed = 0.5f;
        rotationSpeed = -0.2f;
        curRotation = 0;
    }

    @Override
    public String getTextureName() {
        return "assets/buildings/miner_background.png";
    }

    @Override
    public float mine() {
        return mineSpeed * Gdx.graphics.getDeltaTime();
    }

    @Override
    protected int getInitialHealth() {
        return 1;
    }

    @Override
    protected String getDetailTextureName() {
        return "assets/buildings/miner.png";
    }

    @Override
    public void logic() {
        super.logic();
        curRotation = (curRotation + rotationSpeed * 360 * Gdx.graphics.getDeltaTime()) % 360;
        getDetailSprite().setRotation(curRotation);
    }
}
