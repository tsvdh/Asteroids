package tsvdh.asteroids.game.tower_defense;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import tsvdh.asteroids.game.Game;
import tsvdh.asteroids.logic.GameObject;
import tsvdh.asteroids.logic.Laser;
import tsvdh.asteroids.logic.Ship;
import tsvdh.asteroids.util.PersistentDataManager;
import tsvdh.asteroids.util.Text;
import tsvdh.asteroids.util.text_manager.AbsoluteTextManager;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class TowerDefenseGame extends Game {

    private final static float WORLD_SIZE = 2000;
    private final static float CAMERA_SIZE = 1000;

    private Ship ship;
    private final Collection<Laser> shipLasers = new LinkedList<>();

    private AbsoluteTextManager textManager;

    public TowerDefenseGame(Map<String, Texture> textures, PersistentDataManager dataManager) {
        super(textures, dataManager);
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
    public void create() {
        super.create();
        ship = new Ship(textures, new Vector2(WORLD_SIZE / 2, WORLD_SIZE / 2));

        textManager = new AbsoluteTextManager(spriteBatch, "assets/fonts/Connection.ttf");
        textManager.addFont("normal", Color.ORANGE, 50);
        textManager.addText("test", "Hello world!",
                            new Vector2(WORLD_SIZE / 2, WORLD_SIZE / 2),
                            "normal", Text.AlignMode.CENTERED);
    }

    @Override
    protected void input() {
        standardInput(ship, shipLasers);
    }

    @Override
    protected void logic() {
        ship.logic();

        handleCollisions();
        handleOutOfBounds();
    }

    @Override
    protected void draw() {
        ScreenUtils.clear(Color.BLACK);

        viewPort.getCamera().position.set(new Vector3(ship.getPos(), 0));
        viewPort.apply();
        spriteBatch.setProjectionMatrix(viewPort.getCamera().combined);
        spriteBatch.begin();

        ship.draw(spriteBatch);

        textManager.draw();

        spriteBatch.end();
    }

    @Override
    protected boolean gameOver() {
        return false;
    }

    @Override
    protected void handleCollisions() {

    }

    @Override
    protected void handleOutOfBounds(GameObject gameObject) {

    }

    @Override
    protected void handleOutOfBounds() {

    }

    @Override
    public boolean gameShouldExit() {
        return false;
    }

    @Override
    public void dispose() {
        super.dispose();
        textManager.dispose();
    }
}
