package tsvdh.asteroids.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import tsvdh.asteroids.game.classic.ClassicGame;
import tsvdh.asteroids.game.tower_defense.TowerDefenseGame;

import java.util.Map;

public class MainMenu extends Game {

    private final static float WORLD_SIZE = 500;
    private final static float CAMERA_SIZE = 500;

    private Class<? extends Game> nextGame;

    public MainMenu(Map<String, Texture> textures) {
        super(textures);
    }

    @Override
    public void create() {
        super.create();

    }

    @Override
    protected float getWorldSize() {
        return WORLD_SIZE;
    }

    @Override
    protected float getCameraSize() {
        return CAMERA_SIZE;
    }

    @Override
    protected void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
            nextGame = ClassicGame.class;

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
            nextGame = TowerDefenseGame.class;
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
        return nextGame != null;
    }

    public Class<? extends Game> getNextGame() {
        return nextGame;
    }
}
