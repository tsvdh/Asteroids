package tsvdh.asteroids.game.tower_defense;

import com.badlogic.gdx.graphics.Texture;
import tsvdh.asteroids.game.Game;
import tsvdh.asteroids.util.PersistentDataManager;

import java.util.Map;

public class TowerDefenseGame extends Game {

    public TowerDefenseGame(Map<String, Texture> textures, PersistentDataManager dataManager) {
        super(textures, dataManager);
    }

    @Override
    protected float getWorldSize() {
        return 0;
    }

    @Override
    protected float getCameraSize() {
        return 0;
    }

    @Override
    protected void input() {

    }

    @Override
    protected void logic() {

    }

    @Override
    protected void draw() {

    }

    @Override
    protected boolean gameOver() {
        return false;
    }

    @Override
    protected float handleOutOfBounds(float val) {
        return 0;
    }

    @Override
    public boolean gameShouldExit() {
        return false;
    }
}
